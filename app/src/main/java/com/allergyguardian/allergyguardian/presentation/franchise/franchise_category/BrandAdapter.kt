package com.allergyguardian.allergyguardian.presentation.franchise.franchise_category

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.databinding.RecyclerviewBrandsBinding

class BrandAdapter : ListAdapter<String, BrandAdapter.ViewHolder>(object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}) {

    private var selectedPosition: Int? = null

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewBrandsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))

        val currentPosition = holder.bindingAdapterPosition

        holder.itemView.setOnClickListener {
            selectedPosition = currentPosition
            itemClick?.onClick(it, currentPosition)
        }

        holder.itemView.setBackgroundColor(
            if (currentPosition == selectedPosition) {
                ContextCompat.getColor(holder.itemView.context, R.color.main_color_orange)
            } else {
                Color.parseColor("#E4E4E4")
            }
        )

        holder.brand.setTextColor(
            if (currentPosition == selectedPosition) {
                Color.parseColor("#FFFFFF")
            } else {
                Color.parseColor("#6A6F7A")
            }
        )
    }

    class ViewHolder(binding: RecyclerviewBrandsBinding) : RecyclerView.ViewHolder(binding.root) {
        var brand = binding.tvBrand

        fun bind(item: String) {
            brand.text = item
        }
    }
}
