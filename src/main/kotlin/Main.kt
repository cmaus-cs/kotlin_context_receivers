import model.*
import service.ItemsStockQuery
import service.WarehouseRepository
import service.processOrder

fun main(args: Array<String>) {
    val order = Order(
        customer = Customer(id = "Helmut"),
        items = listOf(
            OrderItem(sku = Sku("x42-6645"), count = 42)
        )
    )

    val console = object : Console {
        override fun output(string: String) {
            println(string)
        }
    }

    val repo = object : WarehouseRepository {
        override fun reportNumberOfItems(query: ItemsStockQuery): ItemsStock = ItemsStock(mapOf())
    }

    with(console) {
        with(repo) {
            doProcessOrder(order)
        }
    }
}

interface Console {
    fun output(string: String)
}

context(Console, WarehouseRepository)
fun doProcessOrder(order: Order) {
    val result = processOrder(order)
    output(result.toString())
}

