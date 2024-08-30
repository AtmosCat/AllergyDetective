package com.example.allergydetective.presentation.itemdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.allergydetective.R

class ViewPagerAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<ViewPagerAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        holder.imageView.load(imageUrl) {
            placeholder(R.drawable.placeholder) // 이미지 로딩 중 보여줄 플레이스홀더 이미지
            error(R.drawable.error_image) // 이미지 로딩 실패 시 보여줄 이미지
        }
    }

    override fun getItemCount(): Int = imageUrls.size
}
