package com.allergyguardian.allergyguardian.presentation.signin.findPw

import com.allergyguardian.allergyguardian.presentation.UserViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.allergyguardian.allergyguardian.databinding.FragmentFindPwBinding

class FindPwFragment : Fragment() {

    private var _binding: FragmentFindPwBinding? = null

    private val binding get() = _binding!!

    // 이렇게 뷰모델 호출하는 거 맞나?
    private val viewModel: UserViewModel by activityViewModels {
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
        _binding = FragmentFindPwBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        var name = binding.etFindPwName.text.toString()
//        var id = binding.etFindPwId.text.toString()
//        var email = binding.etFindPwEmail.text.toString()
//
//        var userFoundByName = com.example.allergydetective.data.model.user.UserManager.users.find { it.name == name.toString() }
//        var userFoundById = com.example.allergydetective.data.model.user.UserManager.users.find { it.id == id.toString() }
//        var userFoundByEmail = com.example.allergydetective.data.model.user.UserManager.users.find { it.email == email.toString() }
//
//        binding.btnFindPw.setOnClickListener{
//            if (userFoundByName != null && userFoundByName == userFoundById && userFoundById == userFoundByEmail) {
//                // 회원 메일로 비밀번호 변경 코드 전송
//            } else {
//                Toast.makeText(requireContext(), "일치하는 회원 정보가 없습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    }
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
