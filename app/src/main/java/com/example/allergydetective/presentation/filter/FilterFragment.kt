package com.example.allergydetective.presentation.filter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.allergydetective.R
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentFilterBinding
import com.example.allergydetective.presentation.SharedViewModel


class FilterFragment : Fragment() {

    private var _binding : FragmentFilterBinding? = null

    private var isCategoryButton1Checked = false
    private var isCategoryButton2Checked = false
    private var isCategoryButton3Checked = false
    private var isCategoryButton4Checked = false
    private var isCategoryButton5Checked = false
    private var isCategoryButton6Checked = false
    private var isCategoryButton7Checked = false
    private var isCategoryButton8Checked = false
    private var isCategoryButton9Checked = false
    private var isCategoryButton10Checked = false
    private var isCategoryButton11Checked = false
    private var isCategoryButton12Checked = false

    private var isCategoryButtonCheckedList = mutableListOf(
        isCategoryButton1Checked,
        isCategoryButton2Checked,
        isCategoryButton3Checked,
        isCategoryButton4Checked,
        isCategoryButton5Checked,
        isCategoryButton6Checked,
        isCategoryButton7Checked,
        isCategoryButton8Checked,
        isCategoryButton9Checked,
        isCategoryButton10Checked,
        isCategoryButton11Checked,
        isCategoryButton12Checked,
    )

    private lateinit var categoryButton1: Button
    private lateinit var categoryButton2: Button
    private lateinit var categoryButton3: Button
    private lateinit var categoryButton4: Button
    private lateinit var categoryButton5: Button
    private lateinit var categoryButton6: Button
    private lateinit var categoryButton7: Button
    private lateinit var categoryButton8: Button
    private lateinit var categoryButton9: Button
    private lateinit var categoryButton10: Button
    private lateinit var categoryButton11: Button
    private lateinit var categoryButton12: Button

    private lateinit var categoryButtonList : List<Button>

    private lateinit var categoryButtonTextList : List<String>

    private var selectedCategoriesByButton = mutableListOf<String>()

    private lateinit var  egg: CheckBox
    private lateinit var  milk: CheckBox
    private lateinit var  buckwheat: CheckBox
    private lateinit var  peanut: CheckBox
    private lateinit var  soybean: CheckBox
    private lateinit var  wheat: CheckBox
    private lateinit var  mackerel: CheckBox
    private lateinit var  crab: CheckBox
    private lateinit var  shrimp: CheckBox
    private lateinit var  pork: CheckBox
    private lateinit var  peach: CheckBox
    private lateinit var  tomato: CheckBox
    private lateinit var  sulfurousAcids: CheckBox
    private lateinit var  walnut: CheckBox
    private lateinit var  chicken: CheckBox
    private lateinit var  beef: CheckBox
    private lateinit var  squid: CheckBox
    private lateinit var  seashell: CheckBox
    private lateinit var  pinenut: CheckBox
    private lateinit var  oyster: CheckBox
    private lateinit var  abalone: CheckBox
    private lateinit var  mussel: CheckBox

    private lateinit var allergies : List<CheckBox>

    private lateinit var allergiesTextList : List<String>

    private var isAllergiesCheckedList = mutableListOf(false,false,false,false,false,false,false,false,false,false,
        false,false,false,false,false,false,false,false,false,false,false,false)

    private var selectedAllergiesByCheckbox = mutableListOf<String>()

    private val binding get() = _binding!!

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

        // 카테고리 관련 부분
        categoryButton1 = binding.customButton1
        categoryButton2 = binding.customButton2
        categoryButton3 = binding.customButton3
        categoryButton4 = binding.customButton4
        categoryButton5 = binding.customButton5
        categoryButton6 = binding.customButton6
        categoryButton7 = binding.customButton7
        categoryButton8 = binding.customButton8
        categoryButton9 = binding.customButton9
        categoryButton10 = binding.customButton10
        categoryButton11 = binding.customButton11
        categoryButton12 = binding.customButton12

