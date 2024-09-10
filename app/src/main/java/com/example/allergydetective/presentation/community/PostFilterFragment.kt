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
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.currentCompositionErrors
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.GroupMember
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentFilterBinding
import com.example.allergydetective.databinding.FragmentPostFilterBinding
import com.example.allergydetective.presentation.PostViewModel
import com.example.allergydetective.presentation.SharedViewModel
import com.example.allergydetective.presentation.UserViewModel
import kotlinx.coroutines.selects.select


class PostFilterFragment : Fragment() {

    private var _binding : FragmentPostFilterBinding? = null

    private var isCategoryButtonCheckedList = mutableListOf(false, false, false, false, false)

    private lateinit var categoryButton1: Button
    private lateinit var categoryButton2: Button
    private lateinit var categoryButton3: Button
    private lateinit var categoryButton4: Button
    private lateinit var categoryButton5: Button
//    private lateinit var categoryButton6: Button
//    private lateinit var categoryButton7: Button
//    private lateinit var categoryButton8: Button
//    private lateinit var categoryButton9: Button
//    private lateinit var categoryButton10: Button
//    private lateinit var categoryButton11: Button
//    private lateinit var categoryButton12: Button

    private lateinit var categoryButtonList : List<Button>

    private lateinit var categoryButtonTextList : List<String>

    private var selectedCategoriesByButton = mutableListOf<String>()

    private var isOptionCheckboxCheckedList = mutableListOf(false, false)

    private lateinit var optionCheckbox1: CheckBox
    private lateinit var optionCheckbox2: CheckBox

    private lateinit var optionCheckboxList: List<CheckBox>
    private lateinit var optionCheckboxTextList : List<String>

    private var selectedSearchOption = ""

    private val binding get() = _binding!!


    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    private val postViewModel: PostViewModel by activityViewModels {
        viewModelFactory { initializer { PostViewModel(requireActivity().application) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 카테고리 관련 부분
        categoryButton1 = binding.customButton1
        categoryButton2 = binding.customButton2
        categoryButton3 = binding.customButton3
        categoryButton4 = binding.customButton4
        categoryButton5 = binding.customButton5
//        categoryButton6 = binding.customButton6
//        categoryButton7 = binding.customButton7
//        categoryButton8 = binding.customButton8
//        categoryButton9 = binding.customButton9
//        categoryButton10 = binding.customButton10
//        categoryButton11 = binding.customButton11
//        categoryButton12 = binding.customButton12

        categoryButtonList = listOf(
            categoryButton1,
            categoryButton2,
            categoryButton3,
            categoryButton4,
            categoryButton5,
//            categoryButton6,
//            categoryButton7,
//            categoryButton8,
//            categoryButton9,
//            categoryButton10,
//            categoryButton11,
//            categoryButton12
        )

        categoryButtonTextList = listOf(
            categoryButton1.text.toString(),
            categoryButton2.text.toString(),
            categoryButton3.text.toString(),
            categoryButton4.text.toString(),
            categoryButton5.text.toString(),
//            categoryButton6.text.toString(),
//            categoryButton7.text.toString(),
//            categoryButton8.text.toString(),
//            categoryButton9.text.toString(),
//            categoryButton10.text.toString(),
//            categoryButton11.text.toString(),
//            categoryButton12.text.toString()
        )

        // 초기 카테고리 세팅
        if (postViewModel.selectedCategories.value != null) {
            selectedCategoriesByButton = postViewModel.selectedCategories.value!!
            if (postViewModel.selectedCategories.value?.size != 0) {
                for (selectedCategory in postViewModel.selectedCategories.value!!)
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
        for (i in 0..categoryButtonList.size-1) {
            categoryButtonClicker(categoryButtonList[i], i)
        }

        // 검색 옵션 관련 부분
        optionCheckbox1 = binding.optionTitle
        optionCheckbox2 = binding.optionTitleAndDetail

        optionCheckboxList = listOf(optionCheckbox1, optionCheckbox2)

        optionCheckboxTextList = listOf(
            optionCheckbox1.text.toString(),
            optionCheckbox2.text.toString(),
        )

        // 초기 검색 옵션 세팅
        if (postViewModel.selectedSearchOption.value != null) {
            selectedSearchOption = postViewModel.selectedSearchOption.value!!
            if (selectedSearchOption.isNotEmpty()) {
                for (optionCheckbox in optionCheckboxList) optionCheckbox.isChecked = false
                val index = optionCheckboxTextList.indexOf(selectedSearchOption)
                optionCheckboxList[index].isChecked = true
            } else {
                selectedSearchOption = ""
            }
        } else {
            selectedSearchOption = ""
        }

        // 알러지 버튼 ClickListener
        for (checkbox in optionCheckboxList) {
            optionCheckboxClicker(checkbox)
        }

        binding.btnApplyFilter.setOnClickListener {
            postViewModel.setCategories(selectedCategoriesByButton)
            if (selectedSearchOption.isNotEmpty()) {
                postViewModel.setSearchOption(selectedSearchOption)
            } else {
                postViewModel.setSearchOption("제목+내용 검색")
            }
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun categoryButtonClicker(button: Button, position: Int){
        button.setOnClickListener{
            isCategoryButtonCheckedList[position] = !isCategoryButtonCheckedList[position]
            updateButtonState(button, position)
            setCategories(button, position)
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

    private fun setCategories(button: Button, position: Int) {
        if (isCategoryButtonCheckedList[position]) {
            selectedCategoriesByButton.add(button.text.toString())
        } else {
            selectedCategoriesByButton.remove(button.text.toString())
        }
    }

    private fun optionCheckboxClicker(checkbox: CheckBox) {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            var otherCheckboxes = optionCheckboxList - checkbox
            if (checkbox.isChecked) {
                for (checkbox in otherCheckboxes) checkbox.isChecked = false
                selectedSearchOption = checkbox.text.toString()
            } else {
                selectedSearchOption = ""
            }
         }
    }

}