package com.allergyguardian.allergyguardian.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.databinding.ActivityMainBinding
import com.allergyguardian.allergyguardian.presentation.signin.SignInFragment
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