package com.allergyguardian.allergyguardian.presentation.franchise.franchise_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.allergyguardian.allergyguardian.data.model.franchise.Menu
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseDetailBinding
import com.allergyguardian.allergyguardian.presentation.FranchiseViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel

const val ARG_PARAM1 = "param1"
class FranchiseDetailFragment : Fragment() {
    private var param1: String? = null

    private var _binding: FragmentFranchiseDetailBinding? = null
    private val binding get() = _binding!!

    private var clickedItem = Menu()

    private val franchiseViewModel: FranchiseViewModel by activityViewModels {
        viewModelFactory { initializer { FranchiseViewModel(requireActivity().application) } }
    }

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFranchiseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(param1: String) = FranchiseDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = param1


    }

}
