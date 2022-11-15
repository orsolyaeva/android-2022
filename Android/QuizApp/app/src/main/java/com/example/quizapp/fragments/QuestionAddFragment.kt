package com.example.quizapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.example.quizapp.databinding.FragmentQuestionAddBinding
import com.example.quizapp.models.Item
import com.example.quizapp.viewModels.QuizViewModel
import com.google.android.material.snackbar.Snackbar


class QuestionAddFragment : Fragment() {
    private lateinit var questionType: Spinner
    private lateinit var questionCategory: EditText
    private lateinit var question: EditText
    private lateinit var answerLayout: LinearLayout
    private lateinit var addQuestionButton: Button

    private lateinit var binding: FragmentQuestionAddBinding
    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()

        val questionTypes = mutableListOf("Single Choice", "Multiple Choice", "True or False")
        val questionTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            questionTypes
        )
        questionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        questionType.adapter = questionTypeAdapter

        generateAnswerLayout()
    }

    private fun generateAnswerLayout() {
        val currentQuestionType = questionType.selectedItem.toString()

        if(currentQuestionType == "Single Choice") {
            // add 4 answer options
            for(i in 0..3) {
                val answer = EditText(requireContext())

                if (i == 0) {
                    answer.hint = "Correct Answer"
                } else {
                    answer.hint = "Answer $i"
                }

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 30)
                answer.layoutParams = params
                answerLayout.addView(answer)
            }
        }

        if (currentQuestionType == "True or False") {
            // add 2 answer options
            for(i in 0..1) {
                val answer = EditText(requireContext())

                if (i == 0) {
                    answer.hint = "True"
                } else {
                    answer.hint = "False"
                }

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 30)
                answer.layoutParams = params
                answerLayout.addView(answer)
            }
        }

        if (currentQuestionType == "Multiple Choice") {
            for(i in 0..3) {
                val answer = EditText(requireContext())

                if (i == 0 || i == 1) {
                    answer.hint = "Correct Answer"
                } else {
                    answer.hint = "Answer ${i - 1}"
                }

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 30)
                answer.layoutParams = params
                answerLayout.addView(answer)
            }
        }
    }

    private fun initViews() {
        questionType = binding.selectType
        question = binding.questionInput
        questionCategory = binding.categoryInput
        addQuestionButton = binding.addQuestionButton
        answerLayout = binding.linearLayout
    }

    private fun initListeners() {
        addQuestionButton.setOnClickListener {
            val answers = mutableListOf<String>()

            if(questionCategory.text.isEmpty()) {
                Snackbar.make(requireView(), "Category cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(question.text.isEmpty()) {
                Snackbar.make(requireView(), "Question cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // check if all the answers are filled
            for(i in 0 until answerLayout.childCount) {
                val answer = answerLayout.getChildAt(i) as EditText

                if(answer.text.isEmpty()) {
                    Snackbar.make(requireView(), "All answers must be filled", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                answers.add(answer.text.toString())
            }

            var correct = mutableListOf<String>()

            if (questionType.selectedItem.toString() == "Multiple Choice") {
                correct = mutableListOf(answers[0], answers[1])
            } else {
                correct = mutableListOf(answers[0])
            }

            val item = Item(
                category = questionCategory.text.toString(),
                question = question.text.toString(),
                answers = answers,
                correct = correct,
                type = when (questionType.selectedItem.toString()) {
                    "Single Choice" -> 0
                    "Multiple Choice" -> 1
                    else -> 2
                }
            )

            viewModel.addQuestion(item)
            Snackbar.make(requireView(), "Question added successfully", Snackbar.LENGTH_SHORT).show()

            question.text.clear()
            questionCategory.text.clear()

            for(i in 0 until answerLayout.childCount) {
                val answer = answerLayout.getChildAt(i) as EditText
                answer.text.clear()
            }
        }

        questionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                answerLayout.removeAllViews()
                generateAnswerLayout()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }
        }
    }
}