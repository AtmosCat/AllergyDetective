package com.example.allergydetective.presentation.mypage


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.GroupMember
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentGroupManagerBinding
import com.example.allergydetective.presentation.SharedViewModel
import com.example.allergydetective.presentation.UserViewModel


class GroupManagerFragment : Fragment() {

    private var _binding: FragmentGroupManagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var  egg : CheckBox
    private lateinit var  milk : CheckBox
    private lateinit var  buckwheat : CheckBox
    private lateinit var  peanut : CheckBox
    private lateinit var  soybean : CheckBox
    private lateinit var  wheat : CheckBox
    private lateinit var  mackerel : CheckBox
    private lateinit var  crab : CheckBox
    private lateinit var  shrimp : CheckBox
    private lateinit var  pork : CheckBox
    private lateinit var  peach : CheckBox
    private lateinit var  tomato : CheckBox
    private lateinit var  sulfurousAcids : CheckBox
    private lateinit var  walnut : CheckBox
    private lateinit var  chicken : CheckBox
    private lateinit var  beef : CheckBox
    private lateinit var  squid : CheckBox
    private lateinit var  seashell : CheckBox
    private lateinit var  pinenut : CheckBox
    private lateinit var  oyster: CheckBox
    private lateinit var  abalone: CheckBox
    private lateinit var  mussel: CheckBox

    private var allergyCheckboxes = listOf<CheckBox>()

    private var allergenNames = listOf("알류(가금류)","우유","메밀","땅콩","대두","밀","고등어","게","새우","돼지고기",
        "복숭아","토마토","아황산류","호두","닭고기","쇠고기","오징어","조개류(조개)","잣", "조개류(굴)","조개류(전복)","조개류(홍합)")

    private lateinit var allergies: List<Pair<CheckBox, String>>

    private var selectedAllergies = mutableListOf<String>()

    private lateinit var currentUserGroup: MutableList<GroupMember>

    private var selectedMember = GroupMember("", mutableListOf())

    private var selectedMemberNewName = ""

    private var ivAddMembers = mutableListOf<ImageView>()

    private var ivAddedMembers = mutableListOf<ImageView>()

    private var tvMembers = mutableListOf<EditText>()

    private var isMember1Added = false
    private var isMember2Added = false
    private var isMember3Added = false
    private var isMember4Added = false
    private var isMember5Added = false

    private var isMemberAddedList = mutableListOf(isMember1Added, isMember2Added, isMember3Added, isMember4Added, isMember5Added)

    private var isMember1Selected = false
    private var isMember2Selected = false
    private var isMember3Selected = false
    private var isMember4Selected = false
    private var isMember5Selected = false

    private var isMemberSelectedList = mutableListOf(isMember1Selected, isMember2Selected, isMember3Selected, isMember4Selected, isMember5Selected)

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
        _binding = FragmentGroupManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val ivAddMember1 = binding.ivAddGroupMember1
        val ivAddMember2 = binding.ivAddGroupMember2
        val ivAddMember3 = binding.ivAddGroupMember3
        val ivAddMember4 = binding.ivAddGroupMember4
        val ivAddMember5 = binding.ivAddGroupMember5

        ivAddMembers = mutableListOf(ivAddMember1, ivAddMember2,
            ivAddMember3, ivAddMember4, ivAddMember5)

        val ivAddedMember1 = binding.ivAddedGroupMember1
        val ivAddedMember2 = binding.ivAddedGroupMember2
        val ivAddedMember3 = binding.ivAddedGroupMember3
        val ivAddedMember4 = binding.ivAddedGroupMember4
        val ivAddedMember5 = binding.ivAddedGroupMember5

        ivAddedMembers = mutableListOf(ivAddedMember1, ivAddedMember2,
            ivAddedMember3, ivAddedMember4, ivAddedMember5)

        for (ivAddedMember in ivAddedMembers) ivAddedMember.visibility = View.GONE

