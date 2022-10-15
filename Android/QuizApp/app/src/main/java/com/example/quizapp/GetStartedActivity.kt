package com.example.quizapp

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import java.io.File

class GetStartedActivity : AppCompatActivity() {

    private val getImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { it ->
                val userPicture = findViewById<ImageView>(R.id.userPicture)
                userPicture.setImageURI(it)
            }
        }

    private val takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            latestTmpUri?.let { uri ->
                val userPicture = findViewById<ImageView>(R.id.userPicture)
                userPicture.setImageURI(uri)
            }
        }
    }

    private var latestTmpUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        val userName = intent.getStringExtra("userName")
        val playerName = findViewById<TextView>(R.id.playerName).apply {
            text = userName.toString()
        }

        val selectPhotoButton = findViewById<Button>(R.id.selectPhotoButton)
        selectPhotoButton.setOnClickListener {
            getImage.launch("image/*")
        }

        val takePhotoButton = findViewById<Button>(R.id.takePhotoButton)
        takePhotoButton.setOnClickListener {
           takeImage()
        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImage.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }
}
