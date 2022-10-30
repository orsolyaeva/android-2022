package com.example.quizapp.fragments

import android.content.Context
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
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.TAG
import com.example.quizapp.databinding.FragmentQuizStartBinding
import com.example.quizapp.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [QuizStartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizStartFragment : Fragment() {
    private lateinit var binding: FragmentQuizStartBinding
    private lateinit var contactButton: Button
    private lateinit var getStartedButton: Button
    private lateinit var imageButton: Button
    private lateinit var userName : EditText
    private lateinit var userAge: EditText
    private lateinit var userViewModel: UserViewModel

    @RequiresApi(Build.VERSION_CODES.O)
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

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { it ->
            val userAvatar = binding.userAvatar
            userAvatar.setImageURI(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizStartBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        return binding.root
    }

    private fun initViews() {
        contactButton = binding.contactButton
        getStartedButton = binding.startButton
        imageButton = binding.selectImageButton
        userName = binding.userName
        userAge = binding.userAge
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
        Log.i("Info", "Button Pressed");

        val userNameT = userName.text.toString()
//        val userAgeT = userAge.text.toString()

        /*
        if (userAgeT.toIntOrNull() == null) {
//            Snackbar.make(, "Please enter a valid age!", Snackbar.LENGTH_SHORT).show()
            Snackbar.make(binding.root, "Please enter a valid age!", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (userAgeT.toInt() < 0 || userAgeT.toInt() > 100) {
            Snackbar.make(binding.root, "Please enter a valid age", Snackbar.LENGTH_SHORT).show()
            return
        }
        */

        if (userNameT.isEmpty()) {
            Snackbar.make(binding.root, "Please enter your name", Snackbar.LENGTH_SHORT).show()
        } else {
//            // if username is not empty, save it to shared preferences
//            if (userNameT.isNotEmpty()) {
//                val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
//                with (sharedPref.edit()) {
//                    putString("username", userNameT)
//                    apply()
//                }
//            }
            if (userViewModel.getName()?.isEmpty() == true) {
                userViewModel.setName(userNameT)
            }
            findNavController().navigate(R.id.action_quizStartFragment_to_questionFragment)
//            Snackbar.make(binding.root, "Welcome $userNameT", Snackbar.LENGTH_SHORT).show()
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