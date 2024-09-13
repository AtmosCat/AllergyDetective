package com.example.allergydetective.presentation.community.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.allergydetective.data.model.user.Comments
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.data.model.user.Reply
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.databinding.FragmentReplyDetailBinding
import com.example.allergydetective.presentation.PostViewModel
import com.example.allergydetective.presentation.UserViewModel
import com.example.allergydetective.presentation.community.postdetail.reply.RepliesAdapter
import com.google.firebase.firestore.FieldValue
import java.util.UUID

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReplyDetailFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentReplyDetailBinding? = null
    private val binding get() = _binding!!

    private val repliesAdapter by lazy { RepliesAdapter() }

    private var currentUser = User()

    private var clickedItem = Post()

    private var clickedComment = Comments()

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    private val postViewModel: PostViewModel by activityViewModels {
        viewModelFactory { initializer { PostViewModel(requireActivity().application) } }
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
        _binding = FragmentReplyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            ReplyDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewCommentReplies.adapter = repliesAdapter
        binding.recyclerviewCommentReplies.layoutManager = LinearLayoutManager(requireContext())

        currentUser = userViewModel.currentUser.value!!

        val clickedItemId = param1
        val clickedCommentId = param2
        postViewModel.filteredPosts.observe(viewLifecycleOwner) { filteredPosts ->
            clickedItem = filteredPosts.find { it.id == clickedItemId }!!
            clickedComment = clickedItem.comments.find { it.id == clickedCommentId }!!

            binding.btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            binding.ivCommenter.load(clickedComment.commenterPhoto)
            binding.tvCommenter.text = clickedComment.commenterNickname
            binding.tvCommentDetail.text = clickedItem.detail

            repliesAdapter.submitList(clickedComment.reply)
            binding.btnAddReply.setOnClickListener{
                var newReply = Reply(
                    replierEmail = currentUser.email,
                    replierPhoto = currentUser.photo,
                    replierNickname = currentUser.nickname,
                    detail = binding.etAddReply.text.toString()
                )
                postViewModel.addReply(
                    clickedItemId!!,
                    clickedCommentId!!,
                    newReply)
                Toast.makeText(this.requireContext(), "답글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                binding.etAddReply.setText("")
            }
        }

    }
}

