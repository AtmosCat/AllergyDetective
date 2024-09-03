package com.example.allergydetective.presentation.itemlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
//import coil.load
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allergydetective.R
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentItemListBinding
import com.example.allergydetective.presentation.SharedViewModel
import com.example.allergydetective.presentation.base.UiState
import com.example.allergydetective.presentation.filter.FilterFragment
import com.example.allergydetective.presentation.home.HomeFragment
import com.example.allergydetective.presentation.home.MyPageFragment
import com.example.allergydetective.presentation.itemdetail.ItemDetailFragment

private const val ARG_PARAM1 = "param1"
class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null

    private var sortedList: List<Food>? = emptyList()

    private var clickedItem: Food? = null

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

    private val itemListAdapter by lazy { ItemListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewItemlist.adapter = itemListAdapter
        binding.recyclerviewItemlist.layoutManager = LinearLayoutManager(requireContext())

        binding.progress.isVisible = false
        viewModel.getFilteredFoods()
        viewModel.filteredFoods.observe(viewLifecycleOwner) { filteredFoods ->
            itemListAdapter.submitList(filteredFoods)
        }

        binding.btnItemlistSearch.setOnClickListener() {
            if (viewModel.selectedCategory.value == null) {
                Toast.makeText(requireContext(), "카테고리를 1가지 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.setSearchKeyword(binding.etItemlistSearch.text.toString())
                viewModel.getFilteredFoods()
                viewModel.filteredFoods.observe(viewLifecycleOwner){data ->
                    itemListAdapter.updateData(data)
                }
            }
        }

        val blinkAnimation = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 1000 // 애니메이션 실행 시간 (0.5초)
            repeatMode = Animation.REVERSE // 애니메이션을 반대로 반복
            repeatCount = Animation.INFINITE // 무한 반복
        }

        viewModel.selectedCategory.observe(viewLifecycleOwner) { data ->
            if (data == null) {
                binding.btnItemlistFilter.startAnimation(blinkAnimation)
            } else {
                binding.btnItemlistSearch.startAnimation(blinkAnimation)
            }
        }


        val filterFragment = requireActivity().supportFragmentManager.findFragmentByTag("FilterFragment")
        binding.btnItemlistFilter.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@ItemListFragment)
                if (filterFragment == null) {
                    add(R.id.main_frame, FilterFragment(), "FilterFragment")
                } else {
                    show(filterFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        viewModel.selectedAllergies.observe(viewLifecycleOwner) { data ->
            if (data.size == 1) {
                binding.tvFilteredAllergy.text = "✅ 필터: ${viewModel.selectedAllergies.value!![0]}"
            } else if (data.size == 2) {
                binding.tvFilteredAllergy.text =
                "✅ 필터: ${viewModel.selectedAllergies.value!![0]} / ${viewModel.selectedAllergies.value!![1]}"
            } else if (data.size == 0) {
                binding.tvFilteredAllergy.text = "✅ 필터: 없음"
            }
        }

        val spinnerItems = listOf("기본순 ▼", "가나다순 ▼", "인기순 ▼", "추천순 ▼", "조회수순 ▼", "가격순 ▼")
        val spinnerAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_layout_custom, spinnerItems)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_list_layout_custom)
        binding.btnSpinner.adapter = spinnerAdapter

        binding.btnSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sortedList = when (position) {
                    1 -> viewModel.filteredFoods.value?.sortedBy { it.prdlstNm } // 이름순으로 정렬
                    2 -> viewModel.filteredFoods.value?.sortedBy { it.like } // 좋아요순으로 정렬
                    else -> viewModel.filteredFoods.value
                }

                // RecyclerView 어댑터에 새로 정렬된 데이터 설정
                if (sortedList != null) {
                    itemListAdapter.updateData(sortedList!!)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        val itemDetailFragment = requireActivity().supportFragmentManager.findFragmentByTag("ItemDetailFragment")

        itemListAdapter.itemClick = object : ItemListAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                viewModel.filteredFoods.observe(viewLifecycleOwner) { data ->
                    if (sortedList == null) {
                        clickedItem = data[position]
                    } else {
                        clickedItem = sortedList!![position]
                    }
                    val dataToSend = clickedItem!!.prdlstReportNo
                    val itemDetail = ItemDetailFragment.newInstance(dataToSend)
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        hide(this@ItemListFragment)
                        if (itemDetailFragment == null) {
                            add(R.id.main_frame, itemDetail, "ItemDetailFragment")
                        } else {
                            show(itemDetail)
                        }
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

        val homeFragment = requireActivity().supportFragmentManager.findFragmentByTag("HomeFragment")
        binding.btnTabHome.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@ItemListFragment)
                if (homeFragment == null) {
                    add(R.id.main_frame, HomeFragment(), "HomeFragment")
                } else {
                    show(homeFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val myPageFragment = requireActivity().supportFragmentManager.findFragmentByTag("MyPageFragment")
        binding.btnTabMypage.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@ItemListFragment)
                if (myPageFragment == null) {
                    add(R.id.main_frame, MyPageFragment(), "MyPageFragment")
                } else {
                    show(myPageFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

    }
}

//    private fun initView() = with(binding) {
//        recyclerviewItemlist.adapter = itemListAdapter
//        recyclerviewItemlist.layoutManager = LinearLayoutManager(requireContext())
//    }
//
//    private fun initViewModel() {
//        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
//            when (uiState) {
//                is UiState.Loading -> {
//                    binding.progress.isVisible = true
//                }
//
//                is UiState.Success -> {
//                    binding.progress.isVisible = false
//                    viewModel.filteredFoods.observe(viewLifecycleOwner) { filteredFoods ->
//                        itemListAdapter.submitList(filteredFoods)
//                    }
//                }
//
//                is UiState.Error -> {
//                    Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//        viewModel.getFilteredFoods()
//    }
