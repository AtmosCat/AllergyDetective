package com.example.allergydetective.presentation.community.community_home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.databinding.FragmentCommunityHomeBinding
import com.example.allergydetective.presentation.PostViewModel
import com.example.allergydetective.presentation.UserViewModel
import com.example.allergydetective.presentation.community.newpost.NewPostFragment
import com.example.allergydetective.presentation.community.postdetail.PostDetailFragment
import com.example.allergydetective.presentation.community.postlist.PostListFragment
import com.example.allergydetective.presentation.filter.PostFilterFragment
import com.example.allergydetective.presentation.home.HomeFragment
import com.example.allergydetective.presentation.home.MyPageFragment

class CommunityHomeFragment : Fragment() {

    private var _binding: FragmentCommunityHomeBinding? = null

    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    private val postViewModel: PostViewModel by activityViewModels {
        viewModelFactory { initializer { PostViewModel(requireActivity().application) } }
    }

    private val communityHomeAdapter by lazy { CommunityHomeAdapter() }

    private var allPosts = listOf<Post>()

    private var clickedPost = Post()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.adapter = communityHomeAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        postViewModel.getAllPosts()
        postViewModel.allPosts.observe(viewLifecycleOwner) { data ->
            allPosts = data
            allPosts.filter { it.posterEmail !in userViewModel.currentUser.value!!.blockedUsers }
            communityHomeAdapter.submitList(allPosts)
        }

        val postListFragment = requireActivity().supportFragmentManager.findFragmentByTag("PostListFragment")
        binding.btnSearch.setOnClickListener() {
            postViewModel.setSearchKeyword(binding.etSearch.text.toString())
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@CommunityHomeFragment)
                if (postListFragment == null) {
                    add(R.id.main_frame, PostListFragment(), "PostListFragment")
                } else {
                    show(postListFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val blinkAnimation = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 1000 // 애니메이션 실행 시간 (0.5초)
            repeatMode = Animation.REVERSE // 애니메이션을 반대로 반복
            repeatCount = Animation.INFINITE // 무한 반복
        }

        postViewModel.selectedCategories.observe(viewLifecycleOwner) { data ->
            if (data.isEmpty()) {
                binding.btnFilter.startAnimation(blinkAnimation)
            } else {
                binding.btnFilter.startAnimation(blinkAnimation)
            }
        }

        val postFilterFragment = requireActivity().supportFragmentManager.findFragmentByTag("PostFilterFragment")
        binding.btnFilter.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
//                hide(this@ItemListFragment)
                if (postFilterFragment == null) {
                    add(R.id.main_frame, PostFilterFragment(), "PostFilterFragment")
                } else {
                    show(postFilterFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val postDetailFragment = requireActivity().supportFragmentManager.findFragmentByTag("PostDetailFragment")
        communityHomeAdapter.itemClick = object : CommunityHomeAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                clickedPost = allPosts[position]
                val dataToSend = clickedPost.id
                val postDetail = PostDetailFragment.newInstance(dataToSend)
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    hide(this@CommunityHomeFragment)
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

        val emptyScrollUpButton = binding.btnScrollUpEmpty
        val filledScrollUpButton = binding.btnScrollUpFilled
        binding.recyclerview.addOnScrollListener (object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy>0) {
                    if (emptyScrollUpButton.visibility == View.GONE){
                        emptyScrollUpButton.apply{
                            visibility = View.VISIBLE
                            alpha = 0f
                            animate().alpha(1f).setDuration(300).start()
                        }
                    }
                } else {
                    if(emptyScrollUpButton.visibility == View.VISIBLE){
                        emptyScrollUpButton.animate()
                            .alpha(0f)
                            .setDuration(800)
                            .withEndAction{emptyScrollUpButton.visibility = View.GONE }
                            .start()
                    }
                }
            }
        })

        emptyScrollUpButton.setOnClickListener{
            binding.recyclerview.smoothScrollToPosition(0)
            filledScrollUpButton.visibility = ImageView.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                filledScrollUpButton.visibility = ImageView.GONE
            }, 50) // 100밀리초, 0.1초
        }

        val newPostFragment = requireActivity().supportFragmentManager.findFragmentByTag("NewPostFragment")
        binding.btnNewPost.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@CommunityHomeFragment)
                if (newPostFragment == null) {
                    add(R.id.main_frame, NewPostFragment(), "NewPostFragment")
                } else {
                    show(newPostFragment)
                }
                addToBackStack(null)
                commit()
            }
        }


        val homeFragment = requireActivity().supportFragmentManager.findFragmentByTag("HomeFragment")
        binding.btnTabHome.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@CommunityHomeFragment)
                if (homeFragment == null) {
                    add(R.id.main_frame, HomeFragment(), "HomeFragment")
                } else {
                    show(homeFragment)
                }
                addToBackStack(null)
                commit()
            }
        }

        val myPageFragment = requireActivity().supportFragmentManager.findFragmentByTag("MyPageFragment")
        binding.btnTabMypage.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                hide(this@CommunityHomeFragment)
                if (myPageFragment == null) {
                    add(R.id.main_frame, MyPageFragment(), "MyPageFragment")
                } else {
                    show(myPageFragment)
                }
                addToBackStack(null)
                commit()
            }
        }
    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        postViewModel.getAllPosts()
        postViewModel.filteredPosts.observe(viewLifecycleOwner) { data ->
            communityHomeAdapter.updateData(data)
            allPosts = data
        }
    }
    override fun onResume() {
        super.onResume()
        postViewModel.getAllPosts()
        postViewModel.filteredPosts.observe(viewLifecycleOwner) { data ->
            communityHomeAdapter.updateData(data)
            allPosts = data
        }
    }
}