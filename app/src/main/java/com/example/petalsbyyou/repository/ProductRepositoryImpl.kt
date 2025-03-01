package com.example.petalsbyyou.repository

import com.example.petalsbyyou.R
import com.example.petalsbyyou.model.ProductModel

class ProductRepositoryImpl : ProductRepository {
    // Sample product data
    private val sampleProducts = mapOf(
        "1" to ProductModel("1", "Rose Bouquet", "Love, passion, romance", 29.99, R.drawable.roses),
        "2" to ProductModel("2", "Tulip Collection", "Declaration of love, perfect love", 24.99, R.drawable.tulips),
        "3" to ProductModel("3", "Orchid Arrangement", "Beauty, luxury, refinement", 39.99, R.drawable.orchids),
        "4" to ProductModel("4", "Sunflower Bunch", "Adoration, loyalty, longevity", 19.99, R.drawable.sunflowers),
        "5" to ProductModel("5", "Lily Bouquet", "Purity, innocence, virtue", 34.99, R.drawable.lily),
        "6" to ProductModel("6", "Forget-Me-Not Set", "Remembrance, true love, loyalty", 27.99, R.drawable.forget_me_not),
        "7" to ProductModel("7", "Chrysanthemum Elegance", "Loyalty, friendship, abundance", 32.99, R.drawable.chrysanthemum),
        "8" to ProductModel("8", "Lavender Bliss", "Serenity, calmness, devotion", 22.99, R.drawable.lavenders),
        "9" to ProductModel("9", "Peony Romance", "Prosperity, good fortune, romance", 42.99, R.drawable.peony),
        "10" to ProductModel("10", "Daisy Freshness", "Happiness, innocence, simplicity", 18.99, R.drawable.daisy)
    )

    override fun getProductById(productId: String, callback: (ProductModel?, Boolean) -> Unit) {
        val product = sampleProducts[productId]
        callback(product, product != null)
    }

}