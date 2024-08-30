package com.example.allergydetective.presentation.itemdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.allergydetective.R
import com.example.allergydetective.data.model.market.Market
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentItemDetailBinding
import com.example.allergydetective.presentation.SharedViewModel

private const val ARG_PARAM1 = "param1"
class ItemDetailFragment : Fragment() {
    private var param1: String? = null

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    // 이렇게 뷰모델 호출하는 거 맞나?
    private val viewModel: SharedViewModel by activityViewModels {
        viewModelFactory {
            initializer {
                SharedViewModel(
                    GonggongFoodRepositoryImpl(),
                    MarketRepositoryImpl()
                )
            }
        }
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
        val clickedItem = viewModel.filteredFoods.value?.find { it.prdlstReportNo == data }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val imageResources =
            listOf(clickedItem?.imgurl1.toString(), clickedItem?.imgurl2.toString())

        val viewPagerAdapter = ViewPagerAdapter(imageResources)
        viewPager.adapter = viewPagerAdapter

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
            binding.tvPrdlstReportNo.text = "- 품목보고번호: ${clickedItem?.prdlstReportNo.toString()}"
        }

        viewModel.getMarketDetail(clickedItem?.manufacture.toString() + " " +clickedItem?.prdlstNm.toString())

        var marketList: List<Market>?

        viewModel.marketData.observe(viewLifecycleOwner) { data ->
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
                    val url = marketList!![position].link.toString()
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }

        }

    }
}

//    private fun initViewModel() {
//        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
//            when (uiState) {
//                is UiState.Loading -> {
//                    binding.progress.isVisible = true
//                }
//
//                is UiState.Success -> {
//                    binding.progress.isVisible = false
//                }
//
//                is UiState.Error -> {
//                    Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        }
////        viewModel.setFilter()
//    }
