package com.nyorsi.p3track

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nyorsi.p3track.models.UserModel
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.GlobalViewModel
import com.nyorsi.p3track.viewModels.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var navigationBarView: NavigationBarView
    private lateinit var userViewModel: UserViewModel
    private lateinit var globalViewModel: GlobalViewModel
    private var checkedUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            !checkedUser || userViewModel.requestState.value == RequestState.LOADING
        }

        setContentView(R.layout.activity_main)

        navigationBarView = findViewById(R.id.bottom_navigation)
        globalViewModel = ViewModelProvider(this)[GlobalViewModel::class.java]

        navigationBarView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_activities -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.actvitiesFragment)
                }
                R.id.item_tasks -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.myTasksFragment)
                }
                R.id.item_groups -> {
                    // Respond to navigation item 2 click
                }
                R.id.item_profile -> {
                    globalViewModel.getLoggedInUser()
                    globalViewModel.requestState.observe(this) {
                        if (it == RequestState.SUCCESS) {
                            val parsedValue = Gson().toJson(globalViewModel.getCurrentUser()!!, object: TypeToken<UserModel>() {}.type)
                            findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment, Bundle().apply {
                                putString("loggedInUser", parsedValue)
                            })
                        }
                    }
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
                        findNavController(R.id.nav_host_fragment).navigate(R.id.actvitiesFragment)
                    }
                    else -> {
                        checkedUser = true
                        findNavController(R.id.nav_host_fragment).navigate(R.id.loginFragment)
                    }
                }
            }
        } else {
            checkedUser = true
        }
    }
}