package com.example.lecture5

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val addCartButton = findViewById<Button>(R.id.add_cart)
        val deleteCartButton = findViewById<Button>(R.id.delete_carts)


        val adapter = CartAppleRecyclerViewAdapter {
            Toast.makeText(this,
                "Корзина вмещает только три яблока :(",
                Toast.LENGTH_LONG).show()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        addCartButton.setOnClickListener {
            adapter.items.add(0, CartItem(0, 0))
            recyclerView.scrollToPosition(0)
            adapter.itemInserted(0)
        }

        deleteCartButton.setOnClickListener {
            val end = adapter.items.lastIndex
            adapter.items.removeAll { it !is SummaryItem }
            adapter.summaryItem.totalApples = 0
            adapter.notifyItemRangeRemoved(0, end)
            adapter.notifyItemChanged(adapter.items.lastIndex)
        }
    }
}