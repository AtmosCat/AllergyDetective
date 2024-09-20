package com.example.allergydetective.presentation.mypage.mypost

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
import com.example.allergydetective.databinding.FragmentMyPostBinding
import com.example.allergydetective.presentation.UserViewModel
import com.example.allergydetective.presentation.community.postdetail.PostDetailFragment

class MyPostFragment : Fragment() {

    private var _binding: FragmentMyPostBinding? = null
    private val binding get() = _binding!!

    private var clickedItem: Post? = null

    private val myPostAdapter by lazy { MyPostAdapter() }

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
        _binding = FragmentMyPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewMypostList.adapter = myPostAdapter
        binding.recyclerviewMypostList.layoutManager = LinearLayoutManager(requireContext())

        var currentUserMyPosts = mutableListOf<Post>()

        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                currentUserMyPosts = data.mypost
                myPostAdapter.submitList(currentUserMyPosts)
            } else {
                currentUserMyPosts = emptyList<Post>().toMutableList()
                Toast.makeText(requireContext(), "내가 쓴 글이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        myPostAdapter.itemClick = object : MyPostAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
                    if (data != null) {
                        currentUserMyPosts = data.mypost
                        clickedItem = currentUserMyPosts[position]

                        val postDetailFragment = requireActivity().supportFragmentManager.findFragmentByTag("PostDetailFragment")
                        val dataToSend = clickedItem!!.id
                        val postDetail = PostDetailFragment.newInstance(dataToSend)
                        requireActivity().supportFragmentManager.beginTransaction().apply {
                            hide(this@MyPostFragment)
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
        var updatedCurrentUserMyposts = mutableListOf<Post>()
        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                updatedCurrentUserMyposts = data.mypost
                myPostAdapter.updateData(updatedCurrentUserMyposts)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        var updatedCurrentUserMyposts = mutableListOf<Post>()
        userViewModel.currentUser.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                updatedCurrentUserMyposts = data.mypost
                myPostAdapter.updateData(updatedCurrentUserMyposts)
            }
        }
    }
}
