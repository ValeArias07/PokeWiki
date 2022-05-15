package com.icesi.pokewiki.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.pokewiki.R
import com.icesi.pokewiki.databinding.LoginActivityBinding
import com.icesi.pokewiki.model.Pokemon
import com.icesi.pokewiki.model.User

class LoginActivity : AppCompatActivity() {
    private val binding: LoginActivityBinding by lazy { LoginActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.newUserText.setOnClickListener { onNewUserTextListener() }
        binding.loginBtn.setOnClickListener { onLoginBtnListener() }
    }

    private fun onNewUserTextListener(){
        val intent = Intent(this, NewUserActivity::class.java)
        startActivity(intent)
    }

    private fun onLoginBtnListener(){
        var usernameCheck = binding.usernameText.text.toString()
        Firebase.firestore.collection("users").whereEqualTo("username", usernameCheck).get().addOnCompleteListener{ task ->
            if(task.result?.size() != 0){
                lateinit var userInDB:User
                for(document in task.result!!){ userInDB = document.toObject(User::class.java) }
                val intent = Intent(this, MainActivity::class.java).apply{
                    putExtra("currentUser", userInDB.username)
                }
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext, R.string.username_login_fail, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
