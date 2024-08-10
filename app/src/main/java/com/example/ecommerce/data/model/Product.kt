package com.example.ecommerce.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var createdAt: String? = null,
    var name: String? = null,
    var image: String? = null,
    var price: String? = null,
    var description: String? = null,
    var model: String? = null,
    var brand: String? = null,
    var quantity: Int? = 1,
):Serializable