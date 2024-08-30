package com.example.allergydetective.presentation.signin.findId

import com.example.allergydetective.presentation.UserViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.allergydetective.databinding.FragmentFindIdBinding

class FindIdFragment : Fragment() {

    private var _binding: FragmentFindIdBinding? = null

    private val binding get() = _binding!!

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
        _binding = FragmentFindIdBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        var name = binding.etFindIdName.text.toString()
//        var contact = binding.etFindIdContact.text.toString()
//
//        var userFoundByName = com.example.allergydetective.data.model.user.UserManager.users.find { it.name == name.toString() }
//        var userFoundByContact = com.example.allergydetective.data.model.user.UserManager.users.find { it.contact == contact.toString() }
//
//        binding.btnFindId.setOnClickListener{
//            if (userFoundByName != null && userFoundByName == userFoundByContact) {
//                Toast.makeText(requireContext(), "아이디: ${userFoundByName.id}", Toast.LENGTH_SHORT).show()
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



