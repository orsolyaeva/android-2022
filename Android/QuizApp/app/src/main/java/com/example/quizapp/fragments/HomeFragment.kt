package com.example.quizapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var startQuizButton: Button
    private lateinit var readQuestionsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initViews() {
        startQuizButton = binding.quizStartButton
        readQuestionsButton = binding.readQuestionsButton
    }

    private fun initListeners() {
        startQuizButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_quizStartFragment)
        }

        readQuestionsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_questionListFragment)
        }
    }
}