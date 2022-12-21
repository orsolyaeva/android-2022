package com.nyorsi.p3track.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.nyorsi.p3track.R
import com.nyorsi.p3track.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var signOutButton: Button

    private lateinit var _binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signOutButton = _binding.signOutButton

        signOutButton.setOnClickListener {
            // delete deadline from shared preferences
            // navigate to login fragment
            val sharedPref =
                activity?.getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPref?.edit()

            editor?.remove("deadline")
            editor?.apply()

            editor?.remove("token")
            editor?.apply()

            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ProfileFragment", "onCreateView")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return _binding.root
    }
}