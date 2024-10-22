package com.allergyguardian.allergyguardian.presentation.franchise.franchise_home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.model.franchise.Menu
import com.allergyguardian.allergyguardian.databinding.RecyclerviewFranchiseMenuBinding

class FranchiseHomeAdapter() :
    ListAdapter<Menu, FranchiseHomeAdapter.HomeViewHolder>(object :
        DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem == newItem
        }
    }) {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            RecyclerviewFranchiseMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    // 홀더에 실제 데이터 할당
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        runCatching {
            holder.bind(getItem(position))
            holder.itemView.setOnClickListener {
                itemClick?.onClick(it, position)
            }
        }.onFailure { exception ->
            Log.e("FranchiseHomeAdapter", "Exception! ${exception.message}")
        }
    }

    class HomeViewHolder(binding: RecyclerviewFranchiseMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val photo = binding.ivItemlistPhoto
        val brand = binding.tvItemlistFranchise
        val name = binding.tvItemlistName
        val allergy = binding.tvItemlistAllergy
        val like = binding.ivLike
        fun bind(item: Menu) {
            when (item.type) {
                "패스트푸드" -> photo.setImageResource(R.drawable.hamburger)
                "피자" -> photo.setImageResource(R.drawable.pizza)
                "치킨" -> photo.setImageResource(R.drawable.chicken)
                "카페" -> photo.setImageResource(R.drawable.coffee)
                "아이스크림" -> photo.setImageResource(R.drawable.ice_cream)
                "베이커리/도넛" -> photo.setImageResource(R.drawable.doughnut)
                "샌드위치" -> photo.setImageResource(R.drawable.sandwich)
            }
            brand.text = item.brand
            name.text = item.name
            allergy.text = "⚠️"+"${item.allergy}"
        }
    }
}

