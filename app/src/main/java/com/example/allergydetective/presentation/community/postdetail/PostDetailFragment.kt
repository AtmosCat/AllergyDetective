package com.example.allergydetective.presentation.community.postdetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import coil.load
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.Comments
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.data.model.user.Reply
import com.example.allergydetective.data.model.user.Report
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.data.model.user.sampleBitmap
import com.example.allergydetective.databinding.FragmentPostDetailBinding
import com.example.allergydetective.presentation.PostViewModel
import com.example.allergydetective.presentation.UserViewModel
import com.example.allergydetective.presentation.community.community_home.CommunityHomeAdapter
import com.example.allergydetective.presentation.community.editpost.EditPostFragment
import com.example.allergydetective.presentation.community.postdetail.reply.RepliesAdapter
import com.example.allergydetective.presentation.home.HomeFragment
import com.example.allergydetective.presentation.itemdetail.ViewPagerAdapter
import com.google.firebase.firestore.FieldValue
import java.util.UUID

private const val ARG_PARAM1 = "param1"
class PostDetailFragment : Fragment() {
    private var param1: String? = null

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val commentsAdapter by lazy { CommentsAdapter() }
    private val repliesAdapter by lazy { RepliesAdapter() }

    private var currentUser = User()

    private var clickedItem = Post()

    private var clickedItemId = ""

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

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        clickedItemId = param1.toString()
        val clickedItem2 = postViewModel.filteredPosts.value!!.find { it.id == clickedItemId }
        postViewModel.filteredPosts.observe(viewLifecycleOwner) { filteredPosts ->
            var filteredPosts = postViewModel.filteredPosts.value!!
            clickedItem = filteredPosts.find { it.id == clickedItemId }!!

            binding.btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            binding.tvCategory.text = "주제: ${clickedItem.category}"

            val reportDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_report_post, null)
            val etReportReason = reportDialogView.findViewById<EditText>(R.id.et_report_post_reason)
            val etReportDetail = reportDialogView.findViewById<EditText>(R.id.et_report_post_detail)

            if (clickedItem.posterEmail != currentUser.email) {
                for (mypost in currentUser.mypost) {
                    binding.btnMenu.setOnClickListener { view ->
                        val popupMenu = PopupMenu(requireContext(), view)
                        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                            when (item.itemId) {
                                R.id.action_report -> {
                                    AlertDialog.Builder(requireContext())
                                        .setView(reportDialogView)
                                        .setPositiveButton("제출") { dialog, _ ->
                                            val userReportReason = etReportReason.text.toString()
                                            val userReportDetail = etReportDetail.text.toString()
                                            val newReport = Report(
                                                type = "Post",
                                                postId = clickedItem.id,
                                                reporterEmail = currentUser.email,
                                                reporteeEmail = clickedItem.posterEmail,
                                                reportReason = userReportReason,
                                                reportDetail = userReportDetail
                                            )
                                            postViewModel.sendReport(newReport)
                                            Toast.makeText(
                                                this.requireContext(),
                                                "신고가 접수되었습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog.dismiss()
                                        }
                                        .setNegativeButton("취소") { dialog, which ->
                                            dialog.dismiss()
                                        }
                                        .show()
                                    true
                                }

                                else -> {
                                    false
                                }
                            }
                        }
                        popupMenu.show()
                    }
                }
            } else {
                binding.btnMenu.setOnClickListener { view ->
                    val popupMenu = PopupMenu(requireContext(), view)
                    popupMenu.menuInflater.inflate(R.menu.popup_menu_mine, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                        when (item.itemId) {
                            R.id.action_edit -> {
                                val editPostFragment = requireActivity().supportFragmentManager.findFragmentByTag("EditPostFragment")
                                val dataToSend = clickedItem.id
                                val editPost = EditPostFragment.newInstance(dataToSend)
                                requireActivity().supportFragmentManager.beginTransaction().apply {
                                    hide(this@PostDetailFragment)
                                    if (editPostFragment == null) {
                                        add(R.id.main_frame, editPost, "EditPostFragment")
                                    } else {
                                        show(editPost)
                                    }
                                    addToBackStack(null)
                                    commit()
                                }
                                true
                            }
                            R.id.action_delete -> {
                                AlertDialog.Builder(requireContext())
                                    .setTitle("게시글 삭제하기")
                                    .setMessage("게시글을 삭제하시겠습니까?")
                                    .setPositiveButton("삭제") { dialog, _ ->
                                        postViewModel.deletePost(clickedItem.id)
                                        var newItems = postViewModel.filteredPosts.value!!.sortedBy { it.timestamp }
                                        CommunityHomeAdapter().updateData(newItems)
                                        userViewModel.deleteMyPost(currentUser.email, clickedItem)
                                        Toast.makeText(this.requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                        dialog.dismiss()
                                        requireActivity().supportFragmentManager.popBackStack()
//                                        val homeFragment = requireActivity().supportFragmentManager.findFragmentByTag("HomeFragment")
//                                        requireActivity().supportFragmentManager.beginTransaction().apply {
//                                            hide(this@PostDetailFragment)
//                                            if (homeFragment == null) {
//                                                add(R.id.main_frame, HomeFragment(), "HomeFragment")
//                                            } else {
//                                                show(homeFragment)
//                                            }
//                                            addToBackStack(null)
//                                            commit()
//                                        }
                                    }
                                    .setNegativeButton("취소") { dialog, which ->
                                        dialog.dismiss()
                                    }
                                    .show()
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    }
                    popupMenu.show()
                }
            }


            val viewPager = binding.viewPager

            val imageResources = clickedItem.detailPhoto
            if (imageResources.isEmpty()) {
                binding.tvNoPhotos.visibility = View.VISIBLE
            }

            val viewPagerAdapter = ViewPagerAdapter(imageResources)
            viewPager.adapter = viewPagerAdapter

            viewPager.registerOnPageChangeCallback(object: OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tvPhotoNumber.text = "${position + 1} / ${imageResources.size}"
                }
            })


            if (clickedItem.posterPhoto.isEmpty()) {
                binding.ivPoster.setImageBitmap(sampleBitmap)
            } else {
                postViewModel.getPosterPhotoUrl(
                    clickedItemId = clickedItemId!!,
                    onSuccess = { downloadUrl ->
                        binding.ivPoster.load(downloadUrl) {
                        //                            listener(onSuccess = {
//
//                            },
//                                onError = {
//
//                                })
                        }
                    },
                    onFailure = { exception ->
                        binding.ivPoster.setImageBitmap(sampleBitmap)
                    })
            }

            binding.tvPoster.text = clickedItem.posterNickname
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
                    clickedItem.scrap += 1
                    userViewModel.currentUser.value?.scrap!!.add(clickedItem)
                    userViewModel.updateCurrentUserInfo()
                    postViewModel.updateCurrentPostInfo(clickedItem)
                } else {
                    isScrapped = false
                    binding.ivScrap.setImageResource(R.drawable.scrap)
                    clickedItem.scrap -= 1
                    userViewModel.currentUser.value?.scrap!!.remove(clickedItem)
                    userViewModel.updateCurrentUserInfo()
                    postViewModel.updateCurrentPostInfo(clickedItem)
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
                val newComment = Comments(
                    commenterEmail = currentUser.email,
                    commenterPhoto = currentUser.photo,
                    commenterNickname = currentUser.nickname,
                    detail = binding.etAddComment.text.toString()
                )
                postViewModel.addComment(clickedItemId!!, newComment)
                Toast.makeText(this.requireContext(), "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                binding.etAddComment.setText("")
            }
        }

    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        var filteredPosts = postViewModel.filteredPosts.value!!
        clickedItem = filteredPosts.find { it.id == clickedItemId }!!
    }
    override fun onResume() {
        super.onResume()
        var filteredPosts = postViewModel.filteredPosts.value!!
        clickedItem = filteredPosts.find { it.id == clickedItemId }!!
    }
}

