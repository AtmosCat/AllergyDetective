package com.allergyguardian.allergyguardian.presentation.mypage.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.model.franchise.Menu
import com.allergyguardian.allergyguardian.databinding.FragmentFavoriteBinding
import com.allergyguardian.allergyguardian.presentation.FranchiseViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel
import com.allergyguardian.allergyguardian.presentation.franchise.franchise_detail.FranchiseDetailFragment
import com.allergyguardian.allergyguardian.presentation.itemdetail.ItemDetailFragment

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var clickedItem: Menu? = null

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    private val favoriteListAdapter by lazy { FavoriteListAdapter(userViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewFavoriteList.adapter = favoriteListAdapter
        binding.recyclerviewFavoriteList.layoutManager = LinearLayoutManager(requireContext())

        var currentUserFavorites = mutableListOf<Menu>()

        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                currentUserFavorites = data.like
                favoriteListAdapter.submitList(currentUserFavorites)
            } else {
                currentUserFavorites = emptyList<Menu>().toMutableList()
                Toast.makeText(requireContext(), "좋아요한 상품이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        favoriteListAdapter.itemClick = object :FavoriteListAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
                    if (data != null) {
                        currentUserFavorites = data.like
                        clickedItem = currentUserFavorites[position]
                        val franchiseDetailFragment = requireActivity().supportFragmentManager.findFragmentByTag("FranchiseDetailFragment")
                        val dataToSend = clickedItem!!.id
                        val dataToSend2 = clickedItem!!.type
                        val menuDetail = FranchiseDetailFragment.newInstance(dataToSend, dataToSend2)
                        requireActivity().supportFragmentManager.beginTransaction().apply {
                            hide(this@FavoriteFragment)
                            if (franchiseDetailFragment == null) {
                                add(R.id.main_frame, menuDetail, "FranchiseDetailFragment")
                            } else {
                                show(menuDetail)
                            }
                            addToBackStack(null)
                            commit()
                        }
                    }

                }
            }
        }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        var updatedCurrentUserFavorites = mutableListOf<Menu>()
        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                updatedCurrentUserFavorites = data.like
                favoriteListAdapter.updateData(updatedCurrentUserFavorites)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        var updatedCurrentUserFavorites = mutableListOf<Menu>()
        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                updatedCurrentUserFavorites = data.like
                favoriteListAdapter.updateData(updatedCurrentUserFavorites)
            }
        }
    }
}
