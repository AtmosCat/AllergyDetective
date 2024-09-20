package com.example.allergydetective.presentation.home

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Filter
import android.widget.Toast
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
//import coil.load
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allergydetective.R
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentHomeBinding
import com.example.allergydetective.presentation.SharedViewModel
import com.example.allergydetective.presentation.UserViewModel
import com.example.allergydetective.presentation.base.UiState
import com.example.allergydetective.presentation.community.community_home.CommunityHomeFragment
import com.example.allergydetective.presentation.filter.FilterFragment
import com.example.allergydetective.presentation.itemdetail.ItemDetailFragment
import com.example.allergydetective.presentation.itemlist.ItemListFragment

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
                    Toast.makeText(requireContext(), "데이터를 로딩중입니다.", Toast.LENGTH_LONG).show()
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

//        var homeFoodsData = listOf<Food>()
//        viewModel.totalFoods.observe(viewLifecycleOwner) { totalFoods ->
//            val indexes = listOf(0,1,2,3,4,5,6,7,8,9)
//            homeFoodsData = indexes.map { index -> totalFoods[index] }
//            homeAdapter.submitList(homeFoodsData)
//        }

        val blinkAnimation = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 1000 // 애니메이션 실행 시간 (0.5초)
            repeatMode = Animation.REVERSE // 애니메이션을 반대로 반복
            repeatCount = Animation.INFINITE // 무한 반복
        }

        viewModel.selectedCategories.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                binding.btnHomeSearch.startAnimation(blinkAnimation)
            }
        }

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



//        viewModel.prdkinds.observe(viewLifecycleOwner) { data ->
//            for (i in data) {
//                if (i !in prdkinds) {
//                    prdkinds.add(i+"\n")
//                }
//            }
//        }





    }
}



//    private fun initView() = with(binding) {
//        homeRecyclerList.adapter = homeAdapter
//        homeRecyclerList.layoutManager = LinearLayoutManager(requireContext())
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
//                    // homeFoods LiveData를 관찰하여 RecyclerView에 데이터 전달
//                    viewModel.homeFoods.observe(viewLifecycleOwner) { homeFoods ->
//                        homeAdapter.submitList(homeFoods)
//                    }
//                }
//
//                is UiState.Error -> {
//                    Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//}