package com.example.quizapp

import android.annotation.SuppressLint
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
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
            when (menuItem.itemId) {
                R.id.home -> {
                    // get current fragment
                    val currentFragment = findNavController(R.id.nav_host_fragment).currentDestination?.label
                    // navigate from current fragment to home fragment
                    if (currentFragment != "fragment_home") {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                    }
                    true
                }
                R.id.quiz -> {
                    // get current fragment
                    val currentFragment = findNavController(R.id.nav_host_fragment).currentDestination?.label
                    Log.d("MainActivity", "currentFragment: $currentFragment")
                    // navigate from current fragment to quiz fragment
                    if (currentFragment != "fragment_quiz_start") {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.quizStartFragment)
                    }
                    true
                }
                R.id.profile -> {
                    // get current fragment
                    val currentFragment = findNavController(R.id.nav_host_fragment).currentDestination?.label
                    // navigate from current fragment to profile fragment
                    if (currentFragment != "fragment_profile") {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                    }
                    true
                }
                R.id.list_question -> {
                    // get current fragment
                    val currentFragment = findNavController(R.id.nav_host_fragment).currentDestination?.label
                    // navigate from current fragment to list question fragment
                    if (currentFragment != "fragment_list_question") {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.questionListFragment)
                    }
                    true
                }
                else -> false
            }
            drawerLayout.closeDrawer(Gravity.LEFT)
            true
        }
    }
}