package com.allergyguardian.allergyguardian.presentation.franchise.franchise_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseHomeBinding
import com.allergyguardian.allergyguardian.presentation.FranchiseViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel
import com.allergyguardian.allergyguardian.presentation.base.UiState
import com.allergyguardian.allergyguardian.presentation.franchise.franchise_category.FranchiseCategoryFragment
import com.allergyguardian.allergyguardian.presentation.home.HomeFragment

class FranchiseHomeFragment : Fragment() {

    private var _binding: FragmentFranchiseHomeBinding? = null

    private val categoryList = mutableListOf("카페", "패스트푸드", "베이커리/도넛", "아이스크림",
        "치킨", "피자", "샌드위치", "전체")

    private var categoryButtonList = emptyList<View>()

    private var isAllMenusLoaded = false

    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    private val franchiseViewModel: FranchiseViewModel by activityViewModels {
        viewModelFactory { initializer { FranchiseViewModel(requireActivity().application) } }
    }

    private val franchiseHomeAdapter by lazy { FranchiseHomeAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFranchiseHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isAllMenusLoaded) {
            franchiseViewModel.getAllMenus()
            isAllMenusLoaded = true
        }

        franchiseViewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    Toast.makeText(requireContext(), "데이터를 로딩중입니다.", Toast.LENGTH_SHORT).show()
                    binding.progress.isVisible = true
                }
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "데이터 로딩 완료!", Toast.LENGTH_SHORT).show()
                    binding.progress.isVisible = false
                    franchiseViewModel.allMenus.observe(viewLifecycleOwner) { data ->
                        franchiseHomeAdapter.submitList(data)
                    }
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG).show()
                }
//                else -> {}
            }
        }

        binding.recyclerviewFranchiseHome.adapter = franchiseHomeAdapter
        binding.recyclerviewFranchiseHome.layoutManager = LinearLayoutManager(requireContext())

        categoryButtonList = listOf(
            binding.btnCoffee,
            binding.btnFastfood,
            binding.btnBreadDoughnut,
            binding.btnIcecream,
            binding.btnChicken,
            binding.btnPizza,
            binding.btnSandwich,
            binding.btnAll
        )

        val franchiseCategoryFragmentNull = requireActivity().supportFragmentManager.findFragmentByTag("FranchiseCategoryFragment")
        categoryButtonList.forEach { it ->
            it.setOnClickListener{
                val index = categoryButtonList.indexOf(it)
                val dataToSend = categoryList[index]
                val franchiseCategoryFragment = FranchiseCategoryFragment.newInstance(dataToSend)
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    hide(this@FranchiseHomeFragment)
//                    if (franchiseCategoryFragmentNull == null) {
                        add(R.id.main_frame, franchiseCategoryFragment, "FranchiseCategoryFragment")
//                    } else {
//                        show(franchiseCategoryFragment)
//                    }
                    addToBackStack(null)
                    commit()
                }
            }
        }


//
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

    private fun categoryClicker(button: Button){

    }

}