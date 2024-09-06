package com.example.allergydetective.presentation.signup

import com.example.allergydetective.presentation.UserViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.databinding.FragmentSignUpBinding
import com.example.allergydetective.presentation.signin.SignInFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null

    private var doesEmailExist: User? = null

    private var emailConfirm = false
    private var pwConfirm = false
    private var privacyConfirm = false

    private var auth: FirebaseAuth? = null

    private val binding get() = _binding!!

    // 이렇게 뷰모델 호출하는 거 맞나?
    private val viewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이메일 중복 체크, 빈칸 체크
        binding.btnSignupEmailCheck.setOnClickListener {

            var email = binding.etSignupEmail.text.toString()

            viewModel.findUser(email)
            viewModel.signingInUser.observe(viewLifecycleOwner) { data ->
                doesEmailExist = data
            }

            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (doesEmailExist != null) {
                    Toast.makeText(requireContext(), "중복된 이메일이 있습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    emailConfirm = true
                    Toast.makeText(requireContext(), "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 비밀번호 일치 체크
        binding.btnSignupPwCheck.setOnClickListener {
            var pwCheck = binding.etSignupPwCheck.text.toString()

            var pw = binding.etSignupPw.text.toString()
            if (pw != pwCheck) {
                Toast.makeText(requireContext(), "비밀번호가 일치하기 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show()
                pwConfirm = true
            }
        }

        // 개인정보 수집 동의 체크
        binding.checkBoxSignupPrivacy.setOnCheckedChangeListener{ _, isChecked ->
            privacyConfirm = if (isChecked) {
                true
            } else {
                false
            }
        }
        val email = binding.etSignupEmail.text.toString()
        val pw = binding.etSignupPw.text.toString()
        val name = binding.etSignupName.text.toString()
        val contact = binding.etSignupContact.text.toString()
        val nickname = binding.etSignupNickname.text.toString()

        val isEmailFilled = binding.etSignupEmail.text.isNotEmpty()
        val isPwFilled = binding.etSignupPw.text.isNotEmpty()
        val isPwCheckFilled = binding.etSignupPwCheck.text.isNotEmpty()
//        val isNameFilled = binding.etSignupName.text.isNotEmpty()
//        val isContactFilled = binding.etSignupContact.text.isNotEmpty()
//        val isNicknameFilled = binding.etSignupNickname.text.isNotEmpty()


        binding.btnSignupSignup.setOnClickListener {
            createAccount(
                binding.etSignupEmail.text.toString()
                ,binding.etSignupPw.text.toString()
                ,binding.etSignupName.text.toString()
                ,binding.etSignupContact.text.toString()
                ,binding.etSignupNickname.text.toString()
                ,emailConfirm,pwConfirm,privacyConfirm,
                binding.etSignupEmail.text.isNotEmpty()
                ,binding.etSignupPw.text.isNotEmpty()
                ,binding.etSignupPwCheck.text.isNotEmpty())
        }

        binding.btnSignupCancel.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun createAccount(
        email: String,
        pw: String,
        name: String,
        contact: String,
        nickname: String,
        emailConfirm: Boolean,
        pwConfirm: Boolean,
        privacyConfirm: Boolean,
        isEmailFilled: Boolean,
        isPwFilled: Boolean,
        isPwCheckFilled: Boolean
        ){
        if (!isEmailFilled || !isPwFilled || !isPwCheckFilled) {
            Toast.makeText(requireContext(), "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show()
        } else {
            if (!emailConfirm) {
                Toast.makeText(requireContext(), "이메일 중복확인을 해주세요.", Toast.LENGTH_SHORT).show()
            } else if (!pwConfirm) {
                Toast.makeText(requireContext(), "비밀번호 일치 여부를 확인해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else if (!privacyConfirm) {
                Toast.makeText(requireContext(), "개인정보 수집/이용에 동의해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val user = User(email, pw, name, contact, nickname)
                viewModel.addUser(user)
                auth?.createUserWithEmailAndPassword(email, pw)
                    ?.addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireActivity(), "회원가입이 완료되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireActivity(), "회원가입에 실패했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, SignInFragment())
                    .addToBackStack(null)
                    .commit()
            }
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
//    }
}