package com.allergyguardian.allergyguardian.presentation.itemdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.model.market.Market
import com.allergyguardian.allergyguardian.databinding.RecyclerviewShoppingListBinding
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

        holder.ivMarketLogo.setImageResource(R.drawable.market)
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