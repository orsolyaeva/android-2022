package com.example.quizapp.quiz

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizViewModel: ViewModel() {
    private val numberOfQuestions = 4
    private var questions = selectRandomItems(numberOfQuestions)
    private var countCorrect = 0
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
            val answer = question.answers[question.correct.toInt()]
            question.answers.shuffle()
            val tempIndex = question.answers.indexOf(answer)
            question.correct = tempIndex
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

    fun checkAnswer(answer: Int) {
        Log.d("QuizViewModel", "checkAnswer: $answer")
        Log.d("QuizViewModel", "checkAnswer: ${currentQuestion.value?.first?.correct}")
        if (currentQuestion.value?.first?.correct == answer) {
            countCorrect++
        }
    }

    fun getScore() : Int {
        return countCorrect
    }

    fun resetQuiz() {
        countCorrect = 0
        questions = selectRandomItems(numberOfQuestions)
        itQuestion = startQuiz().iterator()
        currentQuestion.value = Pair(itQuestion.next(), false)
    }

    fun getNumberOfQuestions() : Int {
        return numberOfQuestions
    }
}