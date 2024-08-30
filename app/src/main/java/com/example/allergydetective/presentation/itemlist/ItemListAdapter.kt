package com.example.allergydetective.presentation.itemlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.databinding.RecyclerviewItemlistBinding


// ListAdapter 상속받아서 Home화면용 어댑터 구현
class ItemListAdapter :
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

    // RecyclerView 돌아갈 때 새로운 뷰 홀더 생성 및 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val binding =
            RecyclerviewItemlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemListViewHolder(binding)
    }

    // 홀더에 실제 데이터 할당
    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        runCatching {
            holder.bind(getItem(position))
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
        val like = binding.tvLike
        fun bind(item: Food) {
            photo.load(item.imgurl1)
            name.text = item.prdlstNm
            allergy.text = "⚠️주의: ${item.allergy}"
            rawmatrl.text = "- 원재료: ${item.rawmtrl}"
            like.text = item.like.toString()
//                crossfade(true)
        }
    }

    fun updateData(newItems: List<Food>) {
        submitList(newItems)
    }
}