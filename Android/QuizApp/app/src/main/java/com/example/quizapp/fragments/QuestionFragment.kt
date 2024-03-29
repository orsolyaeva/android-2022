package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuestionBinding
import com.example.quizapp.models.Item
import com.example.quizapp.models.QuestionType
import com.example.quizapp.viewModels.QuizViewModel


class QuestionFragment : Fragment() {
    private lateinit var questionText : TextView
    private lateinit var nextButton : Button
    private lateinit var radioGroup : RadioGroup
    private var userAnswers = mutableListOf<Int>()

    private lateinit var binding: FragmentQuestionBinding
    private lateinit var sharedPref : SharedPreferences
    private val viewModel: QuizViewModel by activityViewModels()

    companion object {
        const val TAG = "QuestionFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        binding = FragmentQuestionBinding.inflate(layoutInflater)
        initViews()
        initListeners()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: ")
        binding = FragmentQuestionBinding.inflate(inflater, container, false)

        // observe the current question and update the UI accordingly
        viewModel.currentQuestion.observe(requireActivity()) { (question, isLast) ->
            binding.questionText.text = question?.question

            handleQuestion(question)

            if(isLast) {
                nextButton.text = "Submit"
                nextButton.setOnClickListener {
                    question?.type?.let { it1 -> collectAnswers(it1) }
                    viewModel.checkAnswer(userAnswers)
                    findNavController().navigate(R.id.action_questionFragment_to_quizEndFragment)
                }
            }
        }

        return binding.root
    }

    private fun handleQuestion(question: Item?) {
        val numberOptions = question?.answers?.size ?: 0

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

        // check question type and add answer groups accordingly
        if (question?.type == QuestionType.SINGLE_CHOICE.ordinal || question?.type == QuestionType.TRUE_FALSE.ordinal) {
            for (i in 0 until numberOptions) {
                val radioButton = RadioButton(requireContext())
                radioButton.text = question.answers[i]
                radioButton.setPadding(30, 30, 30, 30)
                radioButton.id = i
                radioGroup.addView(radioButton)
            }
        } else {
            for (i in 0 until numberOptions) {
                val checkBox = CheckBox(requireContext())
                checkBox.text = question?.answers?.get(i)
                checkBox.setPadding(30, 30, 30, 30)
                checkBox.id = i
                radioGroup.addView(checkBox)
            }
        }

        binding.questionLayout.addView(radioGroup)
    }

    private fun initViews() {
        Log.d(TAG, "initViews: ")
        questionText = binding.questionText
        nextButton = binding.nextQuestionButton
    }

    // collect the user's answers and add them to the list
    private fun collectAnswers(questionType: Int): Boolean {
        userAnswers.clear()
        when(questionType) {
            QuestionType.SINGLE_CHOICE.ordinal -> {
                val selectedAnswer = radioGroup.checkedRadioButtonId
                if (selectedAnswer == -1) {
                    return false
                }

                userAnswers.add(radioGroup.checkedRadioButtonId)
                return true
            }
            QuestionType.MULTIPLE_CHOICE.ordinal -> {
                for (i in 0 until radioGroup.childCount) {
                    val checkBox = radioGroup.getChildAt(i) as CheckBox
                    if (checkBox.isChecked) {
                        userAnswers.add(checkBox.id)
                    }
                }

                return userAnswers.isNotEmpty()
            }
            QuestionType.TRUE_FALSE.ordinal -> {
                val selectedAnswer = radioGroup.checkedRadioButtonId
                if (selectedAnswer == -1) {
                    return false
                }

                userAnswers.add(radioGroup.checkedRadioButtonId)
                return true
            }
            else -> {}
        }
        return true
    }

    private fun initListeners() {
        Log.d(TAG, "initListeners: ")
        nextButton.setOnClickListener {
            if(viewModel.currentQuestion.value?.first?.type?.let { it1 -> collectAnswers(it1) } == false) {
                return@setOnClickListener
            }

            viewModel.checkAnswer(userAnswers)
            binding.questionLayout.removeView(radioGroup)

            viewModel.getNextQuestion()
        }
    }

    // handle the back button press to prevent the user from going back to the previous question
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
        val callback : OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Exit")
                builder.setMessage("Are you sure you want to end the quiz?")
                builder.setPositiveButton("Yes") { _, _ ->
                    collectAnswers(viewModel.currentQuestion.value?.first?.type ?: 0)
                    Log.d(TAG, "handleOnBackPressed: $userAnswers")
                    viewModel.checkAnswer(userAnswers)
                    findNavController().navigate(R.id.action_questionFragment_to_quizEndFragment)
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}