        categoryButtonList = listOf(
            categoryButton1,
            categoryButton2,
            categoryButton3,
            categoryButton4,
            categoryButton5,
            categoryButton6,
            categoryButton7,
            categoryButton8,
            categoryButton9,
            categoryButton10,
            categoryButton11,
            categoryButton12
        )

        categoryButtonTextList = listOf(
            categoryButton1.text.toString(),
            categoryButton2.text.toString(),
            categoryButton3.text.toString(),
            categoryButton4.text.toString(),
            categoryButton5.text.toString(),
            categoryButton6.text.toString(),
            categoryButton7.text.toString(),
            categoryButton8.text.toString(),
            categoryButton9.text.toString(),
            categoryButton10.text.toString(),
            categoryButton11.text.toString(),
            categoryButton12.text.toString()
        )

        // 초기 카테고리 세팅
        if (viewModel.selectedCategories.value != null) {
            selectedCategoriesByButton = viewModel.selectedCategories.value!!
            if (viewModel.selectedCategories.value?.size != 0) {
                for (selectedCategory in viewModel.selectedCategories.value!!)
                    if (selectedCategory in categoryButtonTextList) {
                        val index = categoryButtonTextList.indexOf(selectedCategory)
                        isCategoryButtonCheckedList[index] = true
                    }
            } else {
                for (i in 0..categoryButtonList.size-1) {
                    isCategoryButtonCheckedList[i] = false
                }
            }
        } else {
            selectedCategoriesByButton = mutableListOf()
        }

        for (i in 0..categoryButtonList.size-1) {
            updateButtonState(categoryButtonList[i], i)
        }

        // 카테고리 버튼 ClickListener
        for (button in categoryButtonList) {
            var i = 0
            categoryButtonClicker(button, i)
            i += 1
        }

        // 알러지 관련 부분
        egg = binding.checkboxIngredientEgg
        milk = binding.checkboxIngredientMilk
        buckwheat = binding.checkboxIngredientBuckwheat
        peanut = binding.checkboxIngredientPeanut
        soybean = binding.checkboxIngredientSoybean
        wheat = binding.checkboxIngredientWheat
        mackerel = binding.checkboxIngredientMackerel
        crab = binding.checkboxIngredientCrab
        shrimp = binding.checkboxIngredientShrimp
        pork = binding.checkboxIngredientPork
        peach = binding.checkboxIngredientPeach
        tomato = binding.checkboxIngredientTomato
        sulfurousAcids = binding.checkboxIngredientSulfurousAcids
        walnut = binding.checkboxIngredientWalnut
        chicken = binding.checkboxIngredientChicken
        beef = binding.checkboxIngredientBeef
        squid = binding.checkboxIngredientSquid
        seashell = binding.checkboxIngredientSeashell
        pinenut = binding.checkboxIngredientPinenut
        oyster = binding.checkboxIngredientOyster
        abalone = binding.checkboxIngredientAbalone
        mussel = binding.checkboxIngredientMussel

        allergies = listOf(egg, milk, buckwheat, peanut, soybean, wheat, mackerel, crab, shrimp, pork, peach,
            tomato, sulfurousAcids, walnut, chicken, beef, squid, seashell, pinenut, oyster, abalone, mussel)

        allergiesTextList = listOf(
            egg.text.toString(),
            milk.text.toString(),
            buckwheat.text.toString(),
            peanut.text.toString(),
            soybean.text.toString(),
            wheat.text.toString(),
            mackerel.text.toString(),
            crab.text.toString(),
            shrimp.text.toString(),
            pork.text.toString(),
            peach.text.toString(),
            tomato.text.toString(),
            sulfurousAcids.text.toString(),
            walnut.text.toString(),
            chicken.text.toString(),
            beef.text.toString(),
            squid.text.toString(),
            seashell.text.toString(),
            pinenut.text.toString(),
            oyster.text.toString(),
            abalone.text.toString(),
            mussel.text.toString(),
        )

