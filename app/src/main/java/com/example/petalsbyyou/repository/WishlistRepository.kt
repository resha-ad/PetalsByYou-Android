package com.example.petalsbyyou.repository

import com.example.petalsbyyou.model.WishlistModel

interface WishlistRepository {
    fun addToWishlist(wishlistModel: WishlistModel, callback: (Boolean, String) -> Unit)
    fun removeFromWishlist(wishlistId: String, callback: (Boolean, String) -> Unit)
    fun getWishlistItems(userId: String, callback: (List<WishlistModel>?, Boolean, String) -> Unit)
}