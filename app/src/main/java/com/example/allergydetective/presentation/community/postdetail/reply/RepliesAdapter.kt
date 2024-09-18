package com.example.allergydetective.presentation.community.postdetail.reply

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.allergydetective.R
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

    interface ItemClick2 {
        fun onClick2(view: View, position: Int)
    }

    var itemClick : ItemClick? = null
    var itemClick2 : ItemClick2? = null

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
                val adapterPosition = holder.bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemClick?.onClick(it, adapterPosition)
                }
            }

            holder.menu.setOnClickListener {
                val adapterPosition = holder.bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemClick2?.onClick2(it, adapterPosition)
                }
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
        val menu = binding.btnMenu

        fun bind(item: Reply) {
            val imageUrl = item.replierPhoto
            photo.load(imageUrl) {
                placeholder(R.drawable.placeholder) // 로딩 중 보여줄 이미지
                error(R.drawable.group_member) // 로드 실패 시 보여줄 기본 이미지
                listener(
                    onSuccess = { _, result ->
                        Log.d("Coil", "Image load succeeded")
                    },
                    onError = { _, result ->
                        Log.e("Coil", "Image load failed: ${result.throwable.message}")
                    }
                )
            }
            name.text = item.replierNickname
            detail.text = item.detail
        }
    }
    fun updateData(newItems: List<Reply>) {
        submitList(newItems)
    }

    fun removeItem(fromPosition: Int) {
        val currentList = currentList.toMutableList() // 현재 리스트를 mutable로 복사
        currentList.removeAt(fromPosition) // 아이템 제거

        submitList(currentList) // 변경된 리스트를 제출
    }
}
