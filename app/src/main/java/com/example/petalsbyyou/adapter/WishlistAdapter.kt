//package com.example.petalsbyyou.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.petalsbyyou.R
//import com.example.petalsbyyou.model.WishlistModel
//
//class WishlistAdapter(
//    private val context: Context,
//    private val wishlistItems: List<WishlistModel>,
//    private val onRemoveClick: (String) -> Unit
//) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {
//
//    class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val productName: TextView = itemView.findViewById(R.id.wishlistProductName)
//        val removeButton: TextView = itemView.findViewById(R.id.wishlistRemoveButton)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.item_wishlist, parent, false)
//        return WishlistViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
//        val wishlistItem = wishlistItems[position]
//        holder.productName.text = wishlistItem.productId // Replace with actual product name
//
//        holder.removeButton.setOnClickListener {
//            onRemoveClick(wishlistItem.wishlistId)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return wishlistItems.size
//    }
//}