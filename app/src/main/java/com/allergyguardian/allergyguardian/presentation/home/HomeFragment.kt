package com.allergyguardian.allergyguardian.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.repository.food.GonggongFoodRepositoryImpl
import com.allergyguardian.allergyguardian.data.repository.market.MarketRepositoryImpl
import com.allergyguardian.allergyguardian.databinding.FragmentHomeBinding
import com.allergyguardian.allergyguardian.presentation.SharedViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel
import com.allergyguardian.allergyguardian.presentation.base.UiState
import com.allergyguardian.allergyguardian.presentation.community.community_home.CommunityHomeFragment
import com.allergyguardian.allergyguardian.presentation.filter.FilterFragment
import com.allergyguardian.allergyguardian.presentation.itemlist.ItemListFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels() {
        viewModelFactory {
            initializer {
                SharedViewModel(
                    GonggongFoodRepositoryImpl(),
                    MarketRepositoryImpl()
                )
            }
        }
    }
    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }


    private val homeAdapter by lazy { HomeAdapter(userViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllFoods()

        binding.homeRecyclerList.adapter = homeAdapter
        binding.homeRecyclerList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    Toast.makeText(requireContext(), "1만여 개의 푸드 데이터를 로딩중입니다.", Toast.LENGTH_LONG).show()
                    Toast.makeText(requireContext(), "데이터가 많아요. 조금만 더 기다려주세요!", Toast.LENGTH_LONG).show()
                    binding.progress.isVisible = true
                }
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "데이터 로딩 완료!", Toast.LENGTH_SHORT).show()
                    binding.progress.isVisible = false
                    viewModel.totalFoods.observe(viewLifecycleOwner) { data ->
                        val indexes = listOf(0,1,2,3,4,5,6,7,8,9)
                        val homeFoodsData = indexes.map { index -> data[index] }
                        homeAdapter.submitList(homeFoodsData)
                    }
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG).show()
                }
//                else -> {}
            }
        }

        val blinkAnimation = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 1000 // 애니메이션 실행 시간 (0.5초)
            repeatMode = Animation.REVERSE // 애니메이션을 반대로 반복
            repeatCount = Animation.INFINITE // 무한 반복
        }

//        viewModel.selectedCategories.observe(viewLifecycleOwner) { data ->
//            if (data.isNotEmpty()) {
//                binding.btnHomeSearch.startAnimation(blinkAnimation)
//            }
//        }

//        val itemDetailFragment = requireActivity().supportFragmentManager.findFragmentByTag("ItemDetailFragment")
//        homeAdapter.itemClick = object : HomeAdapter.ItemClick {
//            override fun onClick(view: View, position: Int) {
//                requireActivity().supportFragmentManager.beginTransaction().apply {
//                    hide(this@HomeFragment)
//                    if (itemDetailFragment == null) {
//                        add(R.id.main_frame, ItemDetailFragment(), "ItemDetailFragment")
//                    } else {
//                        show(itemDetailFragment)
//                    }
//                    addToBackStack(null)
//                    commit()
//                }
//            }
//        }

        val filterFragment = requireActivity().supportFragmentManager.findFragmentByTag("FilterFragment")
        binding.btnHomeFilter.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
//                hide(this@HomeFragment)
                if (filterFragment == null) {
                    add(R.id.main_frame, FilterFragment(), "FilterFragment")
                } else {
                    show(filterFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val itemListFragment = requireActivity().supportFragmentManager.findFragmentByTag("ItemListFragment")
        binding.btnHomeSearch.setOnClickListener() {
            viewModel.setSearchKeyword(binding.etHomeSearch.text.toString())
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@HomeFragment)
                if (itemListFragment == null) {
                    add(R.id.main_frame, ItemListFragment(), "ItemListFragment")
                } else {
                    show(itemListFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        binding.etHomeSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setSearchKeyword(binding.etHomeSearch.text.toString())
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    hide(this@HomeFragment)
                    if (itemListFragment == null) {
                        add(R.id.main_frame, ItemListFragment(), "ItemListFragment")
                    } else {
                        show(itemListFragment)
                    }
                    addToBackStack(null)
                    commit()
                }
                true // 이벤트 처리가 완료되었음을 나타냄
            } else {
                false
            }
        }

        val myPageFragment = requireActivity().supportFragmentManager.findFragmentByTag("MyPageFragment")
        binding.btnTabMypage.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@HomeFragment)
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
                hide(this@HomeFragment)
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
