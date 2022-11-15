package com.example.quizapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuestionDetailBinding
import com.example.quizapp.models.Item
import com.example.quizapp.viewModels.QuizViewModel

class QuestionDetailFragment : Fragment() {
    private var currentQuestion: Item? = null
    private lateinit var binding: FragmentQuestionDetailBinding
    private lateinit var questionText: TextView
    private lateinit var questionType: TextView
    private lateinit var answersGroup: LinearLayout
    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val id = it.getInt("id")
            currentQuestion = viewModel.getAllQuestions().value?.get(id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionDetailBinding.inflate(inflater, container, false)

        initViews()

        questionText.text = currentQuestion?.question
        Log.d("QuestionDetailFragment", "questionText = ${currentQuestion?.question}")
        when(currentQuestion?.type) {
            0 -> questionType.text = "Single choice"
            1 -> questionType.text = "Multiple choice"
            2 -> questionType.text = "True/False"
        }

        currentQuestion?.answers?.forEachIndexed() { index, answer ->
            val answerText = TextView(context)
            answerText.text = answer
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 30, 0, 0)
            if (currentQuestion?.correct?.contains(answer) == true) {
                answerText.text = "✓ " + answerText.text
                answerText.setTextColor(Color.GREEN)
            }
            answerText.layoutParams = params
            answersGroup.addView(answerText)
        }

        return binding.root
    }

    private fun initViews() {
        questionText = binding.textQuestion
        questionType = binding.typeQuestion
        answersGroup = binding.answerGroup
    }
}