package com.example.petalsbyyou.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petalsbyyou.adapter.WishlistAdapter
import com.example.petalsbyyou.databinding.FragmentWishlistBinding
import com.example.petalsbyyou.model.ProductModel
import com.example.petalsbyyou.model.WishlistModel
import com.example.petalsbyyou.repository.ProductRepositoryImpl
import com.example.petalsbyyou.repository.UserRepositoryImpl
import com.example.petalsbyyou.repository.WishlistRepositoryImpl
import com.example.petalsbyyou.ui.activity.LoginActivity
import com.example.petalsbyyou.ui.activity.RegistrationActivity

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var wishlistAdapter: WishlistAdapter
    private val wishlistRepository = WishlistRepositoryImpl() // Use WishlistRepository directly
    private val productRepository = ProductRepositoryImpl()
    private val userRepository = UserRepositoryImpl()
    private val wishlistItems = mutableListOf<WishlistModel>()
    private val productMap = mutableMapOf<String, ProductModel>()

    private val userId: String?
        get() = userRepository.getCurrentUser()?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if the user is logged in
        if (userId == null) {
            Toast.makeText(requireContext(), "Please log in to view your wishlist", Toast.LENGTH_SHORT).show()
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.wishlistContentLayout.visibility = View.GONE
            return
        }

        setupRecyclerView()
        loadWishlistItems()

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadWishlistItems()
        }

    }

    private fun setupRecyclerView() {
        wishlistAdapter = WishlistAdapter(
            requireContext(),
            wishlistItems,
            productMap,
            onRemoveClick = { wishlistId ->
                removeFromWishlist(wishlistId)
            },
            onProductClick = { productId ->
                viewProductDetails(productId)
            }
        )

        binding.recyclerViewWishlist.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = wishlistAdapter
        }
    }

    private fun loadWishlistItems() {
        binding.swipeRefreshLayout.isRefreshing = true

        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            binding.swipeRefreshLayout.isRefreshing = false
            return
        }

        wishlistRepository.getWishlistItems(userId!!) { items, success, message ->
            if (_binding == null) return@getWishlistItems

            binding.swipeRefreshLayout.isRefreshing = false

            if (success && items != null) {
                wishlistItems.clear()
                wishlistItems.addAll(items)
                loadProductsForWishlistItems() // Fetch product details for each wishlist item
            } else {
                Toast.makeText(context, "Failed to load wishlist items: $message", Toast.LENGTH_SHORT).show()
                toggleEmptyState()
            }
        }
    }

    private fun loadProductsForWishlistItems() {
        if (wishlistItems.isEmpty()) {
            toggleEmptyState()
            return
        }

        var loadedCount = 0
        productMap.clear()

        for (wishlistItem in wishlistItems) {
            productRepository.getProductById(wishlistItem.productId) { product, success ->
                if (_binding == null) return@getProductById

                if (success && product != null) {
                    productMap[product.productId] = product
                }

                loadedCount++
                if (loadedCount >= wishlistItems.size) {
                    wishlistAdapter.notifyDataSetChanged()
                    toggleEmptyState()
                }
            }
        }
    }

    private fun removeFromWishlist(wishlistId: String) {
        wishlistRepository.removeFromWishlist(wishlistId) { success, message ->
            if (_binding == null) return@removeFromWishlist

            if (success) {
                val position = wishlistItems.indexOfFirst { it.wishlistId == wishlistId }
                if (position != -1) {
                    wishlistItems.removeAt(position)
                    wishlistAdapter.notifyItemRemoved(position)
                    toggleEmptyState()
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun viewProductDetails(productId: String) {
        // Navigate to product details screen (implement this later)
        Toast.makeText(context, "Viewing product: $productId", Toast.LENGTH_SHORT).show()
    }

    private fun toggleEmptyState() {
        if (_binding == null) return

        if (wishlistItems.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.wishlistContentLayout.visibility = View.GONE
        } else {
            binding.emptyStateLayout.visibility = View.GONE
            binding.wishlistContentLayout.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}