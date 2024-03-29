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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
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

    companion object {
        const val TAG = "QuestionAddFragment"
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

        // create spinner to select question type
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

    @SuppressLint("SetTextI18n")
    private fun generateAnswerLayout() {
        // get current selected question type
        val currentQuestionType = questionType.selectedItem.toString()

        if(currentQuestionType == "Single Choice") {
            // add 4 answer options and a checkboxes to mark the correct answer
            for(i in 0..3) {
                val answer = EditText(requireContext())
                answer.hint = "Answer $i"
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                answer.layoutParams = params
                answerLayout.addView(answer)

                // add checkbox for the correct answer
                val correctAnswerCheckbox = CheckBox(requireContext())
                correctAnswerCheckbox.text = "Correct answer"
                correctAnswerCheckbox.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                answerLayout.addView(correctAnswerCheckbox)
            }

            // if one of the answers is checked, unable the others
            for(i in 0 until answerLayout.childCount) {
                if(answerLayout.getChildAt(i) is CheckBox) {
                    val checkBox = answerLayout.getChildAt(i) as CheckBox
                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        if(isChecked) {
                            for(j in 0 until answerLayout.childCount) {
                                if(answerLayout.getChildAt(j) is CheckBox) {
                                    val otherCheckBox = answerLayout.getChildAt(j) as CheckBox
                                    if(otherCheckBox != checkBox) {
                                        otherCheckBox.isEnabled = false
                                    }
                                }
                            }
                        }
                        // if the checkbox is unchecked, enable the other
                        else {
                            for(j in 0 until answerLayout.childCount) {
                                if(answerLayout.getChildAt(j) is CheckBox) {
                                    val otherCheckBox = answerLayout.getChildAt(j) as CheckBox
                                    if(otherCheckBox != checkBox) {
                                        otherCheckBox.isEnabled = true
                                    }
                                }
                            }
                        }
                    }
                }
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

                // add checkbox for the correct answer
                val correctAnswer = CheckBox(requireContext())
                correctAnswer.text = "Correct Answer"
                correctAnswer.layoutParams = params
                answerLayout.addView(correctAnswer)
            }

            // if one of the answers is checked, unable the other
            for(i in 0 until answerLayout.childCount) {
                if(answerLayout.getChildAt(i) is CheckBox) {
                    val checkBox = answerLayout.getChildAt(i) as CheckBox
                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        if(isChecked) {
                            for(j in 0 until answerLayout.childCount) {
                                if(answerLayout.getChildAt(j) is CheckBox) {
                                    val otherCheckBox = answerLayout.getChildAt(j) as CheckBox
                                    if(otherCheckBox != checkBox) {
                                        otherCheckBox.isEnabled = false
                                    }
                                }
                            }
                        }
                        // if the checkbox is unchecked, enable the other
                        else {
                            for(j in 0 until answerLayout.childCount) {
                                if(answerLayout.getChildAt(j) is CheckBox) {
                                    val otherCheckBox = answerLayout.getChildAt(j) as CheckBox
                                    if(otherCheckBox != checkBox) {
                                        otherCheckBox.isEnabled = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (currentQuestionType == "Multiple Choice") {
            for(i in 1..4) {
                val answerOption = EditText(requireContext())
                answerOption.hint = "Answer $i"
                answerOption.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                answerLayout.addView(answerOption)

                val correctAnswerCheckbox = CheckBox(requireContext())
                correctAnswerCheckbox.text = "Correct answer"
                correctAnswerCheckbox.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                answerLayout.addView(correctAnswerCheckbox)
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

            // check if the question category is empty
            if(questionCategory.text.isEmpty()) {
                Snackbar.make(requireView(), "Category cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // check if the question is empty
            if(question.text.isEmpty()) {
                Snackbar.make(requireView(), "Question cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // check if all the answers are filled
            for(i in 0 until answerLayout.childCount) {
                // check if child is an EditText thereby checking if it is an answer
                if(answerLayout.getChildAt(i) is EditText) {
                    val answer = answerLayout.getChildAt(i) as EditText

                    if(answer.text.isEmpty()) {
                        Snackbar.make(requireView(), "All answers must be filled", Snackbar.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    answers.add(answer.text.toString())
                }
            }

            Log.d(TAG, "Answers: $answers")

            var isCorrectAnswerChecked = false
            val correct = mutableListOf<String>()

            for(i in 0 until answerLayout.childCount) {
                // check if given child is a checkbox
                if(answerLayout.getChildAt(i) is CheckBox) {
                    val checkbox = answerLayout.getChildAt(i) as CheckBox

                    // if is checked, add the answer to the correct list
                    if(checkbox.isChecked) {
                        correct.add(answers[(i-1)/2])
                        isCorrectAnswerChecked = true

                        if(questionType.selectedItem.toString() == "Single Choice" ||
                                questionType.selectedItem.toString() == "True or False") {
                            break
                        }
                    }
                }
            }

            if(!isCorrectAnswerChecked) {
                Snackbar.make(requireView(), "At least one correct answer must be checked", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
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

            // clear the answer layout
            for(i in 0 until answerLayout.childCount) {
                if(answerLayout.getChildAt(i) is EditText) {
                    val answer = answerLayout.getChildAt(i) as EditText
                    answer.text.clear()
                }

                if(answerLayout.getChildAt(i) is CheckBox) {
                    val checkbox = answerLayout.getChildAt(i) as CheckBox
                    checkbox.isChecked = false
                }

               if(questionType.selectedItem.toString() == "Multiple Choice") {
                   if(answerLayout.getChildAt(i) is CheckBox) {
                       val checkbox = answerLayout.getChildAt(i) as CheckBox
                       checkbox.isChecked = false
                   }
               }
            }
        }

        // add listener to the question type spinner to change the layout of the answers
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

    // handle the back button press
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
        val callback : OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Exit")
                builder.setMessage("Are you sure you want to exit? You will lose all the data you entered.")
                builder.setPositiveButton("Yes") { _, _ ->

                    for(i in 0 until answerLayout.childCount) {
                        if(answerLayout.getChildAt(i) is EditText) {
                            val answer = answerLayout.getChildAt(i) as EditText
                            answer.text.clear()
                        }

                        if(answerLayout.getChildAt(i) is CheckBox) {
                            val checkbox = answerLayout.getChildAt(i) as CheckBox
                            checkbox.isChecked = false
                        }

                        if(questionType.selectedItem.toString() == "Multiple Choice") {
                            if(answerLayout.getChildAt(i) is CheckBox) {
                                val checkbox = answerLayout.getChildAt(i) as CheckBox
                                checkbox.isChecked = false
                            }
                        }
                    }

                    findNavController().navigate(R.id.action_questionAddFragment_to_homeFragment)
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}