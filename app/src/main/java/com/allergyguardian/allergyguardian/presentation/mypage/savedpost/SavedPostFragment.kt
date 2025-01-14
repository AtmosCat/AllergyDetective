package com.allergyguardian.allergyguardian.presentation.mypage.savedpost

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
import com.allergyguardian.allergyguardian.data.model.user.Post
import com.allergyguardian.allergyguardian.databinding.FragmentSavedPostBinding
import com.allergyguardian.allergyguardian.presentation.UserViewModel
import com.allergyguardian.allergyguardian.presentation.community.postdetail.PostDetailFragment

class SavedPostFragment : Fragment() {

    private var _binding: FragmentSavedPostBinding? = null
    private val binding get() = _binding!!

    private var clickedItem: Post? = null

    private val savedPostAdapter by lazy { SavedPostAdapter() }

    private var currentUserSavedPosts = mutableListOf<Post>()

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

        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            currentUserSavedPosts = data!!.scrap
        }

        if (currentUserSavedPosts.size != 0) {
            savedPostAdapter.submitList(currentUserSavedPosts)
        } else {
            Toast.makeText(requireContext(), "내가 저장한 글이 없습니다.", Toast.LENGTH_SHORT).show()
        }

        savedPostAdapter.itemClick = object : SavedPostAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
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

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    override fun onResume() {
        super.onResume()
        savedPostAdapter.updateData(currentUserSavedPosts)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        savedPostAdapter.updateData(currentUserSavedPosts)
    }
}
