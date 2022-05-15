package com.icesi.pokewiki.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.pokewiki.R
import com.icesi.pokewiki.databinding.NewUserActivityBinding
import com.icesi.pokewiki.model.User

class NewUserActivity : AppCompatActivity() {

    private val binding: NewUserActivityBinding by lazy { NewUserActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.createButton.setOnClickListener { onCreateBtnListener() }
        binding.exitButtonM.setOnClickListener { finish() }
    }

    private fun onCreateBtnListener(){
        val usernameCheck = binding.usernamenewText.text.toString()
        if(usernameCheck !=""){
            Firebase.firestore.collection("users").whereEqualTo("username", usernameCheck).get().addOnCompleteListener{ task ->
                if(task.result?.size() == 0) {
                    Firebase.firestore.collection("users").document(usernameCheck).set(User(usernameCheck))
                        .addOnCompleteListener {
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra("currentUser", usernameCheck)
                            }
                            startActivity(intent)
                            finish()
                        }
                }else Toast.makeText(applicationContext, R.string.username_clone_fail, Toast.LENGTH_LONG).show()
            }
        }else Toast.makeText(applicationContext, R.string.username_empty_fail, Toast.LENGTH_SHORT).show()
    }
}