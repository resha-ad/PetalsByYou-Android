package com.example.petalsbyyou.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petalsbyyou.model.WishlistModel
import com.example.petalsbyyou.repository.WishlistRepository

class WishlistViewModel(private val repo: WishlistRepository) : ViewModel() {

    private val _wishlistItems = MutableLiveData<List<WishlistModel>>()
    val wishlistItems: MutableLiveData<List<WishlistModel>> get() = _wishlistItems

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    fun addToWishlist(wishlistModel: WishlistModel, callback: (Boolean, String) -> Unit) {
        repo.addToWishlist(wishlistModel, callback)
    }

    fun removeFromWishlist(wishlistId: String, callback: (Boolean, String) -> Unit) {
        repo.removeFromWishlist(wishlistId, callback)
    }

    fun getWishlistItems(userId: String) {
        _loading.value = true
        repo.getWishlistItems(userId) { wishlistItems, success, message ->
            if (success) {
                _wishlistItems.value = wishlistItems
            }
            _loading.value = false
        }
    }
}