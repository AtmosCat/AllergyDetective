package com.allergyguardian.allergyguardian.presentation.franchise
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
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseFilterBinding
import com.allergyguardian.allergyguardian.presentation.FranchiseViewModel
import com.allergyguardian.allergyguardian.presentation.SharedViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel
import com.allergyguardian.allergyguardian.presentation.franchise.franchise_category.FranchiseCategoryFragment

class FranchiseFilterFragment : Fragment() {

    private var _binding : FragmentFranchiseFilterBinding? = null

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


    private val franchiseViewModel: FranchiseViewModel by activityViewModels {
        viewModelFactory { initializer { FranchiseViewModel(requireActivity().application) } }
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
        _binding = FragmentFranchiseFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
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
        if (franchiseViewModel.selectedAllergies.value != null) {
            selectedAllergiesByCheckbox = franchiseViewModel.selectedAllergies.value!!
            if (franchiseViewModel.selectedAllergies.value?.size != 0) {
                for (selectedAllergy in franchiseViewModel.selectedAllergies.value!!)
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

        val franchiseCategoryFragment = requireActivity().supportFragmentManager.findFragmentByTag("FranchiseCategoryFragment")
        binding.btnApplyFilter.setOnClickListener {
            franchiseViewModel.setAllergyFilter(selectedAllergiesByCheckbox)
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@FranchiseFilterFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun allergyCheckboxClicker(checkbox: CheckBox, position: Int) {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (checkbox.isChecked) {
                selectedAllergiesByCheckbox.add(allergiesTextList[position])
            } else {
                selectedAllergiesByCheckbox.remove(allergiesTextList[position])
            }
        }
    }

    private fun updateCheckboxState(checkBox: CheckBox, position: Int) {
        if (isAllergiesCheckedList[position]) {
            allergies[position].isChecked = true
        } else {
            allergies[position].isChecked = false
        }
    }


}