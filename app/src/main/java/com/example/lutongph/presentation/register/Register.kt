package com.example.lutongph.presentation.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.lutongph.R
import com.example.lutongph.presentation.login.Login
import com.example.lutongph.util.DBHelper

class Register : AppCompatActivity() {

    private lateinit var uname: EditText
    private lateinit var pword: EditText
    private lateinit var cpnum: EditText
    private lateinit var mail: EditText
    private lateinit var regbtn: Button
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        uname = findViewById(R.id.user)
        pword = findViewById(R.id.pass)
        cpnum = findViewById(R.id.cnum)
        mail = findViewById(R.id.email)
        regbtn = findViewById(R.id.btnregister)
        db = DBHelper(this)

        regbtn.setOnClickListener {
            val user = uname.text.toString()
            val pass = pword.text.toString()
            val cnum = cpnum.text.toString()
            val email = mail.text.toString()

            var valid: Boolean = validateinfo(user, pass, cnum, email)

            if (valid == true) {
                val savedata = db.insertdata(user, pass, cnum, email)
                if (savedata == true) {
                    Toast.makeText(this, "Signup Successfully", Toast.LENGTH_SHORT).show()
                    val Intent = Intent(this, Login::class.java)
                    startActivity(Intent)
                }
            }
        }
    }

    fun validateinfo(username: String, password: String, cnum: String, email: String): Boolean {
        var validation: Boolean = true

        if (email.length == 0) {
            mail.requestFocus()
            mail.setError("This field cannot be empty.")
            validation = false
        } else if (!email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) {
            mail.requestFocus()
            mail.setError("Use valid email.")
            validation = false
        }

        if (cnum.length == 0) {
            cpnum.requestFocus()
            cpnum.setError("This field cannot be empty.")
            validation = false
        } else if (!cnum.matches(Regex("^09\\d{9}$"))) {
            cpnum.requestFocus()
            cpnum.setError("Follow the correct format: 09xxxxxxxxx")
            validation = false
        }

        if (password.length == 0) {
            pword.requestFocus()
            pword.setError("This field cannot be empty.")
            validation = false
        } else if (password.length < 8) {
            pword.requestFocus()
            pword.setError("Minimum of 8 characters.")
            validation = false
        }

        if (username.length == 0) {
            uname.requestFocus()
            uname.setError("This field cannot be empty.")
            validation = false
        } else if (!username.matches(Regex("^[a-zA-Z0-9]+$"))) {
            uname.requestFocus()
            uname.setError("Invalid username. Please use only alphanumeric characters.")
            validation = false
        }

        return validation
    }
}