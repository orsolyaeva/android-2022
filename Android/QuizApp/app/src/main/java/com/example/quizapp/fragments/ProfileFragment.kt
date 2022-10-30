package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.databinding.FragmentProfileBinding
import com.example.quizapp.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userName : EditText
    private lateinit var highScore : TextView
    private lateinit var saveButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()

        userName.hint = userViewModel.getName()
        highScore.text = userViewModel.getHighScore().toString() + " points"
    }

    private fun initViews() {
        userName = binding.nameUser
        highScore = binding.highScore
        saveButton = binding.saveButton
    }

    private fun initListeners() {
        saveButton.setOnClickListener {
            userViewModel.setName(userName.text.toString())
            userName.hint = userName.text.toString()

            Snackbar.make(binding.root, "Changes saved", Snackbar.LENGTH_SHORT).show()

            userName.clearFocus()
        }
    }
}