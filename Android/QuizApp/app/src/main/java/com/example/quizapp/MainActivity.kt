package com.example.quizapp

import android.app.AlertDialog
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.quizapp.interfaces.QuestionEndpoints
import com.example.quizapp.models.Item
import com.example.quizapp.models.QuestionDifficulty
import com.example.quizapp.models.QuestionType
import com.example.quizapp.repositories.ItemRepository
import com.example.quizapp.repositories.QuestionRepository
import com.example.quizapp.services.ItemService
import com.example.quizapp.services.RetrofitService
import com.example.quizapp.viewModels.QuizViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var topBar : MaterialToolbar
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initListeners()

        supportActionBar?.hide()

        ItemRepository.loadItems(com.example.quizapp.models.items)

        var items = mutableListOf<Item>()
        lifecycle.coroutineScope.launch {
            try {
                Log.d("QuizViewModelAPI", "Started loading questions")
                val response = RetrofitService.api.getQuestions(10)
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
                            } as QuestionDifficulty
                        )
                    } as MutableList<Item>?)!!

                    Log.d("QuizViewModelAPI", items.toString())

                    Log.d(TAG, "items: $items")
                    ItemRepository.loadItems(items)
                } else {
                    Log.d("QuizViewModelAPI", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.d("QuizViewModelAPI", "Error: ${e.message}")
            }
        }
    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawerLayout)
        topBar = findViewById(R.id.topAppBar)
        navigationView = findViewById(R.id.navigationView)
    }

    private fun initListeners() {
        topBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            val currentFragment = findNavController(R.id.nav_host_fragment).currentDestination?.label

            if (currentFragment == "fragment_question") {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Are you sure?")
                builder.setMessage("You will lose your progress")
                builder.setPositiveButton("Yes") { _, _ ->
                    navigationView.menu.findItem(R.id.quiz).isChecked = true
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_questionFragment_to_quizEndFragment)
                }
                builder.setNegativeButton("No") { _, _ ->
                    navigationView.menu.findItem(R.id.quiz).isChecked = true
                }
                builder.show()

                drawerLayout.closeDrawers()

                return@setNavigationItemSelectedListener true
            }

            when (menuItem.itemId) {
                R.id.home -> {
                     if (currentFragment != "fragment_home") {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                    }
                }
                R.id.quiz -> {
                    if (currentFragment != "fragment_quiz_start") {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.quizStartFragment)
                    }
                }
                R.id.profile -> {
                    if (currentFragment != "fragment_profile") {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                    }
                }
                R.id.list_question -> {
                    if (currentFragment != "fragment_list_question") {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.questionListFragment)
                    }
                }
                R.id.new_question -> {
                     if (currentFragment != "fragment_question_add") {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.questionAddFragment)
                    }
                }
            }
            drawerLayout.closeDrawer(Gravity.LEFT)
            true
        }
    }
}