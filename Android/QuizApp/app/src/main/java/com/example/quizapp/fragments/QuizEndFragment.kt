package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizEndBinding
import com.example.quizapp.viewModels.QuizViewModel
import com.example.quizapp.viewModels.UserViewModel


class QuizEndFragment : Fragment() {
    private lateinit var scoreText : TextView
    private lateinit var correctNumber: TextView
    private lateinit var incorrectNumber: TextView
    private lateinit var partiallyCorrectNumber: TextView
    private lateinit var tryAgainButton : Button

    private lateinit var binding: FragmentQuizEndBinding
    private lateinit var sharedPref: SharedPreferences
    private val viewModel: QuizViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    companion object {
        const val TAG = "QuizEndFragment"
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        if(viewModel.getScore() > sharedPref.getFloat("highScore", 0.0f)) {
            sharedPref.edit().putFloat("highScore", viewModel.getScore().toFloat()).apply()
        }
        userViewModel.setHighScore(viewModel.getScore())
        scoreText.text = "${viewModel.getScore()} / ${viewModel.getNumberOfQuestions()} points"
        correctNumber.text = "Correct answers: ${viewModel.getCorrectAnswers()}"
        incorrectNumber.text = "Incorrect answers: ${viewModel.getIncorrectAnswers()}"
        partiallyCorrectNumber.text = "Partially correct answers: ${viewModel.getPartiallyCorrectAnswers()}"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        binding = FragmentQuizEndBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initViews() {
        scoreText = binding.scoreText
        tryAgainButton = binding.tryAgainButton
        correctNumber = binding.correctNumber
        incorrectNumber = binding.wrongNumber
        partiallyCorrectNumber = binding.partiallyCorrectNumber
    }

    private fun initListeners() {
        tryAgainButton.setOnClickListener {
            userViewModel.setHighScore(viewModel.getScore())
            if(viewModel.getScore() > sharedPref.getFloat("highScore", 0.0f)) {
                sharedPref.edit().putFloat("highScore", viewModel.getScore().toFloat()).apply()
            }
            viewModel.resetQuiz()
            findNavController().navigate(R.id.action_quizEndFragment_to_quizStartFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback : OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_quizStartFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}