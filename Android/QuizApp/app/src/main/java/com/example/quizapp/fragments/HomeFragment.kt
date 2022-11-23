package com.example.quizapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.TAG
import com.example.quizapp.databinding.FragmentHomeBinding
import com.example.quizapp.models.Item
import com.example.quizapp.models.QuestionDifficulty
import com.example.quizapp.models.QuestionType
import com.example.quizapp.repositories.ItemRepository
import com.example.quizapp.services.RetrofitService
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var startQuizButton: Button
    private lateinit var readQuestionsButton: Button
    private lateinit var addQuestionButton: Button

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
        addQuestionButton = binding.createQuestionButton
    }

    private fun initListeners() {
        startQuizButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_quizStartFragment)
        }

        readQuestionsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_questionListFragment)
        }

        addQuestionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_questionAddFragment)
        }
    }
}