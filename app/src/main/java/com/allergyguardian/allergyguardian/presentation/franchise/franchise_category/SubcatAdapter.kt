package com.allergyguardian.allergyguardian.presentation.franchise.franchise_category

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allergyguardian.allergyguardian.databinding.RecyclerviewSubcatsBinding

class SubcatAdapter : ListAdapter<String, SubcatAdapter.ViewHolder>(object : DiffUtil.ItemCallback<String>() {
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
        val binding = RecyclerviewSubcatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = if (selectedPosition == position) null else position

            notifyItemChanged(previousPosition ?: -1) // 이전 위치를 갱신, previousPosition이 null일 경우 의미없는 -1을 대입해 오류 방지
            notifyItemChanged(position) // 현재 선택된 위치 갱신

            // 클릭된 아이템에 대한 클릭 리스너 호출
            itemClick?.onClick(it, position)
        }

        holder.updateViewAppearance(selectedPosition == position)
    }

    class ViewHolder(private val binding: RecyclerviewSubcatsBinding) : RecyclerView.ViewHolder(binding.root) {
        var subcat = binding.tvSubcat
        fun bind(item: String) {
            subcat.text = item
        }
        fun updateViewAppearance(isSelected: Boolean) {
            itemView.setBackgroundColor(
                if (isSelected) {
                    Color.parseColor("#4169E1")
                } else {
                    Color.parseColor("#E4E4E4")
                }
            )
            subcat.setTextColor(
                if (isSelected) {
                    Color.parseColor("#FFFFFF")
                } else {
                    Color.parseColor("#6A6F7A")
                }
            )
        }
    }
}
