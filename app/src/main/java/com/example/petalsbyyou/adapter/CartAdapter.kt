package com.example.petalsbyyou.adapter//package com.example.petalsbyyou.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.petalsbyyou.R
//import com.example.petalsbyyou.model.CartModel
//
//class CartAdapter(
//    private val context: Context,
//    private val cartItems: List<CartModel>,
//    private val onRemoveClick: (String) -> Unit,
//    private val onQuantityChange: (String, Int) -> Unit
//) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//
//    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val productName: TextView = itemView.findViewById(R.id.cartProductName)
//        val productPrice: TextView = itemView.findViewById(R.id.cartProductPrice)
//        val productQuantity: TextView = itemView.findViewById(R.id.cartProductQuantity)
//        val removeButton: TextView = itemView.findViewById(R.id.cartRemoveButton)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
//        return CartViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        val cartItem = cartItems[position]
//        holder.productName.text = cartItem.productId // Replace with actual product name
//        holder.productPrice.text = "$${cartItem.price}"
//        holder.productQuantity.text = cartItem.quantity.toString()
//
//        holder.removeButton.setOnClickListener {
//            onRemoveClick(cartItem.cartId)
//        }
//
//        // Add logic for quantity change (e.g., buttons to increase/decrease quantity)
//    }
//
//    override fun getItemCount(): Int {
//        return cartItems.size
//    }
//}