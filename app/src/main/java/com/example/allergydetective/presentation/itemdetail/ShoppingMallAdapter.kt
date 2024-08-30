package com.example.allergydetective.presentation.itemdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.allergydetective.R
import com.example.allergydetective.data.model.market.Market
import com.example.allergydetective.databinding.RecyclerviewShoppingListBinding
import java.text.NumberFormat

class ShoppingMallAdapter(private val items: List<Market>?)
    : RecyclerView.Adapter<ShoppingMallAdapter.Holder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerviewShoppingListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        if (items?.get(position)?.mallName.toString() == "네이버") {
            holder.ivMarketLogo.setImageResource(R.drawable.naver)
        } else if (items?.get(position)?.mallName.toString() == "G마켓") {
            holder.ivMarketLogo.setImageResource(R.drawable.gmarket)
        } else if (items?.get(position)?.mallName.toString() == "쿠팡") {
            holder.ivMarketLogo.setImageResource(R.drawable.coupang)
        } else if (items?.get(position)?.mallName.toString() == "옥션") {
            holder.ivMarketLogo.setImageResource(R.drawable.auction)
        } else if (items?.get(position)?.mallName.toString() == "11번가") {
            holder.ivMarketLogo.setImageResource(R.drawable.eleven_st)
        } else if (items?.get(position)?.mallName.toString() == "이마트몰") {
            holder.ivMarketLogo.setImageResource(R.drawable.emart)
        } else if (items?.get(position)?.mallName.toString() == "롯데온") {
            holder.ivMarketLogo.setImageResource(R.drawable.lotteon)
        } else if (items?.get(position)?.mallName.toString() == "홈플러스") {
            holder.ivMarketLogo.setImageResource(R.drawable.homeplus)
        } else if (items?.get(position)?.mallName.toString().contains("SSG")) {
            holder.ivMarketLogo.setImageResource(R.drawable.ssgcom)
        } else {
            holder.ivMarketLogo.setImageResource(R.drawable.market)
        }

        holder.tvMarketName.text = items?.get(position)?.mallName.toString()

        var edittedPrice = ""
        if (items?.get(position)?.lprice.toString().length >= 4) {
            try {
                val price = items?.get(position)?.lprice?.toLong()
                edittedPrice = NumberFormat.getInstance().format(price).toString() + "원"
            } catch (e: NumberFormatException) {
                edittedPrice = items?.get(position)?.lprice.toString() + "원"
            }
        } else {
            edittedPrice = items?.get(position)?.lprice.toString() + "원"
        }
        holder.tvLprice.text = edittedPrice

        holder.btnLink.setOnClickListener {
            itemClick?.onClick(it, position)
        }

    }

    inner class Holder(val binding: RecyclerviewShoppingListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivMarketLogo = binding.ivMarketLogo
        val tvMarketName = binding.tvMarketName
        val tvLprice = binding.tvLprice
        val btnLink = binding.btnLink
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

}