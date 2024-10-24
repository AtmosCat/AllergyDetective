package com.allergyguardian.allergyguardian.presentation.mypage.favorite

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.model.franchise.Menu
import com.allergyguardian.allergyguardian.databinding.RecyclerviewFranchiseMenuBinding
import com.allergyguardian.allergyguardian.presentation.UserViewModel

class FavoriteListAdapter(private val userViewModel: UserViewModel) :
    ListAdapter<Menu, FavoriteListAdapter.Holder>(object :
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

    // RecyclerView 돌아갈 때 새로운 뷰 홀더 생성 및 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerviewFranchiseMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    // 홀더에 실제 데이터 할당
    override fun onBindViewHolder(holder: Holder, position: Int) {
        runCatching {
            holder.bind(getItem(position), userViewModel.currentUser.value!!.like)
            holder.itemView.setOnClickListener {
                itemClick?.onClick(it, position)
            }
        }.onFailure { exception ->
            Log.e("FavoriteListAdapter", "Exception! ${exception.message}")
        }
    }

    class Holder(binding: RecyclerviewFranchiseMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val photo = binding.ivItemlistPhoto
        val brand = binding.tvItemlistFranchise
        val name = binding.tvItemlistName
        val allergy = binding.tvItemlistAllergy
        val like = binding.ivLike
        fun bind(
            item: Menu, likedMenus: MutableList<Menu>
        ) {
            when (item.type) {
                "패스트푸드" -> photo.setImageResource(R.drawable.hamburger)
                "피자" -> photo.setImageResource(R.drawable.pizza)
                "치킨" -> photo.setImageResource(R.drawable.chicken)
                "카페" -> photo.setImageResource(R.drawable.coffee)
                "아이스크림" -> photo.setImageResource(R.drawable.ice_cream)
                "베이커리/도넛" -> photo.setImageResource(R.drawable.doughnut)
                "샌드위치" -> photo.setImageResource(R.drawable.sandwich)
            }
            brand.text = "${item.brand} - ${item.subcat}"
            name.text = item.name
            allergy.text = "⚠️"+"${item.allergy}"
            if (item in likedMenus) {
                like.setImageResource(R.drawable.filled_heart)
            } else {
                like.setImageResource(R.drawable.heart)
            }
        }
    }

    fun updateData(newItems: List<Menu>) {
        submitList(newItems)
    }
}
