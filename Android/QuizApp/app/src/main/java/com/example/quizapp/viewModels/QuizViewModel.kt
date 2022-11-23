package com.example.quizapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.models.Item
import com.example.quizapp.models.QuestionDifficulty
import com.example.quizapp.models.QuestionType
import com.example.quizapp.repositories.ItemRepository
import com.example.quizapp.services.ItemService
import com.example.quizapp.services.RetrofitService
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class QuizViewModel: ViewModel() {
    private var numberOfQuestions = 3
    private var itemService = ItemService(ItemRepository)
    private var questions: ArrayList<Item> = itemService.selectRandomItems(numberOfQuestions)
    private var allQuestions = MutableLiveData<ArrayList<Item>>()
    private var questionsFiltered = MutableLiveData<ArrayList<Item>>()
    private var questionCategories = MutableLiveData<ArrayList<String>>()
    private var countCorrect = 0
    private var countPartiallyCorrect = 0
    private var countPartiallyCorrectPoints = 0.0
    private var itQuestion: MutableIterator<Item> = startQuiz().iterator()
    var currentQuestion: MutableLiveData<Pair<Item?, Boolean>> =
        MutableLiveData<Pair<Item?, Boolean>>()

    companion object {
        private const val TAG = "QuizViewModel"
    }

    init {
        currentQuestion.value = Pair(itQuestion.next(), false)
    }

    private fun startQuiz(): MutableList<Item> {
        itemService.randomizeQuestions()

        for (question in itemService.getAllItems()) {
           // check if question categories is not empty
            if (questionCategories.value != null) {
                // check if question category is not already in the list
                if (!questionCategories.value!!.contains(question.category)) {
                    questionCategories.value!!.add(question.category)
                }
            } else {
                // if question categories is empty, add the first category
                questionCategories.value = ArrayList()
                questionCategories.value!!.add(question.category)
            }
        }


        Log.d(TAG, "questionCategories: ${questionCategories.value}")
        Log.d(TAG, "Questions: ${questions.size}")
        Log.d(TAG, "Questions: $questions")

        return questions
    }

    fun getNextQuestion() {
        if (itQuestion.hasNext()) {
            currentQuestion.value = itQuestion.next() to !itQuestion.hasNext()
        }
    }

    fun checkAnswer(answer: MutableList<Int>) {
        Log.d(TAG, "user answers: $answer")
        Log.d(TAG, "correct answers: ${currentQuestion.value?.first?.correct}")

        if (answer.isEmpty()) {
            return
        }

        when(currentQuestion.value?.first?.type) {
            QuestionType.SINGLE_CHOICE.ordinal -> {
                val userAnswer = currentQuestion.value?.first?.answers?.get(answer[0])
                if (userAnswer == currentQuestion.value?.first?.correct?.get(0)) {
                    countCorrect++
                }
            }
            QuestionType.MULTIPLE_CHOICE.ordinal -> {
                val userAnswers = mutableListOf<String>()
                for (i in answer) {
                    userAnswers.add(currentQuestion.value?.first?.answers?.get(i)!!)
                }
                if (userAnswers.containsAll(currentQuestion.value?.first?.correct!!)) {
                    countCorrect++
                } else if (userAnswers.intersect(currentQuestion.value?.first?.correct!!.toSet()).isNotEmpty()) {
                    countPartiallyCorrect++
                    Log.d(TAG, "PARTIALLY CORRECT")
                    val totalNumberAnswers = currentQuestion.value?.first?.correct?.size!!
                    val totalNumberCorrectAnswers = userAnswers.intersect(currentQuestion.value?.first?.correct!!.toSet()).size
                    countPartiallyCorrectPoints += (totalNumberCorrectAnswers.toDouble() / totalNumberAnswers.toDouble())
                }
            }
            QuestionType.TRUE_FALSE.ordinal -> {
                val userAnswer = currentQuestion.value?.first?.answers?.get(answer[0])
                if (userAnswer == currentQuestion.value?.first?.correct?.get(0)) {
                    countCorrect++
                }
            }
        }

        Log.d(TAG, "countCorrect: $countCorrect")
        Log.d(TAG, "count partially correct: $countPartiallyCorrect")
        Log.d(TAG, "partially correct points: $countPartiallyCorrectPoints")
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
        Log.d(TAG, "questions size: ${questions.size}")
        Log.d(TAG, "correct answers: $countCorrect")
        Log.d(TAG, "partially correct answers: $countPartiallyCorrect")
        return numberOfQuestions - countCorrect - countPartiallyCorrect
    }

    fun resetQuiz() {
        countCorrect = 0
        countPartiallyCorrect = 0
        countPartiallyCorrectPoints = 0.0
        val temp = (3..itemService.getNumberTotalQuestions()).random()
        Log.d(TAG, "temp: $temp")
        questions = itemService.selectRandomItems(temp)
        numberOfQuestions = questions.size
        Log.d(TAG, "number of questions: $numberOfQuestions")
        itQuestion = startQuiz().iterator()
        currentQuestion.value = Pair(itQuestion.next(), false)
    }

    fun getNumberOfQuestions() : Int {
        return numberOfQuestions
    }

    fun getAllQuestions() : LiveData<ArrayList<Item>> {
        allQuestions.value = itemService.getAllItems()
        return allQuestions
    }

    fun addQuestion(item: Item) {
        itemService.addItem(item)
        allQuestions.value = itemService.getAllItems()

        if (!questionCategories.value!!.contains(item.category)) {
            questionCategories.value!!.add(item.category)
        }
    }

    fun deleteQuestion(position: Int) {
        val category = allQuestions.value?.get(position)?.category
        itemService.deleteItem(position)
        allQuestions.value = itemService.getAllItems()
        if (allQuestions.value?.find { it.category == category } == null) {
            questionCategories.value?.remove(category)
        }
    }

    fun getCategoryList() : ArrayList<String> {
        return questionCategories.value!!
    }

    fun filterQuestions(category: String) :  LiveData<ArrayList<Item>> {
        val filteredQuestions = itemService.filterQuestions(category)
        questionsFiltered.value = filteredQuestions
        return questionsFiltered
    }

    private fun getQuestionFromAPI(): MutableList<Item> {
        var items = mutableListOf<Item>()
        viewModelScope.launch {
            try {
                Log.d("QuizViewModelAPI", "Started loading questions")
                val response =  RetrofitService.api.getQuestions(3)
                Log.d("QuizViewModelAPI", "Finished loading questions")
                if (response.isSuccessful) {
                    val questions = response.body()
                    items = (questions?.results?.map {
                        Item(
                            type = when(it.type) {
                                "multiple" -> QuestionType.SINGLE_CHOICE.ordinal
                                "boolean" -> QuestionType.TRUE_FALSE.ordinal
                                else -> QuestionType.SINGLE_CHOICE.ordinal
                            },
                            question = it.question,
                            answers = it.incorrectAnswers.toMutableList().apply { add(it.correctAnswer) },
                            correct = mutableListOf(it.correctAnswer),
                            category = it.category,
                            difficulty = when(it.difficulty) {
                                "easy" -> QuestionDifficulty.EASY
                                "medium" -> QuestionDifficulty.MEDIUM
                                "hard" -> QuestionDifficulty.HARD
                                else -> QuestionDifficulty.EASY
                            }
                        )
                    } as MutableList<Item>?)!!

                    Log.d("QuizViewModelAPI", items.toString())
                }
            } catch (e: Exception) {
                Log.d("QuizViewModelAPI", "Error: ${e.message}")
            }
        }
        return items
//        return mutableListOf()
    }
}