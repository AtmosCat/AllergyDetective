package com.example.allergydetective.presentation.mypage.savedpost

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
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.data.repository.food.GonggongFoodRepositoryImpl
import com.example.allergydetective.data.repository.market.MarketRepositoryImpl
import com.example.allergydetective.databinding.FragmentSavedPostBinding
import com.example.allergydetective.presentation.SharedViewModel
import com.example.allergydetective.presentation.UserViewModel
import com.example.allergydetective.presentation.community.postdetail.PostDetailFragment

class SavedPostFragment : Fragment() {

    private var _binding: FragmentSavedPostBinding? = null
    private val binding get() = _binding!!

    private var clickedItem: Post? = null

    private val savedPostAdapter by lazy { SavedPostAdapter() }

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
        _binding = FragmentSavedPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewSavedPostList.adapter = savedPostAdapter
        binding.recyclerviewSavedPostList.layoutManager = LinearLayoutManager(requireContext())

        var currentUserSavedPosts = mutableListOf<Post>()

        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                currentUserSavedPosts = data.scrap
                savedPostAdapter.submitList(currentUserSavedPosts)
            } else {
                currentUserSavedPosts = emptyList<Post>().toMutableList()
                Toast.makeText(requireContext(), "내가 쓴 글이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        savedPostAdapter.itemClick = object : SavedPostAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
                    if (data != null) {
                        currentUserSavedPosts = data.scrap
                        clickedItem = currentUserSavedPosts[position]

                        val postDetailFragment = requireActivity().supportFragmentManager.findFragmentByTag("PostDetailFragment")
                        val dataToSend = clickedItem!!.id
                        val postDetail = PostDetailFragment.newInstance(dataToSend)
                        requireActivity().supportFragmentManager.beginTransaction().apply {
                            hide(this@SavedPostFragment)
                            if (postDetailFragment == null) {
                                add(R.id.main_frame, postDetail, "PostDetailFragment")
                            } else {
                                show(postDetail)
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
        var updatedCurrentUserSavedPosts = mutableListOf<Post>()
        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                updatedCurrentUserSavedPosts = data.scrap
                savedPostAdapter.updateData(updatedCurrentUserSavedPosts)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        var updatedCurrentUserSavedPosts = mutableListOf<Post>()
        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                updatedCurrentUserSavedPosts = data.scrap
                savedPostAdapter.updateData(updatedCurrentUserSavedPosts)
            }
        }
    }
}