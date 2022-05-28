package model

import arrow.core.*


data class Customer(val id: String)
data class OrderItem(val sku: Sku, val count: Int)
data class Order(val customer: Customer, val items: List<OrderItem>)

@JvmInline
value class Sku(val value: String)

@JvmInline
value class ItemsStock(val value: Map<Sku, Int>)

data class OrderFulfillmentError(val sku: Sku, val requestedCount: Int, val availableCount: Int)

fun orderCanBeFulfilled(order: Order, stock: ItemsStock): ValidatedNel<OrderFulfillmentError, Order> =
    order.items.traverse {
        val availableCount = stock.value.getOrDefault(it.sku, 0)
        if (availableCount >= it.count) {
            it.validNel()
        } else {
            OrderFulfillmentError(it.sku, it.count, availableCount).invalidNel()
        }
    }.map { order }
