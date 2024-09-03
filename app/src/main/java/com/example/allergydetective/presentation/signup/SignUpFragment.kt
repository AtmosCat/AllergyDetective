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

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null

    private var doesIdExist: User? = null

    private var idConfirm = false
    private var pwConfirm = false
    private var privacyConfirm = false

    private val binding get() = _binding!!

    // 이렇게 뷰모델 호출하는 거 맞나?
    private val viewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel() } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        // ID 중복 체크, 빈칸 체크
        binding.btnSignupIdCheck.setOnClickListener {

            var id = binding.etSignupId.text.toString()

            viewModel.getUser(id)
            viewModel.user.observe(viewLifecycleOwner) { data ->
                doesIdExist = data
            }

            if (id.isEmpty()) {
                Toast.makeText(requireContext(), "ID를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (doesIdExist != null) {
                    Toast.makeText(requireContext(), "중복된 아이디가 있습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    idConfirm = true
                    Toast.makeText(requireContext(), "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show()
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

        binding.btnSignupSignup.setOnClickListener {

            var id = binding.etSignupId.text.toString()
            var pw = binding.etSignupPw.text.toString()
            var name = binding.etSignupName.text.toString()
            var email = binding.etSignupEmail.text.toString()
            var contact = binding.etSignupContact.text.toString()

            var isIdFilled = binding.etSignupId.text.isNotEmpty()
            var isPwFilled = binding.etSignupPw.text.isNotEmpty()
            var isPwCheckFilled = binding.etSignupPwCheck.text.isNotEmpty()
            var isNameFilled = binding.etSignupName.text.isNotEmpty()
            var isContactFilled = binding.etSignupContact.text.isNotEmpty()
            var isEmailFilled = binding.etSignupEmail.text.isNotEmpty()

            if (!isIdFilled || !isPwFilled || !isPwCheckFilled || !isNameFilled || !isContactFilled || !isEmailFilled
            ) {
                Toast.makeText(requireContext(), "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show()
            } else {
                if (!idConfirm) {
                    Toast.makeText(requireContext(), "ID 중복확인을 해주세요.", Toast.LENGTH_SHORT).show()
                } else if (!pwConfirm) {
                    Toast.makeText(requireContext(), "비밀번호 일치 여부를 확인해주세요.", Toast.LENGTH_SHORT)
                        .show()
                } else if (!privacyConfirm) {
                    Toast.makeText(requireContext(), "개인정보 수집/이용에 동의해주세요.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val user = User(id, pw, name, contact, email)
                    viewModel.addUser(user)
                    Toast.makeText(requireContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    // 로그인 페이지로 이동(SignInFragment)

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, SignInFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        binding.btnSignupCancel.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
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