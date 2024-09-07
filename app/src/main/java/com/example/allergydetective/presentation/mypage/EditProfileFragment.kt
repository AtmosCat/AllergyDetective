package com.example.allergydetective.presentation.home


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
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
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.data.model.user.sampleBitmap
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentEditProfileBinding
import com.example.allergydetective.databinding.FragmentMyPageBinding
import com.example.allergydetective.presentation.SharedViewModel
import com.example.allergydetective.presentation.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import java.io.ByteArrayOutputStream


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null

    private val binding get() = _binding!!

    private var selectedAllergies = mutableListOf<String>()

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    private lateinit var profileImageView: ImageView
    private lateinit var profileImage : Bitmap

//    private val getResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val intent = result.data
//                val bitmap = intent?.data?.let { uri ->
//                    requireActivity().contentResolver.openInputStream(uri)?.use { inputStream ->
//                        BitmapFactory.decodeStream(inputStream)
//                    }
//                }
//                bitmap?.let {
//                    profileImageView.setImageBitmap(it)
//                    profileImage = bitmap
//                }
//            }
//        }

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

        val allergies = listOf(egg, milk, buckwheat, peanut, soybean, wheat, mackerel,
            crab, shrimp, pork, peach, tomato, sulfurousAcids, walnut, chicken, beef, squid, seashell, pinenut)

        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data?.photo == "") {
                profileImageView.setImageBitmap(sampleBitmap)
            } else {
                userViewModel.getDownloadUrl(
                    onSuccess = { downloadUrl ->
                        // 이미지 로드
                        binding.ivProfileImage.load(downloadUrl) {
//                            crossfade(true)
//                            placeholder(R.drawable.placeholder) // 로딩 중에 표시할 이미지
//                            error(sampleBitmap) // 로드 실패 시 표시할 이미지
                        }
                    },
                    onFailure = { exception ->
                        // 실패 처리
                        binding.ivProfileImage.load(sampleBitmap)
                    })
            }

            binding.etProfileName.setText(data!!.nickname)

            val currentUserAllergies = data?.allergy

            if (currentUserAllergies != null) {
                for (allergy in currentUserAllergies) {
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

            if (currentUserAllergies != null) {
                selectedAllergies = currentUserAllergies
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