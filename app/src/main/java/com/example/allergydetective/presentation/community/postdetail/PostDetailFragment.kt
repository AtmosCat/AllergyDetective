package com.example.allergydetective.presentation.community.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.Comments
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.databinding.FragmentPostDetailBinding
import com.example.allergydetective.presentation.PostViewModel
import com.example.allergydetective.presentation.UserViewModel
import com.example.allergydetective.presentation.community.postdetail.reply.RepliesAdapter
import com.example.allergydetective.presentation.itemdetail.ViewPagerAdapter

private const val ARG_PARAM1 = "param1"
class PostDetailFragment : Fragment() {
    private var param1: String? = null

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val commentsAdapter by lazy { CommentsAdapter() }
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
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(param1: String) =
            PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewComments.adapter = commentsAdapter
        binding.recyclerviewComments.layoutManager = LinearLayoutManager(requireContext())

        currentUser = userViewModel.currentUser.value!!

        val clickedItemId = param1
        postViewModel.filteredPosts.observe(viewLifecycleOwner) { filteredPosts ->
            clickedItem = filteredPosts.find { it.id == clickedItemId }!!

            binding.btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            binding.btnReport.setOnClickListener{
                // 신고
            }

            binding.tvCategory.text = "주제: ${clickedItem.category}"

            val viewPager = binding.viewPager

            val imageResources = clickedItem.detailPhoto
            val viewPagerAdapter = ViewPagerAdapter(imageResources)
            viewPager.adapter = viewPagerAdapter

            binding.ivPoster.load(clickedItem.posterPhoto)
            binding.tvPoster.text = clickedItem.posterName
            binding.tvTitle.text = clickedItem.title
            binding.tvDetail.text = clickedItem.detail

            var isScrapped = false
            if (clickedItem in currentUser.scrap) {
                isScrapped = true
                binding.ivScrap.setImageResource(R.drawable.scrap_filled)
            } else {
                isScrapped = false
                binding.ivScrap.setImageResource(R.drawable.scrap)
            }

            binding.tvCommentTitle.text = "댓글 ${clickedItem.comments.size}"

            binding.ivScrap.setOnClickListener {
                if (!isScrapped) {
                    isScrapped = true
                    binding.ivScrap.setImageResource(R.drawable.scrap_filled)
                    userViewModel.currentUser.value?.scrap!!.add(clickedItem)
                    userViewModel.updateCurrentUserInfo()
                } else {
                    isScrapped = false
                    binding.ivScrap.setImageResource(R.drawable.scrap)
                    userViewModel.currentUser.value?.scrap!!.remove(clickedItem)
                    userViewModel.updateCurrentUserInfo()
                }
            }

            commentsAdapter.submitList(clickedItem.comments)

            val replyDetailFragment = requireActivity().supportFragmentManager.findFragmentByTag("ReplyDetailFragment")
            commentsAdapter.itemClick = object : CommentsAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    clickedComment = clickedItem.comments[position]
                    val dataToSend1 = clickedItemId!!
                    val dataToSend2 = clickedComment.id
                    val replyDetail = ReplyDetailFragment.newInstance(dataToSend1, dataToSend2)
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        hide(this@PostDetailFragment)
                        if (replyDetailFragment == null) {
                            add(R.id.main_frame, replyDetail, "ReplyDetailFragment")
                        } else {
                            show(replyDetail)
                        }
                        addToBackStack(null)
                        commit()
                    }
                }
            }

            binding.btnAddComment.setOnClickListener{
                postViewModel.addComment(clickedItemId!!, binding.etAddComment.text.toString())
            }


        }

    }
}

