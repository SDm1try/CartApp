package com.example.lecture5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAppleRecyclerViewAdapter(private val appleEatingWarning: () -> Unit) : RecyclerView.Adapter<MyHolder>() {
    val summaryItem = SummaryItem
    val items = mutableListOf<Item>(summaryItem)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return when (viewType) {
            CART -> CartViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_item_view, parent, false))
            APPLE -> AppleViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.apple_item_view, parent, false))
            SUMMARY -> SummaryViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.summary_item_view, parent, false))
            else -> throw NoSuchFieldException("Неизвестный тип")
        }
    }


    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is CartItem -> CART
        is AppleItem -> APPLE
        is SummaryItem -> SUMMARY
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        when (val item = items[position]) {
            is CartItem -> {
                item.position = position
                (holder as CartViewHolder).button.setOnClickListener {
                    if (item.currentSize < 3) {
                        val applePosition = item.position + 1
                        items.add(applePosition, AppleItem(item, applePosition))

                        itemInserted(applePosition)
                        summaryItem.totalApples++
                        item.currentSize++
                    } else
                        appleEatingWarning()
                }
            }
            is AppleItem -> {
                (holder as AppleViewHolder).button.setOnClickListener {
                    items.remove(item)

                    itemRemoved(item.position)
                    item.cart.currentSize--
                    summaryItem.totalApples--
                }
            }
            is SummaryItem -> {
                (holder as SummaryViewHolder).textView.text = item.totalApples.toString()
            }
        }
    }

    fun itemInserted(position: Int) {
        items.forEachIndexed { index, item ->
            if (index > position)
                item.position++
        }
        notifyItemInserted(position)
        notifyItemChanged(items.lastIndex)
    }

    fun itemRemoved(position: Int) {
        items.forEachIndexed { index, item ->
            if (index >= position)
                item.position--
        }
        notifyItemRemoved(position)
        notifyItemChanged(items.lastIndex)
    }

    override fun getItemCount(): Int = items.size

    companion object {
        const val CART = 0
        const val APPLE = 1
        const val SUMMARY = 2
    }

}

sealed class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class CartViewHolder(itemView: View) : MyHolder(itemView) {
    var button: Button = itemView.findViewById(R.id.takeAppleButton)
}

class AppleViewHolder(itemView: View) : MyHolder(itemView) {
    val button = itemView.findViewById<Button>(R.id.eat_apple_button)
}

class SummaryViewHolder(itemView: View) : MyHolder(itemView) {
    val textView = itemView.findViewById<TextView>(R.id.appleSumText)
}