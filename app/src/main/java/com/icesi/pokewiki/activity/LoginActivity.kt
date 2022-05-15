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
import com.icesi.pokewiki.databinding.LoginActivityBinding
import com.icesi.pokewiki.model.Pokemon
import com.icesi.pokewiki.model.User

class LoginActivity : AppCompatActivity() {
    private val binding: LoginActivityBinding by lazy {
        LoginActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.newUserText.setOnClickListener {
            val intent = Intent(this, NewUserActivity::class.java).apply{
            }
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            var usernameCheck = ""+binding.usernameText.text
            Firebase.firestore.collection("users").get().addOnCompleteListener { task ->
                for(doc in task.result!!){
                    var userInDB = doc.toObject(User::class.java).username
                    if(usernameCheck == userInDB) {

                        val intent = Intent(this, MainActivity::class.java).apply{
                            putExtra("currentUser", userInDB)
                        }
                        startActivity(intent)
                    }else{
                        Toast.makeText(applicationContext, "Nombre de usuario incorrecto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loadPokemon(user: String) {


    }

    private fun onResult(activityResult: ActivityResult?) {

    }
}