        // 초기 알러지 세팅
        if (viewModel.selectedAllergies.value != null) {
            selectedAllergiesByCheckbox = viewModel.selectedAllergies.value!!
            if (viewModel.selectedAllergies.value?.size != 0) {
                for (selectedAllergy in viewModel.selectedAllergies.value!!)
                    if (selectedAllergy in allergiesTextList) {
                        val index = allergiesTextList.indexOf(selectedAllergy)
                        isAllergiesCheckedList[index] = true
                    }
            } else {
                for (i in 0..allergies.size-1) {
                    isAllergiesCheckedList[i] = false
                }
            }
        } else {
            selectedAllergiesByCheckbox = mutableListOf()
        }

        for (i in 0..allergies.size-1) {
            updateCheckboxState(allergies[i], i)
        }

        // 알러지 버튼 ClickListener
        for (allergyCheckbox in allergies) {
            var i = 0
            allergyCheckboxClicker(allergyCheckbox, i)
            i += 1
        }
//
//        var selectedAllergiesFromViewModel = viewModel.selectedAllergies.value
//        if (selectedAllergiesFromViewModel != null) {
//            for (allergy in selectedAllergiesFromViewModel) {
//                if (allergy == "계란") egg.isChecked = true
//                else if (allergy == "우유") milk.isChecked = true
//                else if (allergy == "메밀") buckwheat.isChecked = true
//                else if (allergy == "대두") soybean.isChecked = true
//                else if (allergy == "땅콩") peanut.isChecked = true
//                else if (allergy == "밀") wheat.isChecked = true
//                else if (allergy == "고등어") mackerel.isChecked = true
//                else if (allergy == "게") crab.isChecked = true
//                else if (allergy == "새우") shrimp.isChecked = true
//                else if (allergy == "돼지고기") pork.isChecked = true
//                else if (allergy == "복숭아") peach.isChecked = true
//                else if (allergy == "토마토") tomato.isChecked = true
//                else if (allergy == "아황산류") sulfurousAcids.isChecked = true
//                else if (allergy == "호두") walnut.isChecked = true
//                else if (allergy == "닭고기") chicken.isChecked = true
//                else if (allergy == "쇠고기") beef.isChecked = true
//                else if (allergy == "오징어") squid.isChecked = true
//                else if (allergy == "조개") seashell.isChecked = true
//                else if (allergy == "잣") pinenut.isChecked = true
//            }
//        }
//
//        setAllergyFilter(egg, "계란")
//        setAllergyFilter(milk, "우유")
//        setAllergyFilter(buckwheat, "메밀")
//        setAllergyFilter(peanut, "땅콩")
//        setAllergyFilter(soybean, "대두")
//        setAllergyFilter(wheat, "밀")
//        setAllergyFilter(mackerel, "고등어")
//        setAllergyFilter(crab, "게")
//        setAllergyFilter(shrimp, "새우")
//        setAllergyFilter(pork, "돼지고기")
//        setAllergyFilter(peach, "복숭아")
//        setAllergyFilter(tomato, "토마토")
//        setAllergyFilter(sulfurousAcids, "아황산류")
//        setAllergyFilter(walnut, "호두")
//        setAllergyFilter(chicken, "닭고기")
//        setAllergyFilter(beef, "쇠고기")
//        setAllergyFilter(squid, "오징어")
//        setAllergyFilter(seashell, "조개")
//        setAllergyFilter(pinenut, "잣")

