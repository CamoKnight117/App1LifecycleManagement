package com.example.app1lifecyclemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class loggedInActivity : AppCompatActivity() {

    private var textViewLoggedInMessage:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)
        textViewLoggedInMessage = findViewById(R.id.textViewLoggedInMessage)

        val firstName = intent.getStringExtra("FIRST_NAME")
        val lastName = intent.getStringExtra("LAST_NAME")
        val combined = "$firstName $lastName is logged in!"
        textViewLoggedInMessage!!.text = combined
    }
}