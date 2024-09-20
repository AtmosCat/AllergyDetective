package com.example.allergydetective.presentation.itemdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.allergydetective.R
import com.example.allergydetective.data.model.food.Food
import com.example.allergydetective.data.model.market.Market
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentItemDetailBinding
import com.example.allergydetective.presentation.SharedViewModel
import com.example.allergydetective.presentation.UserViewModel

private const val ARG_PARAM1 = "param1"
class ItemDetailFragment : Fragment() {
    private var param1: String? = null

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    private var clickedItem = Food()

    // 이렇게 뷰모델 호출하는 거 맞나?
    private val sharedViewModel: SharedViewModel by activityViewModels {
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
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(param1: String) =
            ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = param1
        sharedViewModel.filteredFoods.observe(viewLifecycleOwner) { filteredFoods ->
            clickedItem = filteredFoods.find { it.prdlstReportNo == data }!!

            binding.btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

            val imageResources =
                listOf(clickedItem?.imgurl1.toString(), clickedItem?.imgurl2.toString())

            val viewPagerAdapter = ViewPagerAdapter(imageResources)
            viewPager.adapter = viewPagerAdapter

            var isLiked = false
            var currentUserFavorites = mutableListOf<Food>()
            userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
                if (data != null) {
                    currentUserFavorites = data.like
                    if (clickedItem in currentUserFavorites) {
                        binding.btnLike.setImageResource(R.drawable.filled_heart)
                        isLiked = true
                    }
                }
            }

            binding.btnLike.setOnClickListener {
                if (!isLiked) {
                    isLiked = true
                    binding.btnLike.setImageResource(R.drawable.filled_heart)
                    userViewModel.currentUser.value?.like!!.add(clickedItem)
                    userViewModel.updateCurrentUserInfo()
                } else {
                    isLiked = false
                    binding.btnLike.setImageResource(R.drawable.heart)
                    userViewModel.currentUser.value?.like!!.remove(clickedItem)
                    userViewModel.updateCurrentUserInfo()
                }
            }

            binding.tvCategory.text = clickedItem?.prdkind.toString()
            binding.tvName.text = clickedItem?.prdlstNm.toString()
            binding.tvAllergy.text = "⛔ 알러지유발물질: ${clickedItem?.allergy.toString()}"
            if (clickedItem?.manufacture == "null") {
                binding.tvManufacture.text = "- 제조원: 정보없음"
            } else {
                binding.tvManufacture.text = "- 제조원: ${clickedItem?.manufacture.toString()}"
            }
            if (clickedItem?.seller == "null") {
                binding.tvSeller.text = "- 판매원: 정보없음"
            } else {
                binding.tvSeller.text = "- 판매원: ${clickedItem?.seller.toString()}"
            }
            if (clickedItem?.rawmtrl == "null") {
                binding.tvRawmtrl.text = "- 원재료: 정보없음"
            } else {
                binding.tvRawmtrl.text = "- 원재료: ${clickedItem?.rawmtrl.toString()}"
            }
            if (clickedItem?.nutrient == "null") {
                binding.tvNutrient.text = "- 영양성분: 정보없음"
            } else {
                binding.tvNutrient.text = "- 영양성분: ${clickedItem?.nutrient.toString()}"
            }
            if (clickedItem?.prdlstReportNo == "null") {
                binding.tvPrdlstReportNo.text = "- 품목보고번호: 정보없음"
            } else {
                binding.tvPrdlstReportNo.text =
                    "- 품목보고번호: ${clickedItem?.prdlstReportNo.toString()}"
            }

            sharedViewModel.getMarketDetail(clickedItem?.manufacture.toString(), clickedItem?.prdlstNm.toString())
        }

        var marketList: List<Market>?

        sharedViewModel.marketData.observe(viewLifecycleOwner) { data ->
            marketList = data

            if (marketList!!.isEmpty()) {
                binding.tvNoResultsMessage.isVisible = true
            } else {
                binding.tvNoResultsMessage.isVisible = false
            }

            val shoppingListAdapter = ShoppingMallAdapter(marketList)
            binding.recyclerviewShoppingMalls.adapter = shoppingListAdapter
            binding.recyclerviewShoppingMalls.layoutManager = LinearLayoutManager(requireContext())

            shoppingListAdapter.itemClick = object : ShoppingMallAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    Toast.makeText(requireContext(), "외부 쇼핑몰 링크로 이동합니다.", Toast.LENGTH_SHORT).show()
                    val url = marketList!![position].link.toString()
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }

        }
    }
}