        val tvMember1 = binding.tvGroupMember1
        val tvMember2 = binding.tvGroupMember2
        val tvMember3 = binding.tvGroupMember3
        val tvMember4 = binding.tvGroupMember4
        val tvMember5 = binding.tvGroupMember5

        tvMembers = mutableListOf(tvMember1, tvMember2, tvMember3, tvMember4, tvMember5)

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

        allergyCheckboxes = listOf(egg, milk, buckwheat, peanut, soybean, wheat, mackerel, crab, shrimp, pork,
            peach, tomato, sulfurousAcids, walnut, chicken, beef, squid, seashell, pinenut, oyster,abalone,mussel)

        allergies = listOf(
            Pair(egg, "알류(가금류)"),
            Pair(milk, "우유"),
            Pair(buckwheat, "메밀"),
            Pair(peanut, "땅콩"),
            Pair(soybean, "대두"),
            Pair(wheat, "밀"),
            Pair(mackerel, "고등어"),
            Pair(crab, "게"),
            Pair(shrimp, "새우"),
            Pair(pork, "돼지고기"),
            Pair(peach, "복숭아"),
            Pair(tomato, "토마토"),
            Pair(sulfurousAcids, "아황산류"),
            Pair(walnut, "호두"),
            Pair(chicken, "닭고기"),
            Pair(beef, "쇠고기"),
            Pair(squid, "오징어"),
            Pair(seashell, "조개류(조개)"),
            Pair(pinenut, "잣"),
            Pair(oyster, "조개류(굴)"),
            Pair(abalone, "조개류(전복)"),
            Pair(mussel, "조개류(홍합)")
        )

        for (allergen in allergyCheckboxes) allergen.visibility = View.GONE
        binding.btnSave.visibility = View.GONE

        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            currentUserGroup = data!!.group
            var groupMembers = currentUserGroup.filter { it.name != "" }
            var groupMembersCount = groupMembers.size

            for(i in 0..groupMembersCount - 1) {
                isMemberAddedList[i] = true
                ivAddMembers[i].visibility = View.GONE
                ivAddedMembers[i].visibility = View.VISIBLE
                tvMembers[i].setText(currentUserGroup[i].name)
            }

            if (!isMemberAddedList[0]) {
                addMember(0, "멤버1")
                isMemberAddedList[0] = true
            }
            ivAddedMember1.setOnClickListener{
                isMemberSelectedList[1] = false
                isMemberSelectedList[2] = false
                isMemberSelectedList[3] = false
                isMemberSelectedList[4] = false
                selectMember(0)
            }

            if (!isMemberAddedList[1]) {
                addMember(1, "멤버2")
                isMemberAddedList[1] = true
            }
            ivAddedMember2.setOnClickListener{
                isMemberSelectedList[0] = false
                isMemberSelectedList[2] = false
                isMemberSelectedList[3] = false
                isMemberSelectedList[4] = false
                selectMember(1)
            }

            if (!isMemberAddedList[2]) {
                addMember(2, "멤버3")
                isMemberAddedList[2] = true
            }
            ivAddedMember3.setOnClickListener{
                isMemberSelectedList[0] = false
                isMemberSelectedList[1] = false
                isMemberSelectedList[3] = false
                isMemberSelectedList[4] = false
                selectMember(2)
            }

            if (!isMemberAddedList[3]) {
                addMember(3, "멤버4")
                isMemberAddedList[3] = true
            }
            ivAddedMember4.setOnClickListener{
                isMemberSelectedList[0] = false
                isMemberSelectedList[1] = false
                isMemberSelectedList[2] = false
                isMemberSelectedList[4] = false
                selectMember(3)
            }

            if (!isMemberAddedList[4]) {
                addMember(4, "멤버5")
                isMemberAddedList[4] = true
            }
            ivAddedMember5.setOnClickListener{
                isMemberSelectedList[0] = false
                isMemberSelectedList[1] = false
                isMemberSelectedList[2] = false
                isMemberSelectedList[3] = false
                selectMember(4)
            }

