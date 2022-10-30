package com.example.quizapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.databinding.FragmentQuestionDetailBinding
import com.example.quizapp.models.Item
import com.example.quizapp.viewModels.QuizViewModel


class QuestionDetailFragment : Fragment() {
    private var currentQuestion: Item? = null
    private lateinit var binding: FragmentQuestionDetailBinding
    private lateinit var questionText: TextView
    private lateinit var questionType: TextView
    private lateinit var answersGroup: LinearLayout
    private lateinit var viewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            val id = it?.getInt("id")
            currentQuestion = viewModel.getAllQuestions().value?.get(id!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]

        initViews()

//        questionText.text = currentQuestion?.question
//        when(currentQuestion?.type) {
//            0 -> questionType.text = "Single choice"
//            1 -> questionType.text = "Multiple choice"
//            2 -> questionType.text = "Text answer"
//        }
//
//        currentQuestion?.answers?.forEachIndexed() { index, answer ->
//            val answerText = TextView(context)
//            answerText.text = answer
//            answersGroup.addView(answerText)
//        }

        return binding.root
    }

    private fun initViews() {
        questionText = binding.textQuestion
        questionType = binding.typeQuestion
        answersGroup = binding.answerGroup
    }

    companion object {
        @JvmStatic
        fun newInstance(question: Int) =
            QuestionDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", question)
                }
            }
    }
}