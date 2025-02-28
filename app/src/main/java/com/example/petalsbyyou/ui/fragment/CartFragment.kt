package com.example.petalsbyyou.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petalsbyyou.adapter.CartAdapter
import com.example.petalsbyyou.databinding.FragmentCartBinding
import com.example.petalsbyyou.model.CartModel
import com.example.petalsbyyou.model.ProductModel
import com.example.petalsbyyou.repository.CartRepositoryImpl
import com.example.petalsbyyou.repository.UserRepositoryImpl
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter
    private val cartRepository = CartRepositoryImpl()
    private val userRepository = UserRepositoryImpl() // Add UserRepositoryImpl
    private val cartItems = mutableListOf<CartModel>()
    private val productMap = mutableMapOf<String, ProductModel>()

    // Dynamically fetch userId
    private val userId: String?
        get() = userRepository.getCurrentUser()?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if the user is logged in
        if (userId == null) {
            Toast.makeText(requireContext(), "Please log in to view your cart", Toast.LENGTH_SHORT).show()
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.cartContentLayout.visibility = View.GONE
            return
        }

        setupRecyclerView()
        loadCartItems()

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadCartItems()
        }

        binding.btnCheckout.setOnClickListener {
            if (cartItems.isNotEmpty()) {
                checkoutOrder()
            } else {
                Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            requireContext(),
            cartItems,
            productMap,
            onRemoveClick = { cartId ->
                removeCartItem(cartId)
            },
            onQuantityChange = { cartId, quantity ->
                updateCartItemQuantity(cartId, quantity)
            }
        )

        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
    }

    private fun loadCartItems() {
        binding.swipeRefreshLayout.isRefreshing = true

        // Ensure userId is not null
        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            binding.swipeRefreshLayout.isRefreshing = false
            return
        }

        cartRepository.getCartItems(userId!!) { items, success, message ->
            // Ensure binding is not null
            _binding?.swipeRefreshLayout?.isRefreshing = false

            if (success && items != null) {
                cartItems.clear()
                cartItems.addAll(items)
                cartAdapter.notifyDataSetChanged()
                updateCartSummary()
                toggleEmptyState()
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeCartItem(cartId: String) {
        cartRepository.removeFromCart(cartId) { success, message ->
            if (success) {
                val position = cartItems.indexOfFirst { it.cartId == cartId }
                if (position != -1) {
                    cartItems.removeAt(position)
                    cartAdapter.notifyItemRemoved(position)
                    updateCartSummary()
                    toggleEmptyState()
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCartItemQuantity(cartId: String, quantity: Int) {
        if (quantity <= 0) {
            removeCartItem(cartId)
            return
        }

        cartRepository.updateCartItem(cartId, quantity) { success, message ->
            if (success) {
                val position = cartItems.indexOfFirst { it.cartId == cartId }
                if (position != -1) {
                    cartItems[position].quantity = quantity
                    cartAdapter.notifyItemChanged(position)
                    updateCartSummary()
                }
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCartSummary() {
        var subtotal = 0.0
        var totalItems = 0

        for (item in cartItems) {
            subtotal += item.price * item.quantity
            totalItems += item.quantity
        }

        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        binding.textSubtotal.text = formatter.format(subtotal)
        binding.textShipping.text = if (subtotal > 0) formatter.format(5.99) else formatter.format(0)
        binding.textTotal.text = if (subtotal > 0) formatter.format(subtotal + 5.99) else formatter.format(0)
        binding.textItemCount.text = "Items ($totalItems)"
    }

    private fun toggleEmptyState() {
        if (cartItems.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.cartContentLayout.visibility = View.GONE
        } else {
            binding.emptyStateLayout.visibility = View.GONE
            binding.cartContentLayout.visibility = View.VISIBLE
        }
    }

    private fun checkoutOrder() {
        // Simulate order processing
        Toast.makeText(requireContext(), "Order placed successfully! Payment method: COD", Toast.LENGTH_LONG).show()
        // Clear the cart after successful order
        cartItems.clear()
        cartAdapter.notifyDataSetChanged()
        updateCartSummary()
        toggleEmptyState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}