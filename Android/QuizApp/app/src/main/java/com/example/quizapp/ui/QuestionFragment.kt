package com.example.quizapp.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuestionBinding
import com.example.quizapp.quiz.QuizViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [QuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionFragment : Fragment() {
    private lateinit var binding: FragmentQuestionBinding
    private lateinit var viewModel: QuizViewModel
    private lateinit var questionText : TextView
    private lateinit var nextButton : Button
    private lateinit var radioGroup : RadioGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("QuestionFragment", "onCreate: ")
        viewModel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]
        binding = FragmentQuestionBinding.inflate(layoutInflater)
        initViews()
        initListeners()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("QuestionFragment", "onCreateView: ")
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]

        viewModel.currentQuestion.observe(requireActivity()) { (question, isLast) ->
            binding.questionText.text = question?.question
            val numberAnswers = question?.answers?.size ?: 0

            radioGroup = RadioGroup(requireContext())
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            params.topToBottom = R.id.questionText
            params.startToStart = R.id.questionText
            params.endToEnd = R.id.questionText
            params.bottomToTop = R.id.nextQuestionButton
            radioGroup.layoutParams = params

            for (i in 0 until numberAnswers) {
                val answerButton = RadioButton(requireContext())
                answerButton.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                answerButton.text = question?.answers?.get(i)
                answerButton.id = i

                radioGroup.addView(answerButton)
            }

            binding.questionLayout.addView(radioGroup)

            if(isLast) {
                nextButton.text = "Submit"
                nextButton.setOnClickListener {
                    viewModel.checkAnswer(radioGroup.checkedRadioButtonId)
                    findNavController().navigate(R.id.action_questionFragment_to_quizEndFragment)
                }
            }
        }

        return binding.root
    }

    private fun initViews() {
        Log.d("QuestionFragment", "initViews: ")
        questionText = binding.questionText
        nextButton = binding.nextQuestionButton
    }

    private fun initListeners() {
        Log.d("QuestionFragment", "initListeners: ")
        nextButton.setOnClickListener {
            val selectedAnswer = radioGroup.checkedRadioButtonId

            if(selectedAnswer == -1) {
                return@setOnClickListener
            }

            viewModel.checkAnswer(selectedAnswer)
            binding.questionLayout.removeView(radioGroup)
            viewModel.getNextQuestion()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("QuestionFragment", "onAttach: ")
        val callback : OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Exit")
                builder.setMessage("Are you sure you want to end the quiz?")
                builder.setPositiveButton("Yes") { _, _ ->
                    viewModel.checkAnswer(radioGroup.checkedRadioButtonId)
                    findNavController().navigate(R.id.action_questionFragment_to_quizEndFragment)
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}