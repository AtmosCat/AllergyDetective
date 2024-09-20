package com.example.allergydetective.presentation.home

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.load
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.Report
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.data.model.user.sampleBitmap
import com.example.allergydetective.databinding.FragmentMyPageBinding
import com.example.allergydetective.presentation.UserViewModel
import com.example.allergydetective.presentation.community.community_home.CommunityHomeFragment
import com.example.allergydetective.presentation.mypage.GroupManagerFragment
import com.example.allergydetective.presentation.mypage.favorite.FavoriteFragment
import com.example.allergydetective.presentation.mypage.mypost.MyPostFragment
import com.example.allergydetective.presentation.mypage.savedpost.SavedPostFragment
import com.example.allergydetective.presentation.signin.SignInFragment


class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null

    private val binding get() = _binding!!

    private var currentUser = User()

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
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser = userViewModel.currentUser.value!!

        binding.btnMypageSettings.setOnClickListener{
            val popupMenu = PopupMenu(requireContext(), binding.btnMypageSettings)
            popupMenu.menuInflater.inflate(R.menu.popup_menu_settings, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.action_signout -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("로그아웃")
                            .setMessage("로그아웃하시겠습니까?")
                            .setPositiveButton("확인") { dialog, _ ->
                                userViewModel.signOut()
                                Toast.makeText(requireContext(),"로그아웃되었습니다.",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                                val signInFragment = requireActivity().supportFragmentManager.findFragmentByTag("SignInFragment")
                                requireActivity().supportFragmentManager.beginTransaction().apply {
                                    remove(this@MyPageFragment)
                                    if (signInFragment == null) {
                                        add(R.id.main_frame, SignInFragment(), "SignInFragment")
                                    } else {
                                        show(signInFragment)
                                    }
                                    addToBackStack(null)
                                    commit()
                                }
                            }
                            .setNegativeButton("취소") { dialog, which ->
                                dialog.dismiss()
                            }
                            .show()
                        true
                    }
                    R.id.action_delete_id -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("회원탈퇴")
                            .setMessage("회원탈퇴하시겠습니까?\n삭제된 계정 정보는 복구하실 수 없습니다.")
                            .setPositiveButton("확인") { dialog, _ ->
                                userViewModel.deleteID(currentUser)
                                Toast.makeText(requireContext(),"회원탈퇴되었습니다.",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                                val signInFragment = requireActivity().supportFragmentManager.findFragmentByTag("SignInFragment")
                                requireActivity().supportFragmentManager.beginTransaction().apply {
                                    remove(this@MyPageFragment)
                                    if (signInFragment == null) {
                                        add(R.id.main_frame, SignInFragment(), "SignInFragment")
                                    } else {
                                        show(signInFragment)
                                    }
                                    addToBackStack(null)
                                    commit()
                                }
                            }
                            .setNegativeButton("취소") { dialog, which ->
                                dialog.dismiss()
                            }
                            .show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        val editProfileFragment = requireActivity().supportFragmentManager.findFragmentByTag("EditProfileFragment")
        binding.btnEditProfile.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@MyPageFragment)
                if (editProfileFragment == null) {
                    add(R.id.main_frame, EditProfileFragment(), "EditProfileFragment")
                } else {
                    show(editProfileFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val myAllergyFrame1 = binding.ivMyAllergyFrame
        val myAllergyFrame2 = binding.ivMyAllergyFrame2
        val myAllergyFrame3 = binding.ivMyAllergyFrame3
        val myAllergyFrame4 = binding.ivMyAllergyFrame4
        val myAllergyFrame5 = binding.ivMyAllergyFrame5
        val myAllergyFrame6 = binding.ivMyAllergyFrame6
        val myAllergyFrame7 = binding.ivMyAllergyFrame7

        val myAllergyFrameList = mutableListOf(myAllergyFrame1, myAllergyFrame2, myAllergyFrame3,
            myAllergyFrame4, myAllergyFrame5, myAllergyFrame6, myAllergyFrame7)

        val myAllergy1 = binding.ivMyAllergy
        val myAllergy2 = binding.ivMyAllergy2
        val myAllergy3 = binding.ivMyAllergy3
        val myAllergy4 = binding.ivMyAllergy4
        val myAllergy5 = binding.ivMyAllergy5
        val myAllergy6 = binding.ivMyAllergy6
        val myAllergy7 = binding.ivMyAllergy7

        val myAllergyList = mutableListOf(myAllergy1, myAllergy2, myAllergy3,
            myAllergy4, myAllergy5, myAllergy6, myAllergy7)

        for (frame in myAllergyFrameList) {
            frame.isInvisible = true
        }

        for (allergy in myAllergyList) {
            allergy.isInvisible = true
        }

        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data?.photo == "") {
                binding.ivProfileImage.setImageBitmap(sampleBitmap)
            } else {
                userViewModel.getDownloadUrl(
                    onSuccess = { downloadUrl ->
                        // 이미지 로드
                        binding.ivProfileImage.load(downloadUrl) {
                            crossfade(true)
                            placeholder(R.drawable.placeholder) // 로딩 중에 표시할 이미지
                            error(R.drawable.error_image) // 로드 실패 시 표시할 이미지
                        }
                    },
                    onFailure = { exception ->
                        // 실패 처리
                        binding.ivProfileImage.setImageResource(R.drawable.error_image)
                    })
            }
            binding.tvProfileName.text = data?.nickname
            binding.tvMyAllergies.text = "⚠️ 나의 알러지 성분: ${data?.allergy}"

            val currentUserAllergiesCount = data?.allergy?.size

            if (currentUserAllergiesCount != null) {
                if (currentUserAllergiesCount >= 1) {
                    for(i in 0..currentUserAllergiesCount - 1) {
                        myAllergyFrameList[i].isVisible = true
                        myAllergyList[i].isVisible = true
                        setAllergyImage(myAllergyList[i], data.allergy[i])
                    }
                }
            }
        }

        val favoriteFragment = requireActivity().supportFragmentManager.findFragmentByTag("FavoriteFragment")
        binding.viewMypageMenu1.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@MyPageFragment)
                if (favoriteFragment == null) {
                    add(R.id.main_frame, FavoriteFragment(), "FavoriteFragment")
                } else {
                    show(favoriteFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val groupManagerFragment = requireActivity().supportFragmentManager.findFragmentByTag("GroupManagerFragment")
        binding.viewMypageMenu2.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@MyPageFragment)
                if (groupManagerFragment == null) {
                    add(R.id.main_frame, GroupManagerFragment(), "GroupManagerFragment")
                } else {
                    show(groupManagerFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

//        val membershipFragment = requireActivity().supportFragmentManager.findFragmentByTag("MembershipFragment")
//        binding.viewMypageMenu3.setOnClickListener{
//            requireActivity().supportFragmentManager.beginTransaction().apply {
//                hide(this@MyPageFragment)
//                if (membershipFragment == null) {
//                    add(R.id.main_frame, MembershipFragment(), "MembershipFragment")
//                } else {
//                    show(membershipFragment)
//                }
//                addToBackStack(null)
//                commit()
//            }
//        }

        val myPostFragment = requireActivity().supportFragmentManager.findFragmentByTag("MyPostFragment")
        binding.viewMypageMenu4.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@MyPageFragment)
                if (myPostFragment == null) {
                    add(R.id.main_frame, MyPostFragment(), "MyPostFragment")
                } else {
                    show(myPostFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val savedPostFragment = requireActivity().supportFragmentManager.findFragmentByTag("SavedPostFragment")
        binding.viewMypageMenu5.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@MyPageFragment)
                if (savedPostFragment == null) {
                    add(R.id.main_frame, SavedPostFragment(), "SavedPostFragment")
                } else {
                    show(savedPostFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val homeFragment = requireActivity().supportFragmentManager.findFragmentByTag("HomeFragment")
        binding.btnTabHome.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@MyPageFragment)
                if (homeFragment == null) {
                    add(R.id.main_frame, HomeFragment(), "HomeFragment")
                } else if(homeFragment != null && homeFragment.isHidden) {
                    show(homeFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val communityHomeFragment = requireActivity().supportFragmentManager.findFragmentByTag("CommunityHomeFragment")
        binding.btnTabCommunity.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@MyPageFragment)
                if (communityHomeFragment == null) {
                    add(R.id.main_frame, CommunityHomeFragment(), "CommunityHomeFragment")
                } else if (communityHomeFragment != null && communityHomeFragment.isHidden){
                    show(communityHomeFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

    }

//    override fun onResume() {
//        super.onResume()
//        userViewModel.currentUser.value.let { data ->
//            binding.ivProfileImage.setImageBitmap(data?.photo)
//            binding.tvProfileName.text = data?.name
//            binding.tvMyAllergies.text = "⚠️ 나의 알러지 성분: ${data?.allergy}"
//        }
//    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
                if (data?.photo == "") {
                    binding.ivProfileImage.setImageBitmap(sampleBitmap)
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
                    binding.tvProfileName.text = "익명의 고양이"
                } else {
                    binding.tvProfileName.text = data?.nickname
                }

                if (data?.allergy!!.isEmpty()) {
                    binding.tvMyAllergies.text = "프로필에서 나의 알러지 성분을 설정해주세요!"
                } else {
                    binding.tvMyAllergies.text = "⚠️ 나의 알러지 성분: ${data?.allergy}"
                }

                val currentUserAllergiesCount = data!!.allergy.size

                val myAllergyFrame1 = binding.ivMyAllergyFrame
                val myAllergyFrame2 = binding.ivMyAllergyFrame2
                val myAllergyFrame3 = binding.ivMyAllergyFrame3
                val myAllergyFrame4 = binding.ivMyAllergyFrame4
                val myAllergyFrame5 = binding.ivMyAllergyFrame5
                val myAllergyFrame6 = binding.ivMyAllergyFrame6
                val myAllergyFrame7 = binding.ivMyAllergyFrame7

                val myAllergyFrameList = mutableListOf(myAllergyFrame1, myAllergyFrame2, myAllergyFrame3,
                    myAllergyFrame4, myAllergyFrame5, myAllergyFrame6, myAllergyFrame7)

                val myAllergy1 = binding.ivMyAllergy
                val myAllergy2 = binding.ivMyAllergy2
                val myAllergy3 = binding.ivMyAllergy3
                val myAllergy4 = binding.ivMyAllergy4
                val myAllergy5 = binding.ivMyAllergy5
                val myAllergy6 = binding.ivMyAllergy6
                val myAllergy7 = binding.ivMyAllergy7

                val myAllergyList = mutableListOf(myAllergy1, myAllergy2, myAllergy3,
                    myAllergy4, myAllergy5, myAllergy6, myAllergy7)

                for (frame in myAllergyFrameList) {
                    frame.isInvisible = true
                }

                for (allergy in myAllergyList) {
                    allergy.isInvisible = true
                }

                if (currentUserAllergiesCount != null) {
                    if (currentUserAllergiesCount >= 1) {
                        for(i in 0..currentUserAllergiesCount - 1) {
                            myAllergyFrameList[i].isVisible = true
                            myAllergyList[i].isVisible = true
                            setAllergyImage(myAllergyList[i], data.allergy[i])
                        }
                    }
                }
            }
        }
    }

    private fun setAllergyImage(image: ImageView, allergy: String) {
        when (allergy) {
            "알류(가금류)" -> image.setImageResource(R.drawable.egg)
            "우유" -> image.setImageResource(R.drawable.milk)
            "메밀" -> image.setImageResource(R.drawable.buckwheat)
            "땅콩" -> image.setImageResource(R.drawable.peanut)
            "대두" -> image.setImageResource(R.drawable.soybean)
            "밀" -> image.setImageResource(R.drawable.wheat)
            "고등어" -> image.setImageResource(R.drawable.mackerel)
            "게" -> image.setImageResource(R.drawable.crab)
            "새우" -> image.setImageResource(R.drawable.shrimp)
            "돼지고기" -> image.setImageResource(R.drawable.pork)
            "복숭아" -> image.setImageResource(R.drawable.peach)
            "토마토" -> image.setImageResource(R.drawable.tomato)
            "아황산류" -> image.setImageResource(R.drawable.sulfurous_acids)
            "호두" -> image.setImageResource(R.drawable.walnut)
            "닭고기" -> image.setImageResource(R.drawable.chicken)
            "쇠고기" -> image.setImageResource(R.drawable.beef)
            "오징어" -> image.setImageResource(R.drawable.squid)
            "조개류(조개)" -> image.setImageResource(R.drawable.seashell)
            "잣" -> image.setImageResource(R.drawable.pinenut)
            "조개류(굴)" -> image.setImageResource(R.drawable.oyster)
            "조개류(전복)" -> image.setImageResource(R.drawable.abalone)
            "조개류(홍합)" -> image.setImageResource(R.drawable.mussel)
        }
    }

}
