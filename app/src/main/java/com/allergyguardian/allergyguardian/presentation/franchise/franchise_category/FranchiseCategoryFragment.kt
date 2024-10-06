package com.allergyguardian.allergyguardian.presentation.franchise.franchise_category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseCategoryBinding
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseHomeBinding
import com.allergyguardian.allergyguardian.presentation.UserViewModel
import com.allergyguardian.allergyguardian.presentation.franchise.franchise_home.FranchiseHomeAdapter

class FranchiseCategoryFragment : Fragment() {

    private var _binding: FragmentFranchiseCategoryBinding? = null

    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    private val brandAdapter by lazy { BrandAdapter() }
    private val menuAdapter by lazy { MenuAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFranchiseCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewFranchises.adapter = brandAdapter
        binding.recyclerviewFranchises.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerviewMenus.adapter = menuAdapter
        binding.recyclerviewMenus.layoutManager = LinearLayoutManager(requireContext())


//        val itemListFragment = requireActivity().supportFragmentManager.findFragmentByTag("ItemListFragment")
//        binding.btnHomeSearch.setOnClickListener() {
//            viewModel.setSearchKeyword(binding.etHomeSearch.text.toString())
//            requireActivity().supportFragmentManager.beginTransaction().apply {
//                hide(this@HomeFragment)
//                if (itemListFragment == null) {
//                    add(R.id.main_frame, ItemListFragment(), "ItemListFragment")
//                } else {
//                    show(itemListFragment)
//                }
//                addToBackStack(null)
//                commit()
//            }
//        }
//
//        binding.etHomeSearch.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                viewModel.setSearchKeyword(binding.etHomeSearch.text.toString())
//                requireActivity().supportFragmentManager.beginTransaction().apply {
//                    hide(this@HomeFragment)
//                    if (itemListFragment == null) {
//                        add(R.id.main_frame, ItemListFragment(), "ItemListFragment")
//                    } else {
//                        show(itemListFragment)
//                    }
//                    addToBackStack(null)
//                    commit()
//                }
//                true // 이벤트 처리가 완료되었음을 나타냄
//            } else {
//                false
//            }
//        }

//        val myPageFragment = requireActivity().supportFragmentManager.findFragmentByTag("MyPageFragment")
//        binding.btnTabMypage.setOnClickListener{
//            requireActivity().supportFragmentManager.beginTransaction().apply {
//                hide(this@HomeFragment)
//                if (myPageFragment == null) {
//                    add(R.id.main_frame, MyPageFragment(), "MyPageFragment")
//                } else if (myPageFragment != null && myPageFragment.isHidden){
//                    show(myPageFragment)
//                }
//                addToBackStack(null)
//                commit()
//            }
//        }
//
//        val communityHomeFragment = requireActivity().supportFragmentManager.findFragmentByTag("CommunityHomeFragment")
//        binding.btnTabCommunity.setOnClickListener{
//            requireActivity().supportFragmentManager.beginTransaction().apply {
//                hide(this@HomeFragment)
//                if (communityHomeFragment == null) {
//                    add(R.id.main_frame, CommunityHomeFragment(), "CommunityHomeFragment")
//                } else if (communityHomeFragment != null && communityHomeFragment.isHidden){
//                    show(communityHomeFragment)
//                }
//                addToBackStack(null)
//                commit()
//            }
//        }
    }
}