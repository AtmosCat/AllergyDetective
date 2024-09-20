package com.allergyguardian.allergyguardian.presentation.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.model.food.Food
import com.allergyguardian.allergyguardian.databinding.RecyclerviewItemlistBinding
import com.allergyguardian.allergyguardian.presentation.UserViewModel

// ListAdapter 상속받아서 Home화면용 어댑터 구현
class HomeAdapter(private val userViewModel: UserViewModel) :
    ListAdapter<Food, HomeAdapter.HomeViewHolder>(object :
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            RecyclerviewItemlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    // 홀더에 실제 데이터 할당
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        runCatching {
            holder.bind(getItem(position), userViewModel.currentUser.value!!.like)
            holder.itemView.setOnClickListener {
                itemClick?.onClick(it, position)
            }
        }.onFailure { exception ->
            Log.e("HomeAdapter", "Exception! ${exception.message}")
        }
    }

    class HomeViewHolder(binding: RecyclerviewItemlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val photo = binding.ivItemlistPhoto
            val name = binding.tvItemlistName
            val allergy = binding.tvItemlistAllergy
            val rawmatrl = binding.tvItemlistRawmtrl
            val like = binding.ivLike
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
}
