package com.example.allergydetective.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.allergydetective.R
import com.example.allergydetective.databinding.ActivityMainBinding
import com.example.allergydetective.presentation.home.HomeFragment
import com.example.allergydetective.presentation.signin.SignInFragment
import com.firebase.ui.auth.data.model.User
import com.google.firebase.firestore.FirebaseFirestore

//import com.example.allergydetective.presentation.mypage.MyPageFragment
//import com.example.allergydetective.presentation.home.FilterFragment
//import com.example.allergydetective.presentation.home.ItemListFragment

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
//            ivTabCommunity.setOnClickListener{
//                setFragment(CommunityFragment())
//            }
//            ivTabMypage.setOnClickListener {
//                setFragment(MyPageFragment())
//            }
//            btnHomeFilter.setOnClickListener{
//                setFragment(FilterFragment())
//            }
//            ivHomeSearch.setOnClickListener{
//                setFragment(ItemListFragment())
//            }
        }
        setFragment(SignInFragment())

//        val user = com.example.allergydetective.data.model.user.User("ID","","","","")
//
//        db.collection("testUser") // 게시판
//            .document(user.id) // 제목
//            .set(user) // 넣을 데이터
    }

    private fun setFragment(frag : Fragment) {
        supportFragmentManager.commit {
            add(R.id.main_frame, frag)
        }
    }
}