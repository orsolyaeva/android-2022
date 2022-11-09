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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizEndBinding
import com.example.quizapp.viewModels.QuizViewModel
import com.example.quizapp.viewModels.UserViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [QuizEndFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class QuizEndFragment : Fragment() {

    private lateinit var binding: FragmentQuizEndBinding
    private lateinit var viewModel: QuizViewModel
    private lateinit var scoreText : TextView
    private lateinit var tryAgainButton : Button
    private lateinit var correctNumber: TextView
    private lateinit var incorrectNumber: TextView
    private lateinit var partiallyCorrectNumber: TextView
    private lateinit var sharedPref: SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        sharedPref.edit().putFloat("highScore", viewModel.getScore().toFloat()).apply()
        scoreText.text = "${viewModel.getScore()} / ${viewModel.getNumberOfQuestions()} points"
        correctNumber.text = "Correct answers: ${viewModel.getCorrectAnswers()}"
        incorrectNumber.text = "Incorrect answers: ${viewModel.getIncorrectAnswers()}"
        partiallyCorrectNumber.text = "Partially correct answers: ${viewModel.getPartiallyCorrectAnswers()}"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]
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
            sharedPref.edit().putFloat("highScore", viewModel.getScore().toFloat()).apply()
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