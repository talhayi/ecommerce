package com.example.ecommerce.data.remote.dto

import com.example.ecommerce.domain.model.Product

data class ProductDto(
    var createdAt: String? = null,
    var name: String? = null,
    var image: String? = null,
    var price: String? = null,
    var description: String? = null,
    var model: String? = null,
    var brand: String? = null,
    var id: String? = null
)

fun ProductDto.toProduct(): Product {
    return Product(
        name = this.name,
        image = this.image,
        price = this.price,
        description = this.description,
        id = this.id
    )
}

fun List<ProductDto>.toProductList(): List<Product> {
    return this.map { it.toProduct() }
}