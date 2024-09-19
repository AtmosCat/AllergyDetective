package com.example.allergydetective.presentation.community.postdetail

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
import coil.load
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.Comments
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.data.model.user.Reply
import com.example.allergydetective.data.model.user.Report
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

    private var clickedItemId = ""
    private var clickedCommentId = ""

    private var clickedComment = Comments()

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

        clickedItemId = param1.toString()
        clickedCommentId = param2.toString()
        postViewModel.filteredPosts.observe(viewLifecycleOwner) { filteredPosts ->
            clickedItem = filteredPosts.find { it.id == clickedItemId }!!
            clickedComment = clickedItem.comments.find { it.id == clickedCommentId }!!

            binding.btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            binding.ivCommenter.load(clickedComment.commenterPhoto) {
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
            binding.tvCommenter.text = clickedComment.commenterNickname
            binding.tvCommentDetail.text = clickedComment.detail

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
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedReportUserReason = parent?.getItemAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            repliesAdapter.itemClick2 = object : RepliesAdapter.ItemClick2 {
                override fun onClick2(view: View, position: Int) {
                    val clickedReply = clickedComment.reply[position]
                    if (clickedReply.replierEmail != currentUser.email) {
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
                                                type = "Reply",
                                                postId = clickedReply.id,
                                                postDetail = clickedReply.detail,
                                                reporterEmail = currentUser.email,
                                                reporteeEmail = clickedReply.replierEmail,
                                                reportReason = reportReason,
                                                reportDetail = reportDetail
                                            )
                                            postViewModel.sendReport(newReport)
                                            postViewModel.deleteReply(clickedItem.id, clickedComment.id, clickedReply.id)
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
                                                type = "Reply_User",
                                                postId = clickedReply.id,
                                                postDetail = clickedReply.detail,
                                                reporterEmail = currentUser.email,
                                                reporteeEmail = clickedReply.replierEmail,
                                                reportReason = reportReason,
                                                reportDetail = reportDetail
                                            )
                                            postViewModel.sendReport(newReport)
                                            postViewModel.deleteReply(clickedItem.id, clickedComment.id, clickedReply.id)
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
                                            userViewModel.addBlockedUser(clickedReply.replierEmail)
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
                                        .setTitle("답글 삭제하기")
                                        .setMessage("답글을 삭제하시겠습니까?")
                                        .setPositiveButton("삭제") { dialog, _ ->
                                            repliesAdapter.removeItem(position)
                                            postViewModel.deleteReply(clickedItem.id, clickedComment.id, clickedReply.id)
                                            Toast.makeText(
                                                requireContext(),
                                                "답글이 삭제되었습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog.dismiss()
                                        }
                                        .setNegativeButton("취소") { dialog, which ->
                                            dialog.dismiss()
                                        }
                                        .show()
                                    true
                                } else -> {
                                false
                            }
                            }
                        }
                        popupMenu.show()
                    }
                }
            }
        }

    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }
    override fun onResume() {
        super.onResume()
    }
}

