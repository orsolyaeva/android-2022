package com.nyorsi.p3track.ui.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nyorsi.p3track.R
import com.nyorsi.p3track.databinding.FragmentLoginBinding
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.LoginViewModel
import com.nyorsi.p3track.viewModels.UserViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var hidePassword: ImageView
    private lateinit var signInButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        initializeListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        // hide bottom navigation bar from login fragment
        (activity as AppCompatActivity?)!!.findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        // hide the action bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    private fun initializeViews() {
        email = binding.editTextEmailAddress
        password = binding.editTextPassword
        signInButton = binding.signInButton
        hidePassword = binding.hidePasswordImageView
    }

    private fun initializeListeners() {
        signInButton.setOnClickListener {
            loginViewModel.login(email.text.toString(), password.text.toString())
            loginViewModel.requestState.observe(viewLifecycleOwner) {
                if (it == RequestState.LOADING) {
                    return@observe
                }
                Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
                if (it == RequestState.SUCCESS) {
                    findNavController().navigate(R.id.action_loginFragment_to_actvitiesFragment)
                } else {
                    Toast.makeText(activity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        hidePassword.setOnClickListener {
            if (password.transformationMethod == null) {
                password.transformationMethod = PasswordTransformationMethod()
                hidePassword.setImageResource(R.drawable.show_password)
            } else {
                password.transformationMethod = null
                 hidePassword.setImageResource(R.drawable.hide_password)
            }
        }

        email.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                email.setBackgroundResource(R.drawable.edit_text_focused)
            } else {
                email.setBackgroundResource(R.drawable.rounded_edit_text)
            }
        }

        password.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                password.setBackgroundResource(R.drawable.edit_text_focused)
            } else {
                password.setBackgroundResource(R.drawable.rounded_edit_text)
            }
        }
    }
}