        binding.btnApplyFilter.setOnClickListener {
            viewModel.setCategoryFilter(selectedCategoriesByButton)
            viewModel.setAllergyFilter(selectedAllergiesByCheckbox)
            requireActivity().supportFragmentManager.popBackStack()
        }







//
//        val category1 = binding.switch1
//        val category2 = binding.switch2
//        val category3 = binding.switch3
//        val category4 = binding.switch4
//        val category5 = binding.switch5
//        val category6 = binding.switch6
//        val category7 = binding.switch7
//        val category8 = binding.switch8
//        val category9 = binding.switch9
//        val category10 = binding.switch10
//        val category11 = binding.switch11
//        val category12 = binding.switch12
//
//        var categories = mutableListOf(category1, category2, category3, category4,
//            category5, category6, category7, category8, category9, category10, category11, category12 )
//
//        when (viewModel.selectedCategory.value.toString()) {
//            category1.text.toString() -> category1.isChecked = true
//            category2.text.toString() -> category2.isChecked = true
//            category3.text.toString() -> category3.isChecked = true
//            category4.text.toString() -> category4.isChecked = true
//            category5.text.toString() -> category5.isChecked = true
//            category6.text.toString() -> category6.isChecked = true
//            category7.text.toString() -> category7.isChecked = true
//            category8.text.toString() -> category8.isChecked = true
//            category9.text.toString() -> category9.isChecked = true
//            category10.text.toString() -> category10.isChecked = true
//            category11.text.toString() -> category11.isChecked = true
//            category12.text.toString() -> category12.isChecked = true
//        }
//
//        setCategory(category1, categories)
//        setCategory(category2, categories)
//        setCategory(category3, categories)
//        setCategory(category4, categories)
//        setCategory(category5, categories)
//        setCategory(category6, categories)
//        setCategory(category7, categories)
//        setCategory(category8, categories)
//        setCategory(category9, categories)
//        setCategory(category10, categories)
//        setCategory(category11, categories)
//        setCategory(category12, categories)
//

    }

    private fun categoryButtonClicker(button: Button, position: Int){
        button.setOnClickListener{
            isCategoryButtonCheckedList[position] = !isCategoryButtonCheckedList[position]
            updateButtonState(button, position)
            setCategories(button, position)
        }
    }

    private fun allergyCheckboxClicker(checkbox: CheckBox, position: Int) {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
//            var selectedAllergiesCount = selectedAllergiesByCheckbox.size
//            if (viewModel.selectedAllergies.value == null) {
//                selectedAllergiesCount = 0
//            }

            if (checkbox.isChecked) {
                if (selectedAllergiesByCheckbox.size >= 1) {
                    Toast.makeText(requireContext(), "[베이직 멤버십] 1개만 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    checkbox.isChecked = false
                } else {
                    selectedAllergiesByCheckbox.add(allergiesTextList[position])
                }
            } else {
                selectedAllergiesByCheckbox.remove(allergiesTextList[position])
            }
        }
    }

    private fun updateButtonState(button: Button, position: Int) {
        if (isCategoryButtonCheckedList[position]) {
            button.setBackgroundColor(getColor(requireContext(), R.color.main_color_orange))
            button.setTextColor(getColor(requireContext(), R.color.white))
        } else {
            button.setBackgroundColor(getColor(requireContext(), R.color.main_color_more_light_gray))
            button.setTextColor(getColor(requireContext(), R.color.black))
        }
    }

    private fun updateCheckboxState(checkBox: CheckBox, position: Int) {
        if (isAllergiesCheckedList[position]) {
            allergies[position].isChecked = true
        } else {
            allergies[position].isChecked = false
        }
    }

    private fun setCategories(button: Button, position: Int){
        if (isCategoryButtonCheckedList[position]) {
            selectedCategoriesByButton.add(button.text.toString())
        } else {
            selectedCategoriesByButton.remove(button.text.toString())
        }
    }

//    private fun setCategory(category: Switch, categories: List<Switch>) {
//        category.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                val selectedCategory = category.text.toString()
//                viewModel.setCategoryFilter(selectedCategory)
//                val otherCategories = categories.minus(category)
//                for (otherCategory in otherCategories) {
//                    otherCategory.isChecked = false
//                }
//            } else {
//                category.isChecked = false
//            }
//        }
//    }


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