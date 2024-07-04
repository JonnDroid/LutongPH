package com.example.lutongph.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.lutongph.R
import com.example.lutongph.presentation.register.Register
import com.example.lutongph.presentation.user_manual.UserManual
import com.example.lutongph.util.DBHelper

class Login : AppCompatActivity() {
    private lateinit var uname: EditText
    private lateinit var pword: EditText
    private lateinit var loginbtn: Button
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        uname = findViewById(R.id.user)
        pword = findViewById(R.id.pass)
        loginbtn = findViewById(R.id.btnlogin)
        db = DBHelper(this)

        val btnSignin = findViewById<TextView>(R.id.txtregister)
        loginbtn.setOnClickListener {
            val user = uname.text.toString()
            val pass = pword.text.toString()
            val checker = db.validation(user, pass)

            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Incomplete details", Toast.LENGTH_SHORT).show()
            } else {
                if (checker == true) {
                    userID = db.getID(user, pass)
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UserManual::class.java)
                    startActivity(intent)
                } else {
                    val accountExists = db.checkAccountExists(user)
                    if (accountExists) {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Account does not exist", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            uname.setText("")
            pword.setText("")
        }

        btnSignin.setOnClickListener {
            val Intent = Intent(this, Register::class.java)
            startActivity(Intent)
        }
    }

    companion object {
        var userID: Int = 0
    }
}