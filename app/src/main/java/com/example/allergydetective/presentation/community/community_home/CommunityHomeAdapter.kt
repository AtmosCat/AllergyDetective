package com.example.allergydetective.presentation.community.community_home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.allergydetective.data.model.user.Comments
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.databinding.RecyclerviewPostListBinding

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
                itemClick?.onClick(it, position)
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
            photo.load(item.posterPhoto)
            category.text = item.category
            title.text = item.title
            detail.text = item.detail
            scrap.text = item.scrap.toString()
            comment.text = item.comments.size.toString()
        }
    }
}
//
//data class Post(
//    val id: String = "",
//    val category: String = "",
//    val posterPhoto: String = "",
//    val posterName : String = "",
//    val title: String = "",
//    val detail: String = "",
//    val comments: List<Comments> = mutableListOf(),
//    val like: Int = 0,
//    val report: Boolean = false)
