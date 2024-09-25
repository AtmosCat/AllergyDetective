package com.allergyguardian.allergyguardian.presentation.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.databinding.FragmentHomeBinding
import com.allergyguardian.allergyguardian.databinding.FragmentPrivacyTermsBinding

class PrivacyTermsFragment : Fragment() {

    private var _binding: FragmentPrivacyTermsBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrivacyTermsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

}