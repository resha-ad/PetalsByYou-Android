package com.example.petalsbyyou.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.petalsbyyou.adapter.ProductAdapter
import com.example.petalsbyyou.databinding.FragmentHomeBinding
import com.example.petalsbyyou.model.ProductModel
import com.example.petalsbyyou.R
import com.example.petalsbyyou.model.CartModel
import com.example.petalsbyyou.repository.CartRepositoryImpl
import com.example.petalsbyyou.repository.UserRepositoryImpl
import com.example.petalsbyyou.repository.CartRepository

class HomeFragment : Fragment(), ProductAdapter.ProductClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private val cartRepository = CartRepositoryImpl() // Instance of CartRepositoryImpl
    private val userRepository = UserRepositoryImpl() // Instance of UserRepositoryImpl

    // Current user ID (fetched dynamically)
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch the current user ID
        userId = userRepository.getCurrentUser()?.uid

        setupRecyclerView()
        loadProducts()

        // Handle refresh action
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadProducts()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(requireContext())
        productAdapter.setOnProductClickListener(this)

        binding.recyclerViewProducts.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }

    private fun loadProducts() {
        // Sample flower data with names, meanings, and drawable images
        val products = arrayListOf(
            ProductModel(
                "1",
                "Rose Bouquet",
                "Love, passion, romance",
                29.99,
                R.drawable.roses // Replace with your actual drawable name
            ),
            ProductModel(
                "2",
                "Tulip Collection",
                "Declaration of love, perfect love",
                24.99,
                R.drawable.tulips
            ),
            ProductModel(
                "3",
                "Orchid Arrangement",
                "Beauty, luxury, refinement",
                39.99,
                R.drawable.orchids
            ),
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

    // Update adapter with new data
        productAdapter.updateProducts(products)

        // Show/hide empty state
        if (products.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.recyclerViewProducts.visibility = View.GONE
        } else {
            binding.emptyStateLayout.visibility = View.GONE
            binding.recyclerViewProducts.visibility = View.VISIBLE
        }
    }

    override fun onAddToCartClicked(product: ProductModel, position: Int) {
        // Ensure the user is logged in
        if (userId == null) {
            Toast.makeText(requireContext(), "Please log in to add items to the cart", Toast.LENGTH_SHORT).show()
            return
        }

        val cartModel = CartModel(
            userId = userId!!, // Use the dynamically fetched userId
            productId = product.productId,
            quantity = 1,
            price = product.price
        )

        cartRepository.addToCart(cartModel) { success, message ->
            if (success) {
                Toast.makeText(requireContext(), "${product.productName} added to cart", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to add to cart: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onWishlistClicked(product: ProductModel, position: Int) {
        Toast.makeText(
            requireContext(),
            "${product.productName} added to wishlist",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onProductClicked(product: ProductModel, position: Int) {
        Toast.makeText(
            requireContext(),
            "Viewing ${product.productName}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}