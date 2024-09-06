package com.example.allergydetective.presentation.signin

import com.example.allergydetective.presentation.UserViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.databinding.FragmentSignInBinding
import com.example.allergydetective.presentation.home.HomeFragment
import com.example.allergydetective.presentation.home.MyPageFragment
import com.example.allergydetective.presentation.signin.findId.FindIdFragment
import com.example.allergydetective.presentation.signin.findPw.FindPwFragment
import com.example.allergydetective.presentation.signup.SignUpFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null

    private var user: User? = null
    private var _users: MutableList<User>? = null


    private val binding get() = _binding!!

    // 이렇게 뷰모델 호출하는 거 맞나?
    private val viewModel: UserViewModel by activityViewModels() {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isIdCorrect = false

        var user1 = User("1", "1", "1", "1", "1")
        var user2 = User("2", "2", "2", "2", "2")
        var user3 = User("3", "3", "3", "3", "3")

        viewModel.addUser(user1)
        viewModel.addUser(user2)
        viewModel.addUser(user3)

        binding.btnSignin.setOnClickListener {

            var email = binding.etSigninEmail.text.toString()
            var pw = binding.etSigninPw.text.toString()

            viewModel.findUser(email)
            viewModel.signingInUser.observe(viewLifecycleOwner) { data ->
                user = data
                if (binding.etSigninEmail.text.isEmpty() || binding.etSigninPw.text.isEmpty()) {
                    Toast.makeText(requireContext(), "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    if (user == null) {
                        Toast.makeText(requireContext(), "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        if (pw != user!!.pw) {
                            Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            viewModel.setCurrentUser(user!!)
                            Toast.makeText(requireContext(), "로그인 성공!", Toast.LENGTH_SHORT).show()

                            val homeFragment = requireActivity().supportFragmentManager.findFragmentByTag("HomeFragment")
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                hide(this@SignInFragment)
                                if (homeFragment == null) {
                                    add(R.id.main_frame, HomeFragment(), "HomeFragment")
                                } else {
                                    show(homeFragment)
                                }
                                addToBackStack(null)
                                commit()
                            }
                        }
                    }
                }
            }
        }

        binding.btnSignup.setOnClickListener{
            Toast.makeText(requireContext(), "회원가입 페이지로 이동합니다.", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, SignUpFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnSigninFindIdPw.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setTitle("이메일/비밀번호 찾기")
                .setMessage("항목을 선택해주세요.")
                .setPositiveButton("이메일 찾기") { dialog, which ->
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, FindIdFragment())
                        .addToBackStack(null)
                        .commit()
                }
                .setNegativeButton("비밀번호 찾기") { dialog, which ->
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, FindPwFragment())
                        .addToBackStack(null)
                        .commit()
                }
                .setNeutralButton("닫기") { dialog, which ->
                }
                .show()
        }

        binding.btnGoogleSignin.setOnClickListener{

        }

        binding.btnKakaoSignin.setOnClickListener{

        }

    }
//    private fun initViewModel() {
//        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
//            when (uiState) {
//                is UiState.Loading -> {
//                    binding.progress.isVisible = true
//                }
//
//                is UiState.Success -> {
//                    binding.progress.isVisible = false
//
//                }
//
//                is UiState.Error -> {
//                    Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        }
////        viewModel.set
//    }
}