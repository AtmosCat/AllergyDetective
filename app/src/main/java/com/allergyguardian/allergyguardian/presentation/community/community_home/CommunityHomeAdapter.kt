package com.allergyguardian.allergyguardian.presentation.community.community_home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.model.user.Post
import com.allergyguardian.allergyguardian.databinding.RecyclerviewPostListBinding

class CommunityHomeAdapter :
    ListAdapter<Post, CommunityHomeAdapter.Holder>(object :
        DiffUtil.ItemCallback<Post>() {
        // 구 값, 신 값 비교해서 바뀐 것들만 업데이트
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
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
            RecyclerviewPostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    // 홀더에 실제 데이터 할당
    override fun onBindViewHolder(holder: Holder, position: Int) {
        runCatching {
            holder.bind(getItem(position))
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemClick?.onClick(it, adapterPosition)
                }
            }
        }.onFailure { exception ->
            Log.e("CommunityHomeAdapter", "Exception! ${exception.message}")
        }
    }

    class Holder(binding: RecyclerviewPostListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val photo = binding.ivPhoto
        val category = binding.tvCategory
        val title = binding.tvTitle
        val detail = binding.tvDetail
        val scrap = binding.tvScrap
        val comment = binding.tvComment

        fun bind(item: Post) {
            if (item.detailPhoto.isEmpty()) {
                photo.setImageResource(R.drawable.no_photo)
                photo.setPadding(40, 40, 40, 40)
            } else {
                photo.load(item.detailPhoto[0])
            }
            category.text = item.category
            title.text = item.title
            detail.text = item.detail
            scrap.text = item.scrap.toString()
            comment.text = item.comments.size.toString()
        }
    }
    fun updateData(newItems: List<Post>) {
        submitList(newItems)
        notifyDataSetChanged()
    }
}
