package com.example.quizapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapp.models.Item
import com.example.quizapp.models.QuestionType
import com.example.quizapp.services.ItemService
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class QuizViewModel: ViewModel() {
    private var numberOfQuestions = 3
    private val itemService = ItemService()
    private var questions = itemService.selectRandomItems(numberOfQuestions)
    private var questionTemp = MutableLiveData<ArrayList<Item>>()
    private var countCorrect = 0
    private var countPartiallyCorrect = 0
    private var countPartiallyCorrectPoints = 0.0
    private var itQuestion: MutableIterator<Item> = startQuiz().iterator()
    var currentQuestion: MutableLiveData<Pair<Item?, Boolean>> =
        MutableLiveData<Pair<Item?, Boolean>>()

    init {
        currentQuestion.value = Pair(itQuestion.next(), false)
    }

    private fun startQuiz(): MutableList<Item> {
        itemService.randomizeQuestions()
        questionTemp.value = questions
        return questions
    }

    fun getNextQuestion() {
        if (itQuestion.hasNext()) {
            currentQuestion.value = itQuestion.next() to !itQuestion.hasNext()
        }
    }

    fun checkAnswer(answer: MutableList<Int>) {
        Log.d("QuizViewModel", "user answers: $answer")
        Log.d("QuizViewModel", "correct answers: ${currentQuestion.value?.first?.correct}")

        when(currentQuestion.value?.first?.type) {
            QuestionType.SINGLE_CHOICE.ordinal -> {
                if (answer[0] == currentQuestion.value?.first?.correct?.get(0)) {
                    countCorrect++
                }
            }
            QuestionType.MULTIPLE_CHOICE.ordinal -> {
                if (answer.containsAll(currentQuestion.value?.first?.correct!!)) {
                    countCorrect++
                } else if (answer.intersect(currentQuestion.value?.first?.correct!!.toSet()).isNotEmpty()) {
                    countPartiallyCorrect++
                    Log.d("QuizViewModel", "PARTIALLY CORRECT")
                    val totalNumberAnswers = currentQuestion.value?.first?.correct?.size!!
                    val totalNumberCorrectAnswers = answer.intersect(currentQuestion.value?.first?.correct!!.toSet()).size
                    countPartiallyCorrectPoints += (totalNumberCorrectAnswers.toDouble() / totalNumberAnswers.toDouble())
                }
            }
            QuestionType.SPINNER.ordinal -> {
                if (answer[0] == currentQuestion.value?.first?.correct?.get(0)) {
                    countCorrect++
                }
            }
        }

        Log.d("QuizViewModel", "countCorrect: $countCorrect")
        Log.d("QuizViewModel", "count partially correct: $countPartiallyCorrect")
        Log.d("QuizViewModel", "partially correct points: $countPartiallyCorrectPoints")
    }

    fun getScore() : Double {
        countPartiallyCorrectPoints = (countPartiallyCorrectPoints * 100.0).roundToInt() / 100.0
        return countCorrect.toDouble() + countPartiallyCorrectPoints
    }

    fun getCorrectAnswers() : Int {
        return countCorrect
    }

    fun getPartiallyCorrectAnswers() : Int {
        return countPartiallyCorrect
    }

    fun getPartiallyCorrectPoints() : Double {
        return countPartiallyCorrectPoints
    }

    fun getIncorrectAnswers() : Int {
        Log.d("QuizViewModel", "questions size: ${questions.size}")
        Log.d("QuizViewModel", "correct answers: $countCorrect")
        Log.d("QuizViewModel", "partially correct answers: $countPartiallyCorrect")
        return numberOfQuestions - countCorrect - countPartiallyCorrect
    }

    fun resetQuiz() {
        countCorrect = 0
        countPartiallyCorrect = 0
        countPartiallyCorrectPoints = 0.0
        val temp = (1..itemService.getNumberTotalQuestions()).random()
        Log.d("QuizViewModelQ", "temp: $temp")
        questions = itemService.selectRandomItems(temp)
        questionTemp.value = questions
        numberOfQuestions = questions.size
        Log.d("QuizViewModelQ", "number of questions: $numberOfQuestions")
        itQuestion = startQuiz().iterator()
        currentQuestion.value = Pair(itQuestion.next(), false)
    }

    fun getNumberOfQuestions() : Int {
        return numberOfQuestions
    }

    fun getAllQuestions() : LiveData<ArrayList<Item>> {
        questionTemp.value = questions
        return questionTemp
    }

    fun addQuestion(item: Item) {
        itemService.addItem(item)
        questionTemp.value = questions
    }

    fun deleteQuestion(position: Int) {
        itemService.deleteItem(position)
        questionTemp.value = questions
    }
}