            for (pair in allergies) {
                setAllergyFilter(pair.first, pair.second)
            }
        }

        binding.btnSave.setOnClickListener{
            selectedMember.name = selectedMemberNewName
            selectedMember.allergy = selectedAllergies
            val position = currentUserGroup.indexOf(selectedMember)
            currentUserGroup[position] = selectedMember
            userViewModel.updateGroup(currentUserGroup)
            Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setSelectedMemberAllergies(){
        selectedAllergies = selectedMember.allergy.toMutableList()
        if (selectedMember.allergy.isNotEmpty()) {

            val notSelectedAllergies = allergenNames - selectedAllergies

            for (allergy in selectedAllergies) {
                val index = allergenNames.indexOf(allergy)
                allergies[index].first.isChecked = true
            }
            for (allergy in notSelectedAllergies) {
                val index = allergenNames.indexOf(allergy)
                allergies[index].first.isChecked = false
            }

        } else {
            for (pair in allergies) {
                pair.first.isChecked = false
            }
        }
    }

    private fun setAllergyFilter(allergen: CheckBox, allergenName: String){
        allergen.setOnCheckedChangeListener{ _, isChecked ->
            if (allergen.isChecked && allergenName !in selectedAllergies) {
                if (selectedAllergies.size >= 7) {
                    Toast.makeText(requireContext(), "최대 7개까지 설정 가능합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    selectedAllergies.add(allergenName)
                }
            } else if (!allergen.isChecked && allergenName in selectedAllergies) {
                selectedAllergies.remove(allergenName)
            }
        }
    }

    private fun addMember(position: Int, exampleName: String) {
         ivAddMembers[position].setOnClickListener {
             ivAddMembers[position].visibility = View.GONE
             ivAddedMembers[position].visibility = View.VISIBLE
             tvMembers[position].setText(exampleName)
             currentUserGroup[position] = GroupMember(exampleName, mutableListOf())
//             userViewModel.updateGroupMember(currentUserGroup)
//             isMemberAddedList[position-1] = true
        }
    }

    private fun selectMember(position: Int) {
        val isMemberNotSelectedList = (isMemberSelectedList - isMemberSelectedList[position]).toMutableList()

        val selectedMemberIcon = ivAddedMembers[position]
        val notSelectedMemberIcons = ivAddedMembers - ivAddedMembers[position]

        val selectedMemberName = tvMembers[position]
        val notSelectedMemberNames = tvMembers - tvMembers[position]

        val orangeColor = ContextCompat.getColor(requireContext(), R.color.main_color_orange)
        val moreLightGrayColor =
            ContextCompat.getColor(requireContext(), R.color.main_color_more_light_gray)

        for (notSelectedMember in notSelectedMemberIcons) {
            changeTint(notSelectedMember, moreLightGrayColor)
        }

        for (notSelectedMemberName in notSelectedMemberNames) {
            notSelectedMemberName.isEnabled = false
        }
        if (!isMemberSelectedList[position]) {
            isMemberSelectedList[position] = true

            selectedMember = currentUserGroup[position]
            changeTint(selectedMemberIcon, orangeColor)
            selectedMemberName.isEnabled = true
            selectedMemberNewName = selectedMemberName.text.toString()

            for (allergen in allergyCheckboxes) {
                allergen.visibility = View.VISIBLE
            }

            setSelectedMemberAllergies()

            binding.btnSave.visibility = View.VISIBLE

        } else {
            selectedMember = GroupMember()
            isMemberSelectedList[position] = false

            changeTint(selectedMemberIcon, moreLightGrayColor)
            selectedMemberName.isEnabled = false
            for (allergen in allergyCheckboxes) {
                allergen.visibility = View.GONE
            }
            binding.btnSave.visibility = View.GONE
        }
    }

    private fun changeTint(imageView: ImageView, color: Int) {
        val drawable = imageView.drawable
        drawable?.let {
            DrawableCompat.setTint(drawable, color)
            imageView.setImageDrawable(drawable)
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