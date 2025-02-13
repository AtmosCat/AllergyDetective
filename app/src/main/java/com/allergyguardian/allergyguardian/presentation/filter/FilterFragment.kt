package com.allergyguardian.allergyguardian.presentation.filter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.model.user.GroupMember
import com.allergyguardian.allergyguardian.data.repository.food.GonggongFoodRepositoryImpl
import com.allergyguardian.allergyguardian.data.repository.market.MarketRepositoryImpl
import com.allergyguardian.allergyguardian.databinding.FragmentFilterBinding
import com.allergyguardian.allergyguardian.presentation.SharedViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel

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

    private var isSelectAllCategoryButtonChecked = false

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

    private lateinit var currentUserGroup: MutableList<GroupMember>

    private var selectedMember = GroupMember("", mutableListOf())

    private lateinit var member1icon: ImageView
    private lateinit var member2icon: ImageView
    private lateinit var member3icon: ImageView
    private lateinit var member4icon: ImageView
    private lateinit var member5icon: ImageView

    private lateinit var memberIconList : List<ImageView>

    private lateinit var member1name: TextView
    private lateinit var member2name: TextView
    private lateinit var member3name: TextView
    private lateinit var member4name: TextView
    private lateinit var member5name: TextView

    private lateinit var memberNameList: List<TextView>

    private var isMemberSelectedList = mutableListOf(false,false,false,false,false)

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

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
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

        binding.btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

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
        for (i in 0..categoryButtonList.size-1) {
            categoryButtonClicker(categoryButtonList[i], i)
        }

        // 그룹 관련 부분
        val noMember1icon = binding.ivNoGroupMember1
        val noMember2icon = binding.ivNoGroupMember2
        val noMember3icon = binding.ivNoGroupMember3
        val noMember4icon = binding.ivNoGroupMember4
        val noMember5icon = binding.ivNoGroupMember5

        val noMemberIconList = listOf(noMember1icon, noMember2icon,
            noMember3icon, noMember4icon, noMember5icon)

        member1icon = binding.ivAddedGroupMember1
        member2icon = binding.ivAddedGroupMember2
        member3icon = binding.ivAddedGroupMember3
        member4icon = binding.ivAddedGroupMember4
        member5icon = binding.ivAddedGroupMember5

        memberIconList = listOf(member1icon, member2icon, member3icon, member4icon, member5icon)

        member1name = binding.tvGroupMember1
        member2name = binding.tvGroupMember2
        member3name = binding.tvGroupMember3
        member4name = binding.tvGroupMember4
        member5name = binding.tvGroupMember5

        memberNameList = listOf(member1name, member2name, member3name, member4name, member5name)

        currentUserGroup = mutableListOf()
        var realUserGroup = listOf<GroupMember>()
        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            currentUserGroup = data!!.group
            realUserGroup = currentUserGroup.filter { it.name != "" }
            if (realUserGroup.isNotEmpty()) {
                binding.tvNoMembers.visibility = View.GONE
                binding.viewNoMembers.visibility = View.GONE
                for (member in currentUserGroup) {
                    if (member.name != "") {
                        val index = currentUserGroup.indexOf(member)
                        noMemberIconList[index].visibility = View.GONE
                        memberIconList[index].visibility = View.VISIBLE
                        memberNameList[index].setText(member.name)
                        memberIconList[index].setOnClickListener{
                            when (index) {
                                0 -> {
                                    isMemberSelectedList[1] = false
                                    isMemberSelectedList[2] = false
                                    isMemberSelectedList[3] = false
                                    isMemberSelectedList[4] = false
                                }
                                1 -> {
                                    isMemberSelectedList[0] = false
                                    isMemberSelectedList[2] = false
                                    isMemberSelectedList[3] = false
                                    isMemberSelectedList[4] = false
                                }
                                2 -> {
                                    isMemberSelectedList[0] = false
                                    isMemberSelectedList[1] = false
                                    isMemberSelectedList[3] = false
                                    isMemberSelectedList[4] = false
                                }
                                3 -> {
                                    isMemberSelectedList[0] = false
                                    isMemberSelectedList[1] = false
                                    isMemberSelectedList[2] = false
                                    isMemberSelectedList[4] = false
                                }
                                4 -> {
                                    isMemberSelectedList[0] = false
                                    isMemberSelectedList[1] = false
                                    isMemberSelectedList[2] = false
                                    isMemberSelectedList[3] = false
                                }
                            }
                            selectMember(index)
                        }
                    }
                }
            }

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
        for (i in 0..allergies.size-1) {
            allergyCheckboxClicker(allergies[i], i)
        }

        binding.btnApplyFilter.setOnClickListener {
            viewModel.setCategoryFilter(selectedCategoriesByButton)
            viewModel.setAllergyFilter(selectedAllergiesByCheckbox)
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

//    private fun allCategoryButtonClicker(allCategoryButton: CheckBox, buttons: List<Button>, positions: List<Int>){
//        allCategoryButton.setOnClickListener{
//            for (position in positions) {
//                isCategoryButtonCheckedList[position] = !isCategoryButtonCheckedList[position]
//                updateButtonState(categoryButtonList[position], position)
//                setCategories(categoryButtonList[position], position)
//            }
//        }
//    }

    private fun updateButtonState(button: Button, position: Int) {
        if (isCategoryButtonCheckedList[position]) {
            button.setBackgroundColor(getColor(requireContext(), R.color.main_color_orange))
            button.setTextColor(getColor(requireContext(), R.color.white))
        } else {
            button.setBackgroundColor(getColor(requireContext(), R.color.main_color_more_light_gray))
            button.setTextColor(getColor(requireContext(), R.color.black))
        }
    }

    private fun setCategories(button: Button, position: Int){
        if (isCategoryButtonCheckedList[position]) {
            selectedCategoriesByButton.add(button.text.toString())
        } else {
            selectedCategoriesByButton.remove(button.text.toString())
        }
    }

    private fun allergyCheckboxClicker(checkbox: CheckBox, position: Int) {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (checkbox.isChecked) {
                if (checkbox.text.toString() !in selectedMember.allergy) {
                    initializeMember()
                }
                selectedAllergiesByCheckbox.add(allergiesTextList[position])
            } else {
                if (checkbox.text.toString() in selectedMember.allergy) {
                    initializeMember()
                }
                selectedAllergiesByCheckbox.remove(allergiesTextList[position])
            }
//            if (selectedMember != GroupMember()) {
//                initializeMember()
//            }
        }
    }

    private fun updateCheckboxState(checkBox: CheckBox, position: Int) {
        if (isAllergiesCheckedList[position]) {
            allergies[position].isChecked = true
        } else {
            allergies[position].isChecked = false
        }
    }

    private fun selectMember(position: Int) {

        val selectedMemberIcon = memberIconList[position]
        val notSelectedMemberIcons = memberIconList - memberIconList[position]

        val orangeColor = ContextCompat.getColor(requireContext(), R.color.main_color_orange)
        val moreLightGrayColor =
            ContextCompat.getColor(requireContext(), R.color.main_color_more_light_gray)

        for (notSelectedMember in notSelectedMemberIcons) {
            changeTint(notSelectedMember, moreLightGrayColor)
        }

        if (!isMemberSelectedList[position]) {
            isMemberSelectedList[position] = true

            selectedMember = currentUserGroup[position]
            changeTint(selectedMemberIcon, orangeColor)

            setSelectedMemberAllergies()

        } else {
            selectedMember = GroupMember()
            isMemberSelectedList[position] = false
            changeTint(selectedMemberIcon, moreLightGrayColor)
            for (allergy in allergies) allergy.isChecked = false
        }
    }

    private fun setSelectedMemberAllergies(){
        selectedAllergiesByCheckbox = selectedMember.allergy.toMutableList()
        if (selectedMember.allergy.isNotEmpty()) {

            val notSelectedAllergies = allergiesTextList - selectedAllergiesByCheckbox

            val iterator = allergiesTextList.iterator()
            while (iterator.hasNext()) {
                val allergy = iterator.next()
                if (selectedAllergiesByCheckbox.contains(allergy)) {
                    allergies[allergiesTextList.indexOf(allergy)].isChecked = true
                } else {
                    allergies[allergiesTextList.indexOf(allergy)].isChecked = false
                }
            }
        } else {
            for (allergy in allergies) {
                allergy.isChecked = false
            }
        }
    }

    private fun changeTint(imageView: ImageView, color: Int) {
        val drawable = imageView.drawable
        drawable?.let {
            DrawableCompat.setTint(drawable, color)
            imageView.setImageDrawable(drawable)
        }
    }

    private fun initializeMember() {
        selectedMember = GroupMember()
        for (memberIcon in memberIconList) {
            changeTint(memberIcon, ContextCompat.getColor(requireContext(), R.color.main_color_more_light_gray))
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