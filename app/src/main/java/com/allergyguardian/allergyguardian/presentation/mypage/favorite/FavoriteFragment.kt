package com.allergyguardian.allergyguardian.presentation.mypage.favorite

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
import com.allergyguardian.allergyguardian.data.model.food.Food
import com.allergyguardian.allergyguardian.data.repository.food.GonggongFoodRepositoryImpl
import com.allergyguardian.allergyguardian.data.repository.market.MarketRepositoryImpl
import com.allergyguardian.allergyguardian.databinding.FragmentFavoriteBinding
import com.allergyguardian.allergyguardian.presentation.SharedViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel
import com.allergyguardian.allergyguardian.presentation.itemdetail.ItemDetailFragment

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var clickedItem: Food? = null

    private val sharedViewModel: SharedViewModel by activityViewModels() {
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

    private val favoriteListAdapter by lazy { FavoriteListAdapter(userViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 어댑터 만들고, User에서 like -> List<Food>로 관리하는 거 구현
        binding.recyclerviewFavoriteList.adapter = favoriteListAdapter
        binding.recyclerviewFavoriteList.layoutManager = LinearLayoutManager(requireContext())

        var currentUserFavorites = mutableListOf<Food>()

        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                currentUserFavorites = data.like
                favoriteListAdapter.submitList(currentUserFavorites)
            } else {
                currentUserFavorites = emptyList<Food>().toMutableList()
                Toast.makeText(requireContext(), "좋아요한 상품이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnUpdate.setOnClickListener{
            favoriteListAdapter.updateData(currentUserFavorites)
        }

        favoriteListAdapter.itemClick = object :FavoriteListAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
                    if (data != null) {
                        currentUserFavorites = data.like
                        clickedItem = currentUserFavorites[position]

                        val itemDetailFragment = requireActivity().supportFragmentManager.findFragmentByTag("ItemDetailFragment")
                        val dataToSend = clickedItem!!.prdlstReportNo
                        val itemDetail = ItemDetailFragment.newInstance(dataToSend)
                        requireActivity().supportFragmentManager.beginTransaction().apply {
                            hide(this@FavoriteFragment)
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
        }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        var updatedCurrentUserFavorites = mutableListOf<Food>()
        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                updatedCurrentUserFavorites = data.like
                favoriteListAdapter.updateData(updatedCurrentUserFavorites)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        var updatedCurrentUserFavorites = mutableListOf<Food>()
        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                updatedCurrentUserFavorites = data.like
                favoriteListAdapter.updateData(updatedCurrentUserFavorites)
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