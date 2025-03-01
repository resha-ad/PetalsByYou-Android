package com.example.petalsbyyou.repository

import com.example.petalsbyyou.model.ProductModel

interface ProductRepository {
    fun getProductById(productId: String, callback: (ProductModel?, Boolean) -> Unit)
}