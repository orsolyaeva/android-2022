package com.example.quizapp

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val getContent = registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
        val cursor: Cursor? = contentResolver.query(uri!!, null, null, null)
        if(cursor != null) {
            cursor.moveToFirst()
            val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
            val editText = findViewById<EditText>(R.id.userName)
            editText.setText(name)
        } else {
            Log.d(TAG, "cursor is null")
        }
    }

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { it ->
            val userAvatar = findViewById<ImageView>(R.id.userAvatar)
            userAvatar.setImageURI(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "onCreate called")

        val contactButton: Button = findViewById<Button>(R.id.contactButton)
        contactButton.setOnClickListener {
            getContent.launch(null)
        }

        val imageButton: Button = findViewById<Button>(R.id.selectImageButton)

        imageButton.setOnClickListener {
            getImage.launch("image/*")
        }
    }


    fun onButtonClicked(view: View) {
        Log.i("Info", "Button Pressed");

        val userName = findViewById<EditText>(R.id.userName).text.toString()
        val userAge = findViewById<EditText>(R.id.userAge).text.toString()
        val userAvatar = findViewById<ImageView>(R.id.userAvatar).drawable.toString()

//        val text = "You pressed the button!"
//        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
//        toast.show()

        if (userAge.toIntOrNull() == null) {
            Snackbar.make(view, "Please enter a valid age!", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (userAge.toInt() < 0 || userAge.toInt() > 100) {
            Snackbar.make(view, "Please enter a valid age", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (userName.isEmpty()) {
            Snackbar.make(view, "Please enter your name", Snackbar.LENGTH_SHORT).show()
        } else {

            val intent = Intent(this, GetStartedActivity::class.java).apply {
                putExtra("userName", userName)
            }

//            val snack = Snackbar.make(view, "$userName, you pressed the button!", Snackbar.LENGTH_SHORT)
//            snack.show()

            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart called")
    }
}