package com.example.allergydetective.presentation.filter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Switch
import android.widget.Toast
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.allergydetective.R
import com.example.allergydetective.data.model.food.FoodManager
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentFilterBinding
import com.example.allergydetective.presentation.SharedViewModel
import com.example.allergydetective.presentation.base.UiState
import com.example.allergydetective.presentation.home.HomeFragment
import com.example.allergydetective.presentation.itemlist.ItemListAdapter
import com.example.allergydetective.presentation.itemlist.ItemListFragment

class FilterFragment : Fragment() {

    private var _binding : FragmentFilterBinding? = null

    private val binding get() = _binding!!
    // 이렇게 뷰모델 호출하는 거 맞나?
    private val viewModel: SharedViewModel by activityViewModels {
        viewModelFactory { initializer { SharedViewModel(GonggongFoodRepositoryImpl(), MarketRepositoryImpl()) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initViewModel()

        val category1 = binding.switch1
        val category2 = binding.switch2
        val category3 = binding.switch3
        val category4 = binding.switch4
        val category5 = binding.switch5
        val category6 = binding.switch6
        val category7 = binding.switch7
        val category8 = binding.switch8
        val category9 = binding.switch9
        val category10 = binding.switch10
        val category11 = binding.switch11
        val category12 = binding.switch12

        var categories = mutableListOf(category1, category2, category3, category4,
            category5, category6, category7, category8, category9, category10, category11, category12 )

        when (viewModel.selectedCategory.value.toString()) {
            category1.text.toString() -> category1.isChecked = true
            category2.text.toString() -> category2.isChecked = true
            category3.text.toString() -> category3.isChecked = true
            category4.text.toString() -> category4.isChecked = true
            category5.text.toString() -> category5.isChecked = true
            category6.text.toString() -> category6.isChecked = true
            category7.text.toString() -> category7.isChecked = true
            category8.text.toString() -> category8.isChecked = true
            category9.text.toString() -> category9.isChecked = true
            category10.text.toString() -> category10.isChecked = true
            category11.text.toString() -> category11.isChecked = true
            category12.text.toString() -> category12.isChecked = true
        }

        setCategory(category1, categories)
        setCategory(category2, categories)
        setCategory(category3, categories)
        setCategory(category4, categories)
        setCategory(category5, categories)
        setCategory(category6, categories)
        setCategory(category7, categories)
        setCategory(category8, categories)
        setCategory(category9, categories)
        setCategory(category10, categories)
        setCategory(category11, categories)
        setCategory(category12, categories)


        // 그룹 추가 부분 나중에 그룹프래그먼트 코딩하고 추가하기

        val egg = binding.checkboxIngredientEgg
        val milk = binding.checkboxIngredientMilk
        val buckwheat = binding.checkboxIngredientBuckwheat
        val peanut = binding.checkboxIngredientPeanut
        val soybean = binding.checkboxIngredientSoybean
        val wheat = binding.checkboxIngredientWheat
        val mackerel = binding.checkboxIngredientMackerel
        val crab = binding.checkboxIngredientCrab
        val shrimp = binding.checkboxIngredientShrimp
        val pork = binding.checkboxIngredientPork
        val peach = binding.checkboxIngredientPeach
        val tomato = binding.checkboxIngredientTomato
        val sulfurousAcids = binding.checkboxIngredientSulfurousAcids
        val walnut = binding.checkboxIngredientWalnut
        val chicken = binding.checkboxIngredientChicken
        val beef = binding.checkboxIngredientBeef
        val squid = binding.checkboxIngredientSquid
        val seashell = binding.checkboxIngredientSeashell
        val pinenut = binding.checkboxIngredientPinenut

        val allergies = listOf(egg, milk, buckwheat, peanut, soybean, wheat, mackerel,
            crab, shrimp, pork, peach, tomato, sulfurousAcids, walnut, chicken, beef, squid, seashell, pinenut)

        var selectedAllergies = mutableListOf<String>()

        var selectedAllergiesCount = 0

        var selectedAllergiesFromViewModel = viewModel.selectedAllergies.value
        if (selectedAllergiesFromViewModel != null) {
            for (allergy in selectedAllergiesFromViewModel) {
                if (allergy == "계란") egg.isChecked = true
                else if (allergy == "우유") milk.isChecked = true
                else if (allergy == "메밀") buckwheat.isChecked = true
                else if (allergy == "대두") soybean.isChecked = true
                else if (allergy == "땅콩") peanut.isChecked = true
                else if (allergy == "밀") wheat.isChecked = true
                else if (allergy == "고등어") mackerel.isChecked = true
                else if (allergy == "게") crab.isChecked = true
                else if (allergy == "새우") shrimp.isChecked = true
                else if (allergy == "돼지고기") pork.isChecked = true
                else if (allergy == "복숭아") peach.isChecked = true
                else if (allergy == "토마토") tomato.isChecked = true
                else if (allergy == "아황산류") sulfurousAcids.isChecked = true
                else if (allergy == "호두") walnut.isChecked = true
                else if (allergy == "닭고기") chicken.isChecked = true
                else if (allergy == "쇠고기") beef.isChecked = true
                else if (allergy == "오징어") squid.isChecked = true
                else if (allergy == "조개") seashell.isChecked = true
                else if (allergy == "잣") pinenut.isChecked = true
            }
        }

        setAllergyFilter(egg, "계란")
        setAllergyFilter(milk, "우유")
        setAllergyFilter(buckwheat, "메밀")
        setAllergyFilter(peanut, "땅콩")
        setAllergyFilter(soybean, "대두")
        setAllergyFilter(wheat, "밀")
        setAllergyFilter(mackerel, "고등어")
        setAllergyFilter(crab, "게")
        setAllergyFilter(shrimp, "새우")
        setAllergyFilter(pork, "돼지고기")
        setAllergyFilter(peach, "복숭아")
        setAllergyFilter(tomato, "토마토")
        setAllergyFilter(sulfurousAcids, "아황산류")
        setAllergyFilter(walnut, "호두")
        setAllergyFilter(chicken, "닭고기")
        setAllergyFilter(beef, "쇠고기")
        setAllergyFilter(squid, "오징어")
        setAllergyFilter(seashell, "조개")
        setAllergyFilter(pinenut, "잣")




//        egg.setOnCheckedChangeListener{ _, isChecked ->
//            if (isChecked) {
//                if (selectedAllergiesCount >= 2) {
//                    Toast.makeText(requireContext(), "최대 2개까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
//                    egg.isChecked = false
//                } else {
//                    selectedAllergies.add("계란")
//                    selectedAllergiesCount += 1
//                }
//            } else {
//                selectedAllergies.remove("계란")
//                selectedAllergiesCount -= 1
//            }
//        }
//
//        milk.setOnCheckedChangeListener{ _, isChecked ->
//            if (isChecked) {
//                if (selectedAllergiesCount >= 2) {
//                    Toast.makeText(requireContext(), "최대 2개까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
//                    milk.isChecked = false
//                } else {
//                    selectedAllergies.add("우유")
//                    selectedAllergiesCount += 1
//                }
//            } else {
//                selectedAllergies.remove("우유")
//                selectedAllergiesCount -= 1
//            }
//        }
//
//        wheat.setOnCheckedChangeListener{ _, isChecked ->
//            if (isChecked) {
//                if (selectedAllergiesCount >= 2) {
//                    Toast.makeText(requireContext(), "최대 2개까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
//                    wheat.isChecked = false
//                } else {
//                    selectedAllergies.add("밀")
//                    selectedAllergiesCount += 1
//                }
//            } else {
//                selectedAllergies.remove("밀")
//                selectedAllergiesCount -= 1
//            }
//        }
//
//        soybean.setOnCheckedChangeListener{ _, isChecked ->
//            if (isChecked) {
//                if (selectedAllergiesCount >= 2) {
//                    Toast.makeText(requireContext(), "최대 2개까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
//                    soybean.isChecked = false
//                } else {
//                    selectedAllergies.add("대두")
//                    selectedAllergiesCount += 1
//                }
//            } else {
//                selectedAllergies.remove("대두")
//                selectedAllergiesCount -= 1
//            }
//        }


        binding.btnApplyFilter.setOnClickListener {
//            viewModel.setCategoryFilter(selectedCategory)
//            viewModel.setAllergiesFilter(selectedAllergies)
//            viewModel.setAllergiesCount(selectedAllergiesCount)
            requireActivity().supportFragmentManager.popBackStack()
        }


    }

    private fun setCategory(category: Switch, categories: List<Switch>) {
        category.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val selectedCategory = category.text.toString()
                viewModel.setCategoryFilter(selectedCategory)
                val otherCategories = categories.minus(category)
                for (otherCategory in otherCategories) {
                    otherCategory.isChecked = false
                }
            } else {
                category.isChecked = false
            }
        }
    }

    private fun setAllergyFilter(allergen: CheckBox, allergenName: String){
        allergen.setOnCheckedChangeListener{ _, isChecked ->
            var selectedAllergiesCount = 0
            if (viewModel.selectedAllergies.value == null) {
                selectedAllergiesCount = 0
            } else {
                selectedAllergiesCount = viewModel.selectedAllergies.value!!.size
            }
            if (allergen.isChecked) {
                if (selectedAllergiesCount >= 2) {
                    Toast.makeText(requireContext(), "최대 2개까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    allergen.isChecked = false
                } else {
                    viewModel.addAllergiesFilter(allergenName)
                }
            } else {
                viewModel.removeAllergiesFilter(allergenName)
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
//    }
}