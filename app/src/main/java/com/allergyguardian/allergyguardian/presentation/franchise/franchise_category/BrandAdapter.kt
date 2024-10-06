package com.allergyguardian.allergyguardian.presentation.franchise.franchise_category

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allergyguardian.allergyguardian.databinding.RecyclerviewBrandsBinding
class BrandAdapter :
    ListAdapter<MutableList<String>, BrandAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<MutableList<String>>() {
        override fun areItemsTheSame(oldItem: MutableList<String>, newItem: MutableList<String>): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MutableList<String>, newItem: MutableList<String>): Boolean {
            return true
        }
    }) {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RecyclerviewBrandsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        runCatching {
            holder.bind(getItem(position))
            holder.itemView.setOnClickListener {
                itemClick?.onClick(it, position)
            }
        }.onFailure { exception ->
            Log.e("BrandAdapter", "Exception! ${exception.message}")
        }
    }

    class ViewHolder(binding: RecyclerviewBrandsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var brand = binding.tvBrand

        fun bind(items: MutableList<String>) {
            brand.text = items[position]
        }
    }
}

