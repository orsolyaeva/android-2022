package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.quizapp.databinding.FragmentQuestionDetailBinding
import com.example.quizapp.models.Item
import com.example.quizapp.models.QuestionDifficulty
import com.example.quizapp.viewModels.QuizViewModel

class QuestionDetailFragment : Fragment() {
    private var currentQuestion: Item? = null
    private lateinit var questionText: TextView
    private lateinit var questionCategory: TextView
    private lateinit var questionDifficulty: TextView
    private lateinit var questionType: TextView
    private lateinit var answersGroup: LinearLayout

    private lateinit var binding: FragmentQuestionDetailBinding
    private val viewModel: QuizViewModel by activityViewModels()

    companion object {
        const val TAG = "QuestionDetailFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the current question from the view model
        arguments?.let {
            val id = it.getInt("id")
            currentQuestion = viewModel.getAllQuestions().value?.get(id)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionDetailBinding.inflate(inflater, container, false)

        initViews()

        questionText.text = currentQuestion?.question
        Log.d(TAG, "questionText = ${currentQuestion?.question}")
        when(currentQuestion?.type) {
            0 -> questionType.text = "Single choice"
            1 -> questionType.text = "Multiple choice"
            2 -> questionType.text = "True/False"
        }

        when(currentQuestion?.difficulty) {
            QuestionDifficulty.EASY -> {
                questionDifficulty.text = "Easy"
                questionDifficulty.setTextColor(Color.GREEN)
            }
            QuestionDifficulty.MEDIUM -> {
                questionDifficulty.text = "Medium"
                questionDifficulty.setTextColor(Color.parseColor("#FFA500"))
            }
            QuestionDifficulty.HARD -> {
                questionDifficulty.text = "Hard"
                questionDifficulty.setTextColor(Color.RED)
            }
            else -> {
                questionDifficulty.text = "Not defined"
                questionDifficulty.setTextColor(Color.GRAY)
            }
        }

        val category = currentQuestion?.category
        val categoryText = "Category: $category"
        val spannableString = SpannableString(categoryText)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 10, categoryText.length, 0)
        questionCategory.text = spannableString

        currentQuestion?.answers?.forEachIndexed { _, answer ->
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
        questionCategory = binding.categoryQuestion
        questionDifficulty = binding.difficultyQuestion
    }
}