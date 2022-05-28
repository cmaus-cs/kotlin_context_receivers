package service

import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.ValidatedNel
import model.*

@JvmInline
value class OrderError(val value: String)

@JvmInline
value class ProcessedOrder(val value: Order)

@JvmInline
value class ItemsStockQuery(val value: List<Sku>)

interface WarehouseRepository {
    fun reportNumberOfItems(query: ItemsStockQuery): ItemsStock
}

context(WarehouseRepository)
fun processOrder(order: Order): ValidatedNel<OrderError, ProcessedOrder> {
    val stock = reportNumberOfItems(ItemsStockQuery(order.items.map { it.sku }))
    return orderCanBeFulfilled(order, stock)
        .mapLeft(NonEmptyList<OrderFulfillmentError>::toOrderError)
        .map(::ProcessedOrder)
}

fun Nel<OrderFulfillmentError>.toOrderError() = map { OrderError("stock for ${it.sku} is not sufficient") }
