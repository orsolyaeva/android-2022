package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuestionBinding
import com.example.quizapp.models.Item
import com.example.quizapp.models.QuestionType
import com.example.quizapp.viewModels.QuizViewModel


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
    private lateinit var spinner : Spinner
    private var userAnswers = mutableListOf<Int>()

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

            when(question?.type) {
                QuestionType.SPINNER.ordinal -> {
                    handleSpinner(question)
                }
                else -> {
                    handleSingleMultipleChoice(question)
                }
            }

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

    private fun handleSpinner(question: Item?) {
        spinner = Spinner(requireContext())
        val spinnerParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        spinnerParams.bottomToTop = R.id.nextQuestionButton
        spinnerParams.topToBottom = R.id.questionText
        spinnerParams.setMargins(30, 0, 30, 200)
        spinner.layoutParams = spinnerParams
        val answers : MutableList<String> = question?.answers?.toMutableList() ?: mutableListOf()
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, answers)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        binding.questionLayout.addView(spinner)
    }

    private fun handleSingleMultipleChoice(question: Item?) {
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

        if (question?.type == QuestionType.SINGLE_CHOICE.ordinal) {
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
        Log.d("QuestionFragment", "initViews: ")
        questionText = binding.questionText
        nextButton = binding.nextQuestionButton
    }

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
            QuestionType.SPINNER.ordinal -> {
                userAnswers.add(spinner.selectedItemPosition)
                return true
            }
            else -> {}
        }
        return true
    }

    private fun initListeners() {
        Log.d("QuestionFragment", "initListeners: ")
        nextButton.setOnClickListener {
            if(viewModel.currentQuestion.value?.first?.type?.let { it1 -> collectAnswers(it1) } == false) {
                return@setOnClickListener
            }

            viewModel.checkAnswer(userAnswers)
            if (viewModel.currentQuestion.value?.first?.type == QuestionType.SPINNER.ordinal) {
                binding.questionLayout.removeView(spinner)
            } else {
                binding.questionLayout.removeView(radioGroup)
            }

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
                    collectAnswers(viewModel.currentQuestion.value?.first?.type ?: 0)
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