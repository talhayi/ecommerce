package com.example.ecommerce.domain.model

import java.io.Serializable

data class Product(
    var name: String? = null,
    var image: String? = null,
    var price: String? = null,
    var description: String? = null,
    var id: String? = null
):Serializable