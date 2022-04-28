package com.icesi.pokewiki.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.pokewiki.databinding.InfoActivityBinding
import com.icesi.pokewiki.databinding.LoginActivityBinding
import com.icesi.pokewiki.databinding.NewUserActivityBinding
import com.icesi.pokewiki.model.Pokemon
import java.util.HashMap

class NewUserActivity : AppCompatActivity() {

    private lateinit var binding: NewUserActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewUserActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createButton.setOnClickListener {
            var username = binding.usernamenewText.text.toString()
            if(username !=""){
                Firebase.firestore.collection("users")
                    .document(username)
                    .get()
                    .addOnCompleteListener {
                        task ->

                        if(task.result.data == null){
                            val add = HashMap<String, Any>()
                            add["username"] = username
                            Firebase.firestore.collection("users")
                                .document(username)
                                .set(add)

                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra("currentUser", username)
                            }
                            startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext, "Escribe otro nombre de usuario", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
}