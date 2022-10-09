package com.example.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonClicked(view: View) {
        Log.i("Info", "Button Pressed");

        val name = findViewById<EditText>(R.id.name).text.toString()

//        val text = "You pressed the button!"
//        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
//        toast.show()

        if (name.isEmpty()) {
            Snackbar.make(view, "Please enter your name", Snackbar.LENGTH_SHORT).show()
        } else {
            val snack = Snackbar.make(view, "$name, you pressed the button!", Snackbar.LENGTH_SHORT)
            snack.show()
        }
    }
}