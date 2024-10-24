package com.allergyguardian.allergyguardian.presentation.franchise.franchise_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseHomeBinding
import com.allergyguardian.allergyguardian.presentation.FranchiseViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel
import com.allergyguardian.allergyguardian.presentation.base.UiState
import com.allergyguardian.allergyguardian.presentation.community.community_home.CommunityHomeFragment
import com.allergyguardian.allergyguardian.presentation.franchise.franchise_category.FranchiseCategoryFragment
import com.allergyguardian.allergyguardian.presentation.home.MyPageFragment

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
                    binding.progress.visibility = View.VISIBLE
                    binding.viewLoadingBackground.visibility = View.VISIBLE
                    binding.tvLoading.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.progress.visibility = View.GONE
                    binding.viewLoadingBackground.visibility = View.GONE
                    binding.tvLoading.visibility = View.GONE
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
//            binding.btnAll
        )

        categoryButtonList.forEach { it ->
            it.setOnClickListener{
                val index = categoryButtonList.indexOf(it)
                val dataToSend = categoryList[index]
                val franchiseCategoryFragment = FranchiseCategoryFragment.newInstance(dataToSend)
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    hide(this@FranchiseHomeFragment)
                    add(R.id.main_frame, franchiseCategoryFragment, "FranchiseCategoryFragment")
                    addToBackStack(null)
                    commit()
                }
            }
        }

        val myPageFragment = requireActivity().supportFragmentManager.findFragmentByTag("MyPageFragment")
        binding.btnTabMypage.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@FranchiseHomeFragment)
                if (myPageFragment == null) {
                    add(R.id.main_frame, MyPageFragment(), "MyPageFragment")
                } else if (myPageFragment != null && myPageFragment.isHidden){
                    show(myPageFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val communityHomeFragment = requireActivity().supportFragmentManager.findFragmentByTag("CommunityHomeFragment")
        binding.btnTabCommunity.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@FranchiseHomeFragment)
                if (communityHomeFragment == null) {
                    add(R.id.main_frame, CommunityHomeFragment(), "CommunityHomeFragment")
                } else if (communityHomeFragment != null && communityHomeFragment.isHidden){
                    show(communityHomeFragment)
                }
                addToBackStack(null)
                commit()
            }
        }
    }

}