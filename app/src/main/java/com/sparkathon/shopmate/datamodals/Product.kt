data class Rating(
    val rate: Float,
    val count: Int
)

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    var quantity: Int = 0,
    val description: String,
    val category: String,
    val rating: Rating
)