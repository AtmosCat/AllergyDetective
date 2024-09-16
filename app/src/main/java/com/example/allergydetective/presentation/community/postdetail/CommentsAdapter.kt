package com.example.allergydetective.presentation.community.postdetail

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
import com.example.allergydetective.databinding.RecyclerviewCommentsBinding

class CommentsAdapter :
    ListAdapter<Comments, CommentsAdapter.Holder>(object :
        DiffUtil.ItemCallback<Comments>() {
        // 구 값, 신 값 비교해서 바뀐 것들만 업데이트
        override fun areItemsTheSame(oldItem: Comments, newItem: Comments): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Comments, newItem: Comments): Boolean {
            return oldItem == newItem
        }
    }) {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    interface ItemClick2 {
        fun onClick2(view: View, position: Int)
    }

    var itemClick : ItemClick? = null
    var itemClick2 : ItemClick2? = null

    // RecyclerView 돌아갈 때 새로운 뷰 홀더 생성 및 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerviewCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    // 홀더에 실제 데이터 할당
    override fun onBindViewHolder(holder: Holder, position: Int) {
        runCatching {
            holder.bind(getItem(position))
            holder.itemView.setOnClickListener {
                itemClick?.onClick(it, position)
            }
            holder.menu.setOnClickListener{
                itemClick2?.onClick2(it, position)
            }
        }.onFailure { exception ->
            Log.e("CommentsAdapter", "Exception! ${exception.message}")
        }
    }

    class Holder(binding: RecyclerviewCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val photo = binding.ivCommenter
        val name = binding.tvCommenter
        val detail = binding.tvCommentDetail
        val reply = binding.btnReplyComment
        val menu = binding.btnMenu

        fun bind(item: Comments) {
            photo.load(item.commenterPhoto)
            name.text = item.commenterNickname
            detail.text = item.detail
            reply.text = "↳ 답글 ${item.reply.size}"
        }
    }
    fun updateData(newItems: List<Comments>) {
        submitList(newItems)
        notifyDataSetChanged()
    }
}
