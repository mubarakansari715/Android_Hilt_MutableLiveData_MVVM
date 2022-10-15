package com.mubarak.android_hilt_mutablelivedata_mvvm.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mubarak.android_hilt_mutablelivedata_mvvm.databinding.CustomViewBinding
import com.mubarak.android_hilt_mutablelivedata_mvvm.home.model.HomeDataClass

class HomeAdapter(
    private val homeDataList: List<HomeDataClass>
    ) : RecyclerView.Adapter<HomeAdapter.HomeAdapterViewHolder>() {

    class HomeAdapterViewHolder(private val binding: CustomViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindingData(dataModel: HomeDataClass) {
            binding.model = dataModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        return HomeAdapterViewHolder(
            CustomViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {
        holder.bindingData(homeDataList[position])

        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = homeDataList.size
}