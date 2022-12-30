package com.nyorsi.p3track.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nyorsi.p3track.R
import com.nyorsi.p3track.databinding.FragmentProfileBinding
import com.nyorsi.p3track.models.UserModel
import com.nyorsi.p3track.models.UserType

class ProfileFragment : Fragment() {
    private lateinit var signOutButton: Button
    private lateinit var profilePicture: ImageView
    private lateinit var userName: TextView
    private lateinit var userType: TextView
    private lateinit var userEmail: TextView
    private lateinit var userPhoneNumber: TextView
    private lateinit var officeLocation: TextView

    private lateinit var _binding: FragmentProfileBinding
    private lateinit var currentUser: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.findItem(R.id.add_new_task).isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })

        initializeViews()
        initializeListeners()

        val parsedValue = arguments?.getString("loggedInUser")
        currentUser = Gson().fromJson(parsedValue!!, object : TypeToken<UserModel>() {}.type)

        if(currentUser.image != null) {
            Glide.with(profilePicture.context)
                .load(currentUser.image)
                .into(profilePicture)
        } else {
            profilePicture.setImageResource(R.drawable.userprofile_placeholder)
        }

        userName.text = currentUser.firstName + " " + currentUser.lastName

        when(currentUser.type) {
            UserType.HR_MANAGER -> userType.text = "HR Manager"
            UserType.DEPARTMENT_LEAD -> userType.text = "Department Lead"
            UserType.SIMPLE_EMPLOYEE -> userType.text = "Simple Employee"
        }

        userEmail.text = currentUser.email
        userPhoneNumber.text = HtmlCompat.fromHtml("Phone number: <b>" + (currentUser.phoneNumber ?: "No phone number provided") + "</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        officeLocation.text = HtmlCompat.fromHtml("Office location: <b>" +  (currentUser.location ?: "No office location provided") + "</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ProfileFragment", "onCreateView")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return _binding.root
    }

    private fun initializeViews() {
        signOutButton = _binding.signOutButton
        profilePicture = _binding.profilePicture
        userName = _binding.profileUserName
        userType = _binding.profileUserType
        userEmail = _binding.profileUserEmail
        userPhoneNumber = _binding.profileUserPhone
        officeLocation = _binding.officeLocation
    }

    private fun initializeListeners() {
        signOutButton.setOnClickListener {
            val sharedPref =
                activity?.getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPref?.edit()

            editor?.remove("deadline")
            editor?.apply()

            editor?.remove("token")
            editor?.apply()

            editor?.remove("userID")
            editor?.apply()

            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }
}