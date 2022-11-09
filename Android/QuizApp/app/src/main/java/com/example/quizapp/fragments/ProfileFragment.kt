package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.databinding.FragmentProfileBinding
import com.example.quizapp.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userName : EditText
    private lateinit var highScore : TextView
    private lateinit var saveButton : Button
    private lateinit var profilePicture: ImageView
    private lateinit var selectPhotoButton: Button
    private lateinit var sharedPref : SharedPreferences

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { it ->
            profilePicture.setImageURI(it)
            sharedPref.edit().putString("profilePicture", it.toString()).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()

        if (sharedPref.contains("profilePicture")) {
            profilePicture.setImageURI(Uri.parse(sharedPref.getString("profilePicture", "")))
        }

        userName.hint = sharedPref.getString("username", "Enter your name")
        highScore.text = sharedPref.getFloat("highScore", 0.0F).toString()
    }

    private fun initViews() {
        userName = binding.nameUser
        highScore = binding.highScore
        saveButton = binding.saveButton
        profilePicture = binding.profilePicture
        selectPhotoButton = binding.choosePhotoButton
    }

    private fun initListeners() {
        saveButton.setOnClickListener {
            sharedPref.edit().putString("username", userName.text.toString()).apply()
            userName.hint = userName.text.toString()

            Snackbar.make(binding.root, "Changes saved", Snackbar.LENGTH_SHORT).show()

            userName.clearFocus()
        }

        selectPhotoButton.setOnClickListener {
            getImage.launch("image/*")
        }
    }

}