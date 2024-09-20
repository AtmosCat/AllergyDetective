package com.example.allergydetective.presentation.mypage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.load
import com.example.allergydetective.R
import com.example.allergydetective.databinding.FragmentEditProfileBinding
import com.example.allergydetective.presentation.UserViewModel
import com.example.allergydetective.presentation.home.MyPageFragment
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null

    private val binding get() = _binding!!

    private var selectedAllergies = mutableListOf<String>()

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    private lateinit var profileImageView: ImageView
    private lateinit var profileImage : Bitmap
    private lateinit var pickImageLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val bitmap = intent?.data?.let { uri ->
                    requireActivity().contentResolver.openInputStream(uri)?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
                }
                bitmap?.let {
                    profileImageView.setImageBitmap(it)
                    profileImage = bitmap
                }
                intent?.data?.let { uri ->
                    lifecycleScope.launch {
                        userViewModel.handleImage(uri)
                    }
                }
            }
        }

        profileImageView = binding.ivProfileImage

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
        val oyster = binding.checkboxIngredientOyster
        val abalone = binding.checkboxIngredientAbalone
        val mussel = binding.checkboxIngredientMussel

        val allergies = listOf(egg, milk, buckwheat, peanut, soybean, wheat, mackerel,
            crab, shrimp, pork, peach, tomato, sulfurousAcids, walnut, chicken, beef, squid, seashell, pinenut,
            oyster, abalone, mussel)

        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data?.photo == "") {
                profileImageView.setImageResource(R.drawable.cat)
            } else {
                userViewModel.getDownloadUrl(
                    onSuccess = { downloadUrl ->
                        // 이미지 로드
                        binding.ivProfileImage.load(downloadUrl) {
                            crossfade(true)
                            placeholder(R.drawable.placeholder) // 로딩 중에 표시할 이미지
                            error(R.drawable.no_photo) // 로드 실패 시 표시할 이미지
                        }
                    },
                    onFailure = { exception ->
                        // 실패 처리
                        binding.ivProfileImage.load(R.drawable.no_photo)
                    })
            }

            if (data?.nickname == "") {
                binding.etProfileName.setText("익명의 고양이")
            } else {
                binding.etProfileName.setText(data!!.nickname)
            }

            val currentUserAllergies = data?.allergy

            if (currentUserAllergies != null) {
                for (allergy in currentUserAllergies) {
                    if (allergy == "알류(가금류)") egg.isChecked = true
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
                    else if (allergy == "조개류(조개)") seashell.isChecked = true
                    else if (allergy == "잣") pinenut.isChecked = true
                    else if (allergy == "조개류(굴)") oyster.isChecked = true
                    else if (allergy == "조개류(전복)") abalone.isChecked = true
                    else if (allergy == "조개류(홍합)") mussel.isChecked = true

                }
            }

            if (currentUserAllergies != null) {
                selectedAllergies = currentUserAllergies
            }

            setAllergyFilter(egg, "알류(가금류)")
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
            setAllergyFilter(seashell, "조개류(조개)")
            setAllergyFilter(pinenut, "잣")
            setAllergyFilter(oyster, "조개류(굴)")
            setAllergyFilter(abalone, "조개류(전복)")
            setAllergyFilter(mussel, "조개류(홍합)")
        }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


        binding.btnEditProfileImage.setOnClickListener{
            Toast.makeText(this.requireContext(), "갤러리에서 사진을 선택해주세요.", Toast.LENGTH_SHORT)
                .show()
            pickImage()
        }

        binding.btnSave.setOnClickListener {
            userViewModel.currentUser.value?.nickname = binding.etProfileName.text.toString()

            userViewModel.uploadImageToFirebaseStorage {
                // 업로드 완료 후 updateCurrentUserInfo 호출
                userViewModel.currentUser.value?.allergy = selectedAllergies
                userViewModel.updateCurrentUserInfo()

                val myPageFragment = requireActivity().supportFragmentManager.findFragmentByTag("MyPageFragment")
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    hide(this@EditProfileFragment)
                    if (myPageFragment == null) {
                        add(R.id.main_frame, MyPageFragment(), "MyPageFragment")
                    } else {
                        show(myPageFragment)
                    }
                    addToBackStack(null)
                    commit()
                }
                Toast.makeText(requireContext(), "프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun setAllergyFilter(allergen: CheckBox, allergenName: String){
        allergen.setOnCheckedChangeListener{ _, isChecked ->
            if (allergen.isChecked) {
                if (selectedAllergies.size >= 7) {
                    Toast.makeText(requireContext(), "최대 7개까지 설정 가능합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    selectedAllergies.add(allergenName)
                }
            } else {
                selectedAllergies.remove(allergenName)
            }
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
}
