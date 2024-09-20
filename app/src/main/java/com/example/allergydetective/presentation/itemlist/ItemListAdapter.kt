package com.example.allergydetective.presentation.itemlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.allergydetective.R
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.databinding.RecyclerviewItemlistBinding
import com.example.allergydetective.presentation.UserViewModel


// ListAdapter 상속받아서 Home화면용 어댑터 구현
class ItemListAdapter(private val userViewModel: UserViewModel) :
    ListAdapter<Food, ItemListAdapter.ItemListViewHolder>(object :
        DiffUtil.ItemCallback<Food>() {
        // 구 값, 신 값 비교해서 바뀐 것들만 업데이트
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.prdlstNm == newItem.prdlstNm
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }
    }) {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val binding =
            RecyclerviewItemlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        runCatching {
            holder.bind(getItem(position), userViewModel.currentUser.value!!.like)
            holder.itemView.setOnClickListener {
                itemClick?.onClick(it, position)
            }
        }.onFailure { exception ->
            Log.e("ItemListAdapter", "Exception! ${exception.message}")
        }
    }

    class ItemListViewHolder(binding: RecyclerviewItemlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val photo = binding.ivItemlistPhoto
        val name = binding.tvItemlistName
        val allergy = binding.tvItemlistAllergy
        val rawmatrl = binding.tvItemlistRawmtrl
        val like = binding.ivLike

//        val like = binding.tvLike
        fun bind(item: Food, likedFoods: MutableList<Food>) {
            photo.load(item.imgurl1)
            name.text = item.prdlstNm
            allergy.text = "⚠️주의: ${item.allergy}"
            rawmatrl.text = "- 원재료: ${item.rawmtrl}"
            if (item in likedFoods) {
                like.setImageResource(R.drawable.filled_heart)
            } else {
                like.setImageResource(R.drawable.heart)
            }
//            like.text = item.like.toString()
//                crossfade(true)
        }
    }

    fun updateData(newItems: List<Food>) {
        submitList(newItems)
    }
}

