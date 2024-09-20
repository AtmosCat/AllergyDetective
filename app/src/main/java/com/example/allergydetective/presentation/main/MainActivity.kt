package com.example.allergydetective.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.allergydetective.R
import com.example.allergydetective.databinding.ActivityMainBinding
import com.example.allergydetective.presentation.signin.SignInFragment
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

        }
        setFragment(SignInFragment())
    }

    private fun setFragment(frag : Fragment) {
        supportFragmentManager.commit {
            add(R.id.main_frame, frag)
        }
    }
}