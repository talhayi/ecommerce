package com.example.ecommerce.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String? = null,
    var image: String? = null,
    var price: String? = null,
    var description: String? = null,
    var quantity: Int? = 1,
):Serializable