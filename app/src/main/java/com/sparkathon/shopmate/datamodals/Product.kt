data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    var quantity: Int = 1
)