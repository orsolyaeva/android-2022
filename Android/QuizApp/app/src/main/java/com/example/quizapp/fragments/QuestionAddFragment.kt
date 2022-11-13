package com.example.quizapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.quizapp.databinding.FragmentQuestionAddBinding
import com.example.quizapp.models.Item
import com.example.quizapp.viewModels.QuizViewModel
import com.google.android.material.snackbar.Snackbar


class QuestionAddFragment : Fragment() {
    private lateinit var binding: FragmentQuestionAddBinding
    private lateinit var question: TextView
    private lateinit var answer1: TextView
    private lateinit var answer2: TextView
    private lateinit var answer3: TextView
    private lateinit var answer4: TextView
    private lateinit var addQuestionButton: Button
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
    }

    private fun initViews() {
        question = binding.questionInput
        answer1 = binding.answerInput1
        answer2 = binding.answerInput2
        answer3 = binding.answerInput3
        answer4 = binding.answerInput4
        addQuestionButton = binding.addQuestionButton
    }

    private fun initListeners() {
        addQuestionButton.setOnClickListener {
            val answers = mutableListOf<String>()

            if(question.text.isEmpty()) {
                Snackbar.make(requireView(), "Question cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (answer1.text.isEmpty() || answer2.text.isEmpty() || answer3.text.isEmpty() || answer4.text.isEmpty()) {
                Snackbar.make(requireView(), "Please fill in all the answers", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            answers.add(answer1.text.toString())
            answers.add(answer2.text.toString())
            answers.add(answer3.text.toString())
            answers.add(answer4.text.toString())

            val item: Item = Item(
                question = question.text.toString(),
                answers = answers,
                correct = mutableListOf(answers[0]),
                type = 0
            )

            viewModel.addQuestion(item)
            Snackbar.make(requireView(), "Question added successfully", Snackbar.LENGTH_SHORT).show()

            question.text = ""
            answer1.text = ""
            answer2.text = ""
            answer3.text = ""
            answer4.text = ""
        }
    }
}