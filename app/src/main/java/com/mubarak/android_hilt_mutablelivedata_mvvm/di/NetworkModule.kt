package com.mubarak.android_hilt_mutablelivedata_mvvm.di

import android.net.Uri.decode
import android.util.Log
import com.mubarak.android_hilt_mutablelivedata_mvvm.network.ApiInterface
import com.mubarak.android_hilt_mutablelivedata_mvvm.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class NetworkModule {

    companion object {
        private const val TAG = "ApiClient"
        private const val CONNECT_TIMEOUT_MULTIPLIER = 1
        private const val DEFAULT_CONNECT_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
        private const val DEFAULT_WRITE_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
        private const val DEFAULT_READ_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
        private const val NO_OF_LOG_CHAR = 1000
    }

    private val sDispatcher: Dispatcher? = null

    /***
     * Retrofit calling api
     */
    @Provides
    fun providesApiObj(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClientBuilder().build())
            .build()
            .create(ApiInterface::class.java)
    }

    /*OkHttp client builder*/
    /***
     * Factory for calls, which can be used to send HTTP requests and read their responses.
     */
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        val oktHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(
                (CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_CONNECT_TIMEOUT_IN_SEC).toLong(),
                TimeUnit.SECONDS
            )
            .readTimeout(
                (CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_READ_TIMEOUT_IN_SEC).toLong(),
                TimeUnit.SECONDS
            )
            .writeTimeout(
                (CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_WRITE_TIMEOUT_IN_SEC).toLong(),
                TimeUnit.SECONDS
            )
        //.cookieJar(JavaNetCookieJar(getCookieManager())) /* Using okhttp3 cookie instead of java net cookie*/

        oktHttpClientBuilder.dispatcher(getDispatcher())

        oktHttpClientBuilder.addInterceptor { chain ->
            val builder = chain.request().newBuilder()
                .addHeader("Content-Type", "text/html")
                .addHeader("authKey", "")
                .addHeader("Authorization", Credentials.basic(username = "", password = ""))
            chain.proceed(builder.build())
        }

        oktHttpClientBuilder.addInterceptor(getHttpLoggingInterceptor())

        oktHttpClientBuilder.addInterceptor { chain ->
            val request = chain.request()
            printPostmanFormattedLog(request)
            val response = chain.proceed(request)
            Log.e(TAG, "intercept" + response.code)
            response
        }

        return oktHttpClientBuilder
    }

    /**
     * async requests are executed
     * Each dispatcher uses an ExecutorService to run calls internally.
     * If you supply your own executor, it should be able to run the configured maximum number of calls concurrently.
     */
    private fun getDispatcher(): Dispatcher {
        return sDispatcher ?: Dispatcher()
    }

    /***
     * Typically interceptors add, remove, or transform headers on the request or response.
     */
    private fun getHttpLoggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (message.length > NO_OF_LOG_CHAR) {
                    for (noOfLogs in 0..message.length / NO_OF_LOG_CHAR) {
                        if (noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR < message.length) {
                            Log.d(
                                TAG,
                                message.substring(
                                    noOfLogs * NO_OF_LOG_CHAR,
                                    noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR
                                )
                            )
                        } else {
                            Log.d(
                                TAG,
                                message.substring(noOfLogs * NO_OF_LOG_CHAR, message.length)
                            )
                        }
                    }
                } else {
                    Log.d(TAG, message)
                }
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    /**
     * print postman request parameter
     */
    private fun printPostmanFormattedLog(request: Request) {
        try {
            val allParams: String = if (request.method == "GET" || request.method == "DELETE") {
                request.url.toString().substring(
                    request.url.toString().indexOf("?") + 1,
                    request.url.toString().length
                )
            } else {
                val buffer = Buffer()
                request.body!!.writeTo(buffer)
                buffer.readString(Charset.forName("UTF-8"))
            }
            val params =
                allParams.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val paramsString = StringBuilder("\n")
            for (param in params) {
                paramsString.append(decode(param.replace("=", ":")))
                paramsString.append("\n")
            }
            Log.d(TAG, paramsString.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}