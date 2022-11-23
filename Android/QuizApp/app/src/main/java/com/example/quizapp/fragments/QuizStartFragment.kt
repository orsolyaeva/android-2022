package com.example.quizapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizStartBinding
import com.example.quizapp.viewModels.QuizViewModel
import com.example.quizapp.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar


class QuizStartFragment : Fragment() {
    private lateinit var contactButton: Button
    private lateinit var getStartedButton: Button
    private lateinit var imageButton: Button
    private lateinit var userName : EditText
    private lateinit var userAvatar: ImageView

    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: FragmentQuizStartBinding
    private val viewModel: QuizViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    companion object {
        const val TAG = "QuizStartFragment"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // contact picker activity
    private val getContent = registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
        val resolver = requireActivity().contentResolver
        val cursor: Cursor? = resolver.query(uri!!, null, null, null)
        if(cursor != null) {
            cursor.moveToFirst()
            val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
            userName.setText(name)
        } else {
            Log.d(TAG, "cursor is null")
        }
    }

    // image picker activity
    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { it ->
            val userAvatar = binding.userAvatar
            userAvatar.setImageURI(it)

            userViewModel.setProfilePicture(it)
//            sharedPref.edit().putString("profilePicture", it.toString()).apply()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()

        // if user name is already saved in shared preferences, set it to the edit text
        if (sharedPref.contains("username")) {
            userName.setText(sharedPref.getString("username", ""))
        }

        // if user profile picture is already saved in shared preferences, set it to the image view
        if(userViewModel.getProfilePicture() != null) {
            userAvatar.setImageURI(userViewModel.getProfilePicture())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: ")
        binding = FragmentQuizStartBinding.inflate(inflater, container, false)
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        viewModel.resetQuiz()

        return binding.root
    }

    private fun initViews() {
        contactButton = binding.contactButton
        getStartedButton = binding.startButton
        imageButton = binding.selectImageButton
        userName = binding.userName
        userAvatar = binding.userAvatar
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        contactButton.setOnClickListener {
            getContent.launch(null)
        }

        getStartedButton.setOnClickListener {
            onButtonClicked()
        }

        imageButton.setOnClickListener {
            getImage.launch("image/*")
        }
    }

    private fun onButtonClicked() {
        Log.i(TAG, "Button Pressed")

        val userNameT = userName.text.toString()

        if (userNameT.isEmpty()) {
            Snackbar.make(binding.root, "Please enter your name", Snackbar.LENGTH_SHORT).show()
        } else {
            // get username from shared preferences
            val username = sharedPref.getString("username", null)

            // if user name is changed, reset the quiz
            if (username != userNameT) {
                with (sharedPref.edit()) {
                    putFloat("highScore", 0.0F)
                    putString("username", userNameT)
                    apply()
                }

                userViewModel.resetHighScore()
                userViewModel.setName(userNameT)
            }

            // check if username is empty from shared preferences
            if (sharedPref.getString("username", null) == null) {
                // save username to shared preferences
                with (sharedPref.edit()) {
                    putString("username", userNameT)
                    apply()
                }
                userViewModel.setName(userNameT)
            }
            findNavController().navigate(R.id.action_quizStartFragment_to_questionFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback : OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               findNavController().navigate(R.id.action_quizStartFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}