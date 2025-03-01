package com.example.petalsbyyou.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petalsbyyou.R
import com.example.petalsbyyou.model.ProductModel
import com.example.petalsbyyou.repository.ProductRepository

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _products = MutableLiveData<List<ProductModel>>()
    val products: LiveData<List<ProductModel>> get() = _products

    fun loadProducts() {
        // Load products from repository
        val products = listOf(
            ProductModel("1", "Rose Bouquet", "Love, passion, romance", 29.99, R.drawable.roses),
            ProductModel("2", "Tulip Collection", "Declaration of love, perfect love", 24.99, R.drawable.tulips),
            ProductModel("3", "Orchid Arrangement", "Beauty, luxury, refinement", 39.99, R.drawable.orchids),
            ProductModel(
                "4",
                "Sunflower Bunch",
                "Adoration, loyalty, longevity",
                19.99,
                R.drawable.sunflowers
            ),
            ProductModel(
                "5",
                "Lily Bouquet",
                "Purity, innocence, virtue",
                34.99,
                R.drawable.lily
            ),
            ProductModel(
                "6",
                "Forget-Me-Not Set",
                "Remembrance, true love, loyalty",
                27.99,
                R.drawable.forget_me_not
            ),
            ProductModel(
                "7",
                "Chrysanthemum Elegance",
                "Loyalty, friendship, abundance",
                32.99,
                R.drawable.chrysanthemum
            ),
            ProductModel(
                "8",
                "Lavender Bliss",
                "Serenity, calmness, devotion",
                22.99,
                R.drawable.lavenders
            ),
             ProductModel(
                "9",
                "Peony Romance",
                "Prosperity, good fortune, romance",
                42.99,
                R.drawable.peony
            ),
            ProductModel(
                "10",
                "Daisy Freshness",
                "Happiness, innocence, simplicity",
                18.99,
                R.drawable.daisy
            )
        )
        _products.value = products
    }
}