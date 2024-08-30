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
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentHomeBinding
import com.example.allergydetective.presentation.SharedViewModel
import com.example.allergydetective.presentation.base.UiState
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

    private val homeAdapter by lazy { HomeAdapter() }

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
//        initView()
//        initViewModel()

        binding.homeRecyclerList.adapter = homeAdapter
        binding.homeRecyclerList.layoutManager = LinearLayoutManager(requireContext())

        binding.progress.isVisible = false

        viewModel.getHomeFoods()
        viewModel.homeFoods.observe(viewLifecycleOwner) { homeFoods ->
            homeAdapter.submitList(homeFoods)
        }

        val blinkAnimation = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 1000 // 애니메이션 실행 시간 (0.5초)
            repeatMode = Animation.REVERSE // 애니메이션을 반대로 반복
            repeatCount = Animation.INFINITE // 무한 반복
        }

        viewModel.selectedCategory.observe(viewLifecycleOwner) { data ->
            if (data == null) {
                binding.btnHomeFilter.startAnimation(blinkAnimation)
            } else {
                binding.btnHomeSearch.startAnimation(blinkAnimation)
            }
        }

        homeAdapter.itemClick = object : HomeAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, ItemDetailFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        val filterFragment = fragmentManager?.findFragmentByTag("FilterFragment")
        binding.btnHomeFilter.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(HomeFragment())
                if (filterFragment == null) {
                    add(R.id.main_frame, FilterFragment(), "FilterFragment")
                } else {
                    show(FilterFragment())
                }
                addToBackStack(null)
                commit()
            }
        }
        val itemListFragment = fragmentManager?.findFragmentByTag("ItemListFragment")
        binding.btnHomeSearch.setOnClickListener() {
            if (viewModel.selectedCategory.value == null) {
                Toast.makeText(requireContext(), "카테고리를 1가지 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.setSearchKeyword(binding.etHomeSearch.text.toString())
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    hide(HomeFragment())
                    if (itemListFragment == null) {
                        add(R.id.main_frame, ItemListFragment(), "ItemListFragment")
                    } else {
                        show(ItemListFragment())
                    }
                    addToBackStack(null)
                    commit()
                }
            }
        }
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