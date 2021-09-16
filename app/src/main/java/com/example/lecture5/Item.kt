package com.example.lecture5

sealed interface Item {
    var position: Int
}
data class CartItem(var currentSize: Int = 0, override var position: Int) : Item {
}

data class AppleItem(val cart: CartItem, override var position: Int) : Item {
}

object SummaryItem: Item {
    var totalApples = 0
    override var position: Int = 0
}
