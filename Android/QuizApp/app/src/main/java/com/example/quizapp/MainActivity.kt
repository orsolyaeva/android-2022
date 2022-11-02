package com.example.quizapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

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