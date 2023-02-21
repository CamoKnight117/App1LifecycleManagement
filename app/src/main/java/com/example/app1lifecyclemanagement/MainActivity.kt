package com.example.app1lifecyclemanagement

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    //create variables for the UI elements we control
    private var firstName:EditText? = null
    private var middleName:EditText? = null
    private var lastName:EditText? = null
    private var profileButton:ImageView? = null
    private var submitButton:Button? = null
    private var errorMessage:TextView? = null

    //create a variable to store the current bitmap image
    private var bitmapProfilePic:Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstName = findViewById<EditText>(R.id.editTextFirstName)
        middleName = findViewById<EditText>(R.id.editTextMiddleName)
        lastName = findViewById<EditText>(R.id.editTextLastName)
        profileButton = findViewById<ImageView>(R.id.profileImageButton)
        submitButton = findViewById<Button>(R.id.submitButton)
        errorMessage = findViewById<TextView>(R.id.textViewErrorMessage)

        if(savedInstanceState != null) {
            firstName!!.setText(savedInstanceState.getString("FIRST_NAME"))
            middleName!!.setText(savedInstanceState.getString("MIDDLE_NAME"))
            lastName!!.setText(savedInstanceState.getString("LAST_NAME"))

            if (Build.VERSION.SDK_INT >= 33) {
                bitmapProfilePic = savedInstanceState.getParcelable("PROFILE_PICTURE", Bitmap::class.java)
                if(bitmapProfilePic != null) {
                    profileButton!!.setImageBitmap(bitmapProfilePic)
                }
            }
            else{
                bitmapProfilePic = savedInstanceState.getParcelable<Bitmap>("PROFILE_PICTURE")
                if(bitmapProfilePic != null) {
                    profileButton!!.setImageBitmap(bitmapProfilePic)
                }
            }
        }

        profileButton!!.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                cameraActivity.launch(cameraIntent)
            }
            catch (e: ActivityNotFoundException)
            {
                //Normal error handling outside of assignment scope
            }
        }

        submitButton!!.setOnClickListener {
            if(bitmapProfilePic == null) //User needs to take a profile pic
            {
                errorMessage!!.text = getString(R.string.error_profile_pic)
            }
            else if(firstName!!.text.toString().isBlank() //User needs to fill edit texts
                || middleName!!.text.toString().isBlank()
                || lastName!!.text.toString().isBlank()) {
                errorMessage!!.text = getString(R.string.error_fill_edittext)
            }
            else //The user has entered everything they needed to, so proceed to the next page
            {
                //errorMessage!!.text = ""
                val intent = Intent(this, loggedInActivity::class.java)
                intent.putExtra("FIRST_NAME", firstName!!.text.toString())
                intent.putExtra("LAST_NAME", lastName!!.text.toString())
                startActivity(intent)
            }
        }
    }

    //Create the camera activity we will use when the user clicks the image button
    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= 33) {
                bitmapProfilePic =
                    result.data!!.getParcelableExtra("data", Bitmap::class.java)
                profileButton!!.setImageBitmap(bitmapProfilePic)
            } else {
                bitmapProfilePic = result.data!!.getParcelableExtra<Bitmap>("data")
                profileButton!!.setImageBitmap(bitmapProfilePic)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("FIRST_NAME", firstName!!.text.toString())
        outState.putString("MIDDLE_NAME", middleName!!.text.toString())
        outState.putString("LAST_NAME", lastName!!.text.toString())
        outState.putParcelable("PROFILE_PICTURE", bitmapProfilePic)
        // Save the values you need into "outState"
        super.onSaveInstanceState(outState)
    }
}