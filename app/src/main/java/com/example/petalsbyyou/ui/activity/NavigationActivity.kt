package com.example.petalsbyyou.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.petalsbyyou.R
import com.example.petalsbyyou.databinding.ActivityNavigationBinding
import com.example.petalsbyyou.ui.fragment.CartFragment
import com.example.petalsbyyou.ui.fragment.HomeFragment
import com.example.petalsbyyou.ui.fragment.ProfileFragment
import com.example.petalsbyyou.ui.fragment.WishlistFragment
import com.google.firebase.auth.FirebaseAuth

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the default fragment to HomeFragment
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // Set up bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            try {
                when (menuItem.itemId) {
                    R.id.menuHome -> replaceFragment(HomeFragment())
                    R.id.menuWishlist -> replaceFragment(WishlistFragment())
                    R.id.menuCart -> replaceFragment(CartFragment())
                    R.id.menuProfile -> {
                        // Check if the user is logged in before navigating to the ProfileFragment
                        if (FirebaseAuth.getInstance().currentUser != null) {
                            replaceFragment(ProfileFragment())
                        } else {
                            Toast.makeText(this, "Please log in to view your profile", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> return@setOnItemSelectedListener false
                }
                true
            } catch (e: Exception) {
                // Show a toast message if an error occurs
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        try {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.frameLayout, fragment)
                .commit()
        } catch (e: Exception) {
            // Show a toast message if fragment transaction fails
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}