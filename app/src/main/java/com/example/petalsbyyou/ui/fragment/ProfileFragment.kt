package com.example.petalsbyyou.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petalsbyyou.databinding.FragmentProfileBinding
import com.example.petalsbyyou.model.UserModel
import com.example.petalsbyyou.repository.UserRepositoryImpl
import com.example.petalsbyyou.utils.LoadingUtils
import com.example.petalsbyyou.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingUtils: LoadingUtils
    private lateinit var currentUser: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)
        loadingUtils = LoadingUtils(requireContext())

        // Fetch current user details
        fetchCurrentUserDetails()

        // Set up Save Button
        binding.profileSaveButton.setOnClickListener {
            updateUserProfile()
        }

        // Set up Delete Button
        binding.profileDeleteButton.setOnClickListener {
            deleteUserProfile()
        }
    }

    private fun fetchCurrentUserDetails() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            loadingUtils.show()
            userViewModel.getCurrentUserDetails(userId) { userModel ->
                loadingUtils.dismiss()
                if (userModel != null) {
                    currentUser = userModel
                    populateUserDetails(userModel)
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateUserDetails(userModel: UserModel) {
        binding.profileFirstName.setText(userModel.firstName)
        binding.profileLastName.setText(userModel.lastName)
        binding.profileEmail.setText(userModel.email)
        binding.profileAddress.setText(userModel.address)
        binding.profilePhone.setText(userModel.phoneNumber)
    }

    private fun updateUserProfile() {
        val firstName = binding.profileFirstName.text.toString().trim()
        val lastName = binding.profileLastName.text.toString().trim()
        val address = binding.profileAddress.text.toString().trim()
        val phone = binding.profilePhone.text.toString().trim()

        if (validateInputs(firstName, lastName, address, phone)) {
            loadingUtils.show()
            val updatedUser = currentUser.copy(
                firstName = firstName,
                lastName = lastName,
                address = address,
                phoneNumber = phone
            )

            userViewModel.updateUserProfile(updatedUser) { success, message ->
                loadingUtils.dismiss()
                if (success) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteUserProfile() {
        loadingUtils.show()
        userViewModel.deleteUserProfile { success, message ->
            loadingUtils.dismiss()
            if (success) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                // Navigate to login or another screen after deletion
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(firstName: String, lastName: String, address: String, phone: String): Boolean {
        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.length != 10 || !phone.matches("\\d+".toRegex())) {
            Toast.makeText(requireContext(), "Phone number must be 10 digits", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}