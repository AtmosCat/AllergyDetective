package com.allergyguardian.allergyguardian.presentation.franchise.franchise_detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.load
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.model.food.Food
import com.allergyguardian.allergyguardian.data.model.franchise.Menu
import com.allergyguardian.allergyguardian.databinding.FragmentFranchiseDetailBinding
import com.allergyguardian.allergyguardian.presentation.FranchiseViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel

const val ARG_PARAM1 = "param1"
const val ARG_PARAM2 = "param2"

class FranchiseDetailFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentFranchiseDetailBinding? = null
    private val binding get() = _binding!!

    private var clickedMenu = Menu()
    private var allMenus = listOf<Menu>()

    private val franchiseViewModel: FranchiseViewModel by activityViewModels {
        viewModelFactory { initializer { FranchiseViewModel(requireActivity().application) } }
    }

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFranchiseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) = FranchiseDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        val clickedMenuId = param1

        allMenus = franchiseViewModel.allMenus.value!!
        clickedMenu = allMenus.find { it.id == clickedMenuId }!!

        when (clickedMenu.type) {
            "패스트푸드" -> binding.ivMenu.setImageResource(R.drawable.hamburger)
            "피자" -> binding.ivMenu.setImageResource(R.drawable.pizza)
            "치킨" -> binding.ivMenu.setImageResource(R.drawable.chicken)
            "카페" -> binding.ivMenu.setImageResource(R.drawable.coffee)
            "아이스크림" -> binding.ivMenu.setImageResource(R.drawable.ice_cream)
            "베이커리/도넛" -> binding.ivMenu.setImageResource(R.drawable.doughnut)
            "샌드위치" -> binding.ivMenu.setImageResource(R.drawable.sandwich)
        }
        binding.tvBrand.text = clickedMenu.brand
        binding.tvSubcat.text = clickedMenu.subcat
        binding.tvName.text = clickedMenu.name
        binding.tvAllergy.text = "⚠️ 알러지유발물질: ${clickedMenu.allergy}"
        binding.tvDate.text = "업데이트 일자: ${clickedMenu.date}"

        binding.btnMenuLink.setOnClickListener{
            if (clickedMenu.url.isEmpty() || clickedMenu.url.isBlank()) {
                Toast.makeText(requireContext(), "연결 가능한 URL이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(clickedMenu.url)
                startActivity(intent)
            }
        }

        var isLiked = false
        var currentUserFavorites = mutableListOf<Menu>()
        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                currentUserFavorites = data.like
                if (clickedMenu in currentUserFavorites) {
                    binding.btnLike.setImageResource(R.drawable.filled_heart)
                    isLiked = true
                }
            }
        }

        binding.btnLike.setOnClickListener {
            if (!isLiked) {
                isLiked = true
                binding.btnLike.setImageResource(R.drawable.filled_heart)
                userViewModel.currentUser.value?.like!!.add(clickedMenu)
                userViewModel.updateCurrentUserInfo()
            } else {
                isLiked = false
                binding.btnLike.setImageResource(R.drawable.heart)
                userViewModel.currentUser.value?.like!!.remove(clickedMenu)
                userViewModel.updateCurrentUserInfo()
            }
        }


    }


}

