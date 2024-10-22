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
import com.allergyguardian.allergyguardian.data.model.franchise.Menu
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseCategoryBinding
import com.allergyguardian.allergyguardian.presentation.FranchiseViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel
import com.allergyguardian.allergyguardian.presentation.franchise.franchise_detail.ARG_PARAM1
import com.allergyguardian.allergyguardian.presentation.franchise.franchise_detail.FranchiseDetailFragment

const val ARG_PARAM1 = "param1"

class FranchiseCategoryFragment : Fragment() {

    private var param1: String? = null
    private var _binding: FragmentFranchiseCategoryBinding? = null

    private var brands = listOf<String>()
    private var clickedBrand = ""
    private var clickedMenu = Menu()
    private var clickedBrandMenus = listOf<Menu>()

    private val categoryList = mutableListOf("카페", "패스트푸드", "베이커리/도넛", "아이스크림",
        "치킨", "피자", "샌드위치", "전체")

    private val fastfoodBrandList = mutableListOf("맥도날드", "롯데리아", "KFC", "맘스터치", "NBB버거")
    private val pizzaBrandList = mutableListOf("도미노피자", "피자헛", "미스터피자", "피자알볼로", "파파존스", "피자나라치킨공주", "반올림피자", "피자마루", "청년피자", "7번가피자")
    private val chickenBrandList = mutableListOf("피자나라치킨공주")
    private val cafeBrandList = mutableListOf("스타벅스", "투썸플레이스", "메가커피")
    private val icecreamBrandList = mutableListOf("서비스 준비중")
    private val bakeryDoughnutBrandList = mutableListOf("서비스 준비중")
    private val sandwichBrandList = mutableListOf("서비스 준비중")
    private val allBrandList = cafeBrandList+fastfoodBrandList+bakeryDoughnutBrandList+icecreamBrandList+chickenBrandList+pizzaBrandList+sandwichBrandList

    private val categoryBrandsList = mutableListOf(
        cafeBrandList,fastfoodBrandList, bakeryDoughnutBrandList, icecreamBrandList, chickenBrandList, pizzaBrandList, sandwichBrandList, allBrandList
    )

    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    private val franchiseViewModel: FranchiseViewModel by activityViewModels {
        viewModelFactory { initializer { FranchiseViewModel(requireActivity().application) } }
    }

    private val brandAdapter by lazy { BrandAdapter() }
    private val menuAdapter by lazy { MenuAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFranchiseCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(param1: String) =
            FranchiseCategoryFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        val clickedCategory = param1
        when (clickedCategory) {
//            "카페" -> franchiseViewModel.getCafeData()
            "패스트푸드" -> franchiseViewModel.getFastfoodMenus()
//            "피자" -> franchiseViewModel.getPizzaData()
//            "치킨" -> franchiseViewModel.getChickenData()
        }

        binding.recyclerviewFranchises.adapter = brandAdapter
        binding.recyclerviewFranchises.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerviewMenus.adapter = menuAdapter
        binding.recyclerviewMenus.layoutManager = LinearLayoutManager(requireContext())

        binding.tvCategoryTitle.text = clickedCategory

        val index = categoryList.indexOf(clickedCategory)
        brands = categoryBrandsList[index]
        brandAdapter.submitList(brands)
        brandAdapter.itemClick = object : BrandAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                clickedBrand = brands[position]
                when (clickedCategory) {
                    "패스트푸드" -> {
                        franchiseViewModel.fastfoodMenus.observe(viewLifecycleOwner) { data ->
                            clickedBrandMenus = data.filter { it.brand == clickedBrand }
                            menuAdapter.submitList(clickedBrandMenus)
                            binding.tvMenuCount.text = clickedBrandMenus.size.toString()
                        }
                    }
                    "카페" -> {
                        franchiseViewModel.cafeMenus.observe(viewLifecycleOwner) { data ->
                            clickedBrandMenus = data.filter { it.brand == clickedBrand }
                            menuAdapter.submitList(clickedBrandMenus)
                            binding.tvMenuCount.text = clickedBrandMenus.size.toString()
                        }
                    }
                    "피자" -> {
                        franchiseViewModel.pizzaMenus.observe(viewLifecycleOwner) { data ->
                            clickedBrandMenus = data.filter { it.brand == clickedBrand }
                            menuAdapter.submitList(clickedBrandMenus)
                            binding.tvMenuCount.text = clickedBrandMenus.size.toString()
                        }
                    }
                    "치킨" -> {
                        franchiseViewModel.chickenMenus.observe(viewLifecycleOwner) { data ->
                            clickedBrandMenus = data.filter { it.brand == clickedBrand }
                            menuAdapter.submitList(clickedBrandMenus)
                            binding.tvMenuCount.text = clickedBrandMenus.size.toString()
                        }
                    }
                }
            }
        }

        menuAdapter.itemClick = object : MenuAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                clickedMenu = clickedBrandMenus[position]
                val dataToSend = clickedMenu.id
                val menuDetail = FranchiseDetailFragment.newInstance(dataToSend)
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    hide(this@FranchiseCategoryFragment)
                    show(menuDetail)
                    addToBackStack(null)
                    commit()
                }
            }
        }



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