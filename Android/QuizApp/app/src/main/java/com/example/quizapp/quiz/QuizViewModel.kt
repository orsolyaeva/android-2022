package com.example.quizapp.quiz

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

class QuizViewModel: ViewModel() {
    private val numberOfQuestions = 3
    private var questions = selectRandomItems(numberOfQuestions)
    private var countCorrect = 0
    private var countPartiallyCorrect = 0
    private var countPartiallyCorrectPoints = 0.0
    var itQuestion: MutableIterator<Item> = startQuiz().iterator()
    var currentQuestion: MutableLiveData<Pair<Item?, Boolean>> =
        MutableLiveData<Pair<Item?, Boolean>>()

    init {
        currentQuestion.value = Pair(itQuestion.next(), false)
    }

    private fun selectRandomItems(count: Int) : MutableList<Item> {
        val itemsO = com.example.quizapp.quiz.items
        val items = mutableListOf<Item>()
        var counter = count

        if (count > itemsO.size || count < 0) {
            counter = itemsO.size
        }

        while(items.size < counter) {
            val item = itemsO.random()

            if (!items.contains(item)) {
                items.add(item)
            }
        }

        println(items)

        return items
    }

    private fun randomizeQuestions() {
        questions.shuffle()

        for (question in questions) {
            when(question.type) {
                QuestionType.SINGLE_CHOICE.ordinal -> {
                    val answer = question.answers[question.correct[0]]
                    question.answers.shuffle()
                    val tempIndex = question.answers.indexOf(answer)
                    question.correct = mutableListOf(tempIndex)
                }
                QuestionType.MULTIPLE_CHOICE.ordinal -> {
                    val correctAnswers = question.correct.map { question.answers[it] }

                    question.answers.shuffle()

                    val newCorrectAnswers = mutableListOf<Int>()
                    for (answer in correctAnswers) {
                        newCorrectAnswers.add(question.answers.indexOf(answer))
                    }

                    question.correct = newCorrectAnswers
                }
            }
        }
    }

    private fun startQuiz(): MutableList<Item> {
        randomizeQuestions()
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
        questions = selectRandomItems(numberOfQuestions)
        itQuestion = startQuiz().iterator()
        currentQuestion.value = Pair(itQuestion.next(), false)
    }

    fun getNumberOfQuestions() : Int {
        return numberOfQuestions
    }
}