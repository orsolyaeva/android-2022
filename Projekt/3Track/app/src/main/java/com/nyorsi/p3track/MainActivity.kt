package com.nyorsi.p3track

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationBarView
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var navigationBarView: NavigationBarView
    private lateinit var userViewModel: UserViewModel
    private var checkedUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            !checkedUser
        }

        setContentView(R.layout.activity_main)

        navigationBarView = findViewById(R.id.bottom_navigation)

        navigationBarView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_activities -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.actvitiesFragment)
                }
                R.id.item_tasks -> {

                }
                R.id.item_groups -> {
                    // Respond to navigation item 2 click
                }
                R.id.item_profile -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                }
            }
            true
        }

        val sharedPref = getSharedPreferences("P3Track", MODE_PRIVATE)
        val deadline = sharedPref?.getString("deadline", null)
        val token = sharedPref?.getString("token", null)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        if(deadline != null && deadline > System.currentTimeMillis().toString()) {
            userViewModel.getMyUser(token)
            userViewModel.requestState.observe(this) {
                when (it) {
                    RequestState.SUCCESS -> {
                        checkedUser = true
                        findNavController(R.id.nav_host_fragment).navigate(R.id.actvitiesFragment)
                    }
                    else -> {
                        checkedUser = true
                    }
                }
            }
        } else {
            checkedUser = true
        }
    }
}