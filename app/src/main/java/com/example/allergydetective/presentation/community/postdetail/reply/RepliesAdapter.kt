package com.example.allergydetective.presentation.community.postdetail.reply

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.data.model.user.Reply
import com.example.allergydetective.databinding.RecyclerviewCommentRepliesBinding

class RepliesAdapter :
    ListAdapter<Reply, RepliesAdapter.Holder>(object :
        DiffUtil.ItemCallback<Reply>() {
        // 구 값, 신 값 비교해서 바뀐 것들만 업데이트
        override fun areItemsTheSame(oldItem: Reply, newItem: Reply): Boolean {
            return oldItem.detail == newItem.detail
        }
        override fun areContentsTheSame(oldItem: Reply, newItem: Reply): Boolean {
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
            RecyclerviewCommentRepliesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            Log.e("RepliesAdapter", "Exception! ${exception.message}")
        }
    }

    class Holder(binding: RecyclerviewCommentRepliesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val photo = binding.ivReplier
        val name = binding.tvReplier
        val detail = binding.tvReplyDetail

        fun bind(item: Reply) {
            photo.load(item.replierPhoto)
            name.text = item.replierName
            detail.text = item.detail
        }
    }
    fun updateData(newItems: List<Reply>) {
        submitList(newItems)
    }
}
