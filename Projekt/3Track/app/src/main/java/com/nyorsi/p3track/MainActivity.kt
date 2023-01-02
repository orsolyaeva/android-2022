package com.nyorsi.p3track

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
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

    private lateinit var userProfile: ImageView
    private lateinit var title: TextView

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        val sharedPref = getSharedPreferences("P3Track", MODE_PRIVATE)
        val deadline = sharedPref?.getString("deadline", null)
        val token = sharedPref?.getString("token", null)

        splashScreen.setKeepOnScreenCondition {
            !checkedUser && userViewModel.requestState.value == RequestState.LOADING
        }

        setContentView(R.layout.activity_main)

        userProfile = findViewById(R.id.toolbarProfile)
        title = findViewById(R.id.toolbarTitle)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        val toolbar = supportActionBar
        toolbar?.setDisplayShowTitleEnabled(false)
        userProfile.setImageDrawable(getDrawable(R.drawable.userprofile_placeholder))
//        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))

        navigationBarView = findViewById(R.id.bottom_navigation)
        globalViewModel = ViewModelProvider(this)[GlobalViewModel::class.java]

        navigationBarView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_activities -> {
                    title.text = "Activities"
                    findNavController(R.id.nav_host_fragment).navigate(R.id.actvitiesFragment)
                }
                R.id.item_tasks -> {
                    title.text = "My Tasks"
                    findNavController(R.id.nav_host_fragment).navigate(R.id.myTasksFragment)
                }
                R.id.item_groups -> {
                    title.text = "My Groups"
                    findNavController(R.id.nav_host_fragment).navigate(R.id.myGroupsFragment)
                }
                R.id.item_profile -> {
                    title.text = "Profile"
                    globalViewModel.getLoggedInUser()
                    globalViewModel.requestState.observe(this) {
                        if (it == RequestState.SUCCESS) {
                            val image = globalViewModel.getCurrentUser()?.image
                            if (image != null) {
                                Glide.with(this).load(image).into(userProfile)
                            }
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


        Log.d("MainActivity", "Deadline: $deadline")
        Log.d("MainActivity", "Token: $token")

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        if(deadline != null && deadline > System.currentTimeMillis().toString()) {
            Log.d("MainActivity", "Deadline is valid")
            userViewModel.getMyUser(token)
            userViewModel.requestState.observe(this) {
                when (it) {
                    RequestState.SUCCESS -> {
                        toolbar?.show()
                        findNavController(R.id.nav_host_fragment).navigate(R.id.actvitiesFragment)
                        val currentUser = userViewModel.getCurrentUser()
                        val image = currentUser?.image
                        if (image != null) {
                            Glide.with(this).load(image).into(userProfile)
//                            userProfile.visibility = ImageView.INVISIBLE
//                            toolbar?.setLogo(userProfile.drawable)
                        }
                        title.text = "Activities"
                    }
                    else -> {
                        checkedUser = true
                        title.text = "Activities"
                        findNavController(R.id.nav_host_fragment).navigate(R.id.loginFragment)
                    }
                }
            }
        } else {
            checkedUser = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
}