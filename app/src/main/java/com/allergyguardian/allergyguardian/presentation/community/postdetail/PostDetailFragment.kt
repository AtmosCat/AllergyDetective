package com.allergyguardian.allergyguardian.presentation.community.postdetail

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import coil.load
import com.allergyguardian.allergyguardian.R
import com.allergyguardian.allergyguardian.data.model.user.Comments
import com.allergyguardian.allergyguardian.data.model.user.Post
import com.allergyguardian.allergyguardian.data.model.user.Report
import com.allergyguardian.allergyguardian.data.model.user.User
import com.allergyguardian.allergyguardian.data.model.user.sampleBitmap
import com.allergyguardian.allergyguardian.databinding.FragmentPostDetailBinding
import com.allergyguardian.allergyguardian.presentation.PostViewModel
import com.allergyguardian.allergyguardian.presentation.UserViewModel
import com.allergyguardian.allergyguardian.presentation.community.community_home.CommunityHomeAdapter
import com.allergyguardian.allergyguardian.presentation.community.editpost.EditPostFragment
import com.allergyguardian.allergyguardian.presentation.community.postdetail.reply.ReplyDetailFragment
import com.allergyguardian.allergyguardian.presentation.itemdetail.ViewPagerAdapter

private const val ARG_PARAM1 = "param1"
class PostDetailFragment : Fragment() {
    private var param1: String? = null

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val commentsAdapter by lazy { CommentsAdapter() }

    private var currentUser = User()

    private var clickedItem = Post()

    private var clickedItemId = ""

    private var clickedComment = Comments()

    private var allPosts = listOf<Post>()

    private var filteredPosts = listOf<Post>()

    private var selectedReportPostReason = ""

    private var selectedReportUserReason = ""

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

        postViewModel.getAllPosts()

        binding.recyclerviewComments.adapter = commentsAdapter
        binding.recyclerviewComments.layoutManager = LinearLayoutManager(requireContext())

        currentUser = userViewModel.currentUser.value!!

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        clickedItemId = param1.toString()
//        val clickedItem2 = postViewModel.filteredPosts.value!!.find { it.id == clickedItemId }
        postViewModel.allPosts.observe(viewLifecycleOwner) { data ->
            allPosts = data
            clickedItem = allPosts.find { it.id == clickedItemId }!!

            val comments = clickedItem.comments.toMutableList()
            commentsAdapter.submitList(comments)

            binding.btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            binding.tvCategory.text = "주제: ${clickedItem.category}"

            // 글 신고
            val reportPostDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_report_post, null)
            val etReportPostDetail = reportPostDialogView.findViewById<EditText>(R.id.et_report_post_detail)
            val spinnerReportPostReason = reportPostDialogView.findViewById<Spinner>(R.id.spinner_report_post_reason)
            val reportPostReasonList = arrayOf(
                "도용",
                "비방 및 욕설",
                "원치 않는 상업성 게시물",
                "증오심 표현 및 노골적인 폭력",
                "잘못된 정보",
                "기타")
            val spinnerReportPostAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, reportPostReasonList)
            spinnerReportPostReason.adapter = spinnerReportPostAdapter
            spinnerReportPostReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                    selectedReportPostReason = parent?.getItemAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            // 유저 신고
            val reportUserDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_report_user, null)
            val etReportUserDetail = reportUserDialogView.findViewById<EditText>(R.id.et_report_user_detail)
            val spinnerReportUserReason = reportUserDialogView.findViewById<Spinner>(R.id.spinner_report_user_reason)
            val reportUserReasonList = arrayOf(
                "도용",
                "비방 및 욕설",
                "미성년자 대상 유해한 내용",
                "증오심 표현 및 노골적인 폭력",
                "개인정보 노출 위험",
                "기타"
            )
            val spinnerReportUserAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, reportUserReasonList)
            spinnerReportUserReason.adapter = spinnerReportUserAdapter
            spinnerReportUserReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                    selectedReportUserReason = parent?.getItemAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


            if (clickedItem.posterEmail != currentUser.email) {
                binding.btnMenu.setOnClickListener { view ->
                    val popupMenu = PopupMenu(requireContext(), view)
                    popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                        when (item.itemId) {
                            R.id.action_report -> {
                                AlertDialog.Builder(requireContext())
                                    .setView(reportPostDialogView)
                                    .setPositiveButton("제출") { dialog, _ ->
                                        val reportReason = selectedReportPostReason
                                        val reportDetail = etReportPostDetail.text.toString()
                                        val newReport = Report(
                                            type = "Post",
                                            postId = clickedItem.id,
                                            postDetail = clickedItem.detail,
                                            reporterEmail = currentUser.email,
                                            reporteeEmail = clickedItem.posterEmail,
                                            reportReason = reportReason,
                                            reportDetail = reportDetail
                                        )
                                        postViewModel.sendReport(newReport)
                                        postViewModel.deletePost(clickedItem.id)
                                        Toast.makeText(requireContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                                        dialog.dismiss()
                                        selectedReportPostReason = ""
                                    }
                                    .setNegativeButton("취소") { dialog, which ->
                                        dialog.dismiss()
                                    }
                                    .show()
                                true
                            }
                            R.id.action_report_user -> {
                                AlertDialog.Builder(requireContext())
                                    .setView(reportUserDialogView)
                                    .setPositiveButton("제출") { dialog, _ ->
                                        val reportReason = selectedReportUserReason
                                        val reportDetail = etReportUserDetail.text.toString()
                                        val newReport = Report(
                                            type = "Post_User",
                                            postId = clickedItem.id,
                                            postDetail = clickedItem.detail,
                                            reporterEmail = currentUser.email,
                                            reporteeEmail = clickedItem.posterEmail,
                                            reportReason = reportReason,
                                            reportDetail = reportDetail
                                        )
                                        postViewModel.sendReport(newReport)
                                        postViewModel.deletePost(clickedItem.id)
                                        Toast.makeText(requireContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                                        dialog.dismiss()
                                        selectedReportUserReason = ""
                                    }
                                    .setNegativeButton("취소") { dialog, which ->
                                        dialog.dismiss()
                                    }
                                    .show()
                                true
                            }
                            R.id.action_block_user -> {
                                AlertDialog.Builder(requireContext())
                                    .setTitle("유저 차단")
                                    .setMessage("이 유저를 차단할까요?\n차단 시 해당 유저의 게시글이 노출되지 않습니다.")
                                    .setPositiveButton("확인") { dialog, _ ->
                                        userViewModel.addBlockedUser(clickedItem.posterEmail)
                                        Toast.makeText(requireContext(), "유저가 차단되었습니다.", Toast.LENGTH_SHORT).show()
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
                                        Toast.makeText(this.requireContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                        dialog.dismiss()
                                        requireActivity().supportFragmentManager.popBackStack()
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
            } else {
                binding.tvNoPhotos.visibility = View.GONE
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
                binding.ivPoster.setImageResource(R.drawable.group_member)
            } else {
                postViewModel.getPosterPhotoUrl(
                    clickedItemId = clickedItemId!!,
                    onSuccess = { downloadUrl ->
                        binding.ivPoster.load(clickedItem.posterPhoto) {
                            placeholder(R.drawable.placeholder) // 로딩 중 보여줄 이미지
                            error(R.drawable.group_member) // 로드 실패 시 보여줄 기본 이미지
                            listener(
                                onSuccess = { _, result ->
                                    Log.d("Coil", "Image load succeeded")
                                },
                                onError = { _, result ->
                                    Log.e("Coil", "Image load failed: ${result.throwable.message}")
                                }
                            )
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

            commentsAdapter.itemClick2 = object : CommentsAdapter.ItemClick2 {
                override fun onClick2(view: View, position: Int) {
                    val clickedComment = clickedItem.comments[position]
                    if (clickedComment.commenterEmail != currentUser.email) {
                        val popupMenu = PopupMenu(requireContext(), view)
                        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                            when (item.itemId) {
                                R.id.action_report -> {
                                    AlertDialog.Builder(requireContext())
                                        .setView(reportPostDialogView)
                                        .setPositiveButton("제출") { dialog, _ ->
                                            val reportReason = selectedReportPostReason
                                            val reportDetail = etReportPostDetail.text.toString()
                                            val newReport = Report(
                                                type = "Comment",
                                                postId = clickedComment.id,
                                                postDetail = clickedComment.detail,
                                                reporterEmail = currentUser.email,
                                                reporteeEmail = clickedComment.commenterEmail,
                                                reportReason = reportReason,
                                                reportDetail = reportDetail
                                            )
                                            postViewModel.sendReport(newReport)
                                            postViewModel.deleteComment(clickedItem.id, clickedComment)
                                            Toast.makeText(
                                                requireContext(),
                                                "신고가 접수되었습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog.dismiss()
                                            selectedReportPostReason = ""
                                        }
                                        .setNegativeButton("취소") { dialog, which ->
                                            dialog.dismiss()
                                        }
                                        .show()
                                    true
                                }
                                R.id.action_report_user -> {
                                    AlertDialog.Builder(requireContext())
                                        .setView(reportUserDialogView)
                                        .setPositiveButton("제출") { dialog, _ ->
                                            val reportReason = selectedReportUserReason
                                            val reportDetail = etReportUserDetail.text.toString()
                                            val newReport = Report(
                                                type = "Comment_User",
                                                postId = clickedComment.id,
                                                postDetail = clickedComment.detail,
                                                reporterEmail = currentUser.email,
                                                reporteeEmail = clickedComment.commenterEmail,
                                                reportReason = reportReason,
                                                reportDetail = reportDetail
                                            )
                                            postViewModel.sendReport(newReport)
                                            postViewModel.deleteComment(clickedItem.id, clickedComment)
                                            Toast.makeText(requireContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                                            dialog.dismiss()
                                            selectedReportUserReason = ""
                                        }
                                        .setNegativeButton("취소") { dialog, which ->
                                            dialog.dismiss()
                                        }
                                        .show()
                                    true
                                }
                                R.id.action_block_user -> {
                                    AlertDialog.Builder(requireContext())
                                        .setTitle("유저 차단")
                                        .setMessage("이 유저를 차단할까요?\n" +
                                                "차단 시 해당 유저의 게시글이 노출되지 않습니다.")
                                        .setPositiveButton("확인") { dialog, _ ->
                                            userViewModel.addBlockedUser(clickedComment.commenterEmail)
                                            Toast.makeText(requireContext(), "유저가 차단되었습니다.", Toast.LENGTH_SHORT).show()
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
                    } else {
                        val popupMenu = PopupMenu(requireContext(), view)
                        popupMenu.menuInflater.inflate(R.menu.popup_menu_mine_comment, popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                            when (item.itemId) {
                                R.id.action_delete -> {
                                    AlertDialog.Builder(requireContext())
                                        .setTitle("댓글 삭제하기")
                                        .setMessage("댓글을 삭제하시겠습니까?")
                                        .setPositiveButton("삭제") { dialog, _ ->
                                            commentsAdapter.removeItem(position)
                                            postViewModel.deleteComment(clickedItem.id, clickedComment)
                                            Toast.makeText(requireContext(), "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                            dialog.dismiss()
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
                }
            }

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
        postViewModel.getAllPosts()
        postViewModel.filteredPosts.observe(viewLifecycleOwner) { data ->
            clickedItem = data.find { it.id == clickedItemId }!!
//            commentsAdapter.updateData(clickedItem.comments)
        }
    }
    override fun onResume() {
        super.onResume()
//        postViewModel.getAllPosts()
//        postViewModel.filteredPosts.observe(viewLifecycleOwner) { data ->
//            clickedItem = data.find { it.id == clickedItemId }!!
//            commentsAdapter.updateData(clickedItem.comments)
//        }
    }
    fun reloadFragment() {
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.detach(this)
        fragmentTransaction.attach(this)
        fragmentTransaction.commit()
    }
}

