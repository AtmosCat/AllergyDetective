package com.example.allergydetective.presentation.community.newpost

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.allergydetective.R
import com.example.allergydetective.data.model.user.Comments
import com.example.allergydetective.data.model.user.Post
import com.example.allergydetective.data.model.user.User
import com.example.allergydetective.databinding.FragmentNewPostBinding
import com.example.allergydetective.databinding.FragmentPostDetailBinding
import com.example.allergydetective.presentation.PostViewModel
import com.example.allergydetective.presentation.UserViewModel
import com.example.allergydetective.presentation.community.postdetail.CommentsAdapter
import com.example.allergydetective.presentation.community.postdetail.PostDetailFragment
import com.example.allergydetective.presentation.community.postdetail.ReplyDetailFragment
import com.example.allergydetective.presentation.community.postdetail.reply.RepliesAdapter
import com.example.allergydetective.presentation.itemdetail.ViewPagerAdapter
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

private const val ARG_PARAM1 = "param1"
class NewPostFragment : Fragment() {
    private var param1: String? = null

    private var _binding: FragmentNewPostBinding? = null
    private val binding get() = _binding!!

    private var isCategoryButtonCheckedList = mutableListOf(false, false, false, false, false)

    private lateinit var categoryButton1: Button
    private lateinit var categoryButton2: Button
    private lateinit var categoryButton3: Button
    private lateinit var categoryButton4: Button
    private lateinit var categoryButton5: Button

    private lateinit var categoryButtonList : List<Button>

    private lateinit var categoryButtonTextList : List<String>

    private var selectedCategory = ""


    private var currentUser = User()

    private val imageResources = mutableListOf<String>()

    private val userViewModel: UserViewModel by activityViewModels {
        viewModelFactory { initializer { UserViewModel(requireActivity().application) } }
    }

    private val postViewModel: PostViewModel by activityViewModels {
        viewModelFactory { initializer { PostViewModel(requireActivity().application) } }
    }

    private lateinit var pickImageLauncher : ActivityResultLauncher<Intent>

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
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2. 갤러리에서 이미지 선택
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val uris= mutableListOf<Uri>()

                    data?.let {
                        if (it.clipData != null) {
                            val clipData = it.clipData
                            for (i in 0 until clipData!!.itemCount) {
                                uris.add(clipData.getItemAt(i).uri)
                            }
                        } else {
                            it.data?.let { uri -> uris.add(uri) }
                        }
                        lifecycleScope.launch {
                            handleImages(uris)
                        }
                    }
                }
            }

        // 카테고리 관련 부분
        categoryButton1 = binding.customButton1
        categoryButton2 = binding.customButton2
        categoryButton3 = binding.customButton3
        categoryButton4 = binding.customButton4
        categoryButton5 = binding.customButton5

        categoryButtonList = listOf(
            categoryButton1,
            categoryButton2,
            categoryButton3,
            categoryButton4,
            categoryButton5,
        )

        categoryButtonTextList = listOf(
            categoryButton1.text.toString(),
            categoryButton2.text.toString(),
            categoryButton3.text.toString(),
            categoryButton4.text.toString(),
            categoryButton5.text.toString(),
        )

        for (i in 0..categoryButtonList.size-1) {
            categoryButtonClicker(categoryButtonList[i], i)
        }

        currentUser = userViewModel.currentUser.value!!

        binding.btnCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            postViewModel.initializeTemporaryImageUrls()
        }

        binding.btnAddPhoto.setOnClickListener{
            Toast.makeText(this.requireContext(), "올리실 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            pickImages()
        }

        val viewPager = binding.viewPager

        val viewPagerAdapter = ViewPagerAdapter(imageResources)
        viewPager.adapter = viewPagerAdapter

        binding.ivPoster.load(currentUser.photo)
        binding.tvPoster.text = currentUser.nickname

        binding.btnNewPost.setOnClickListener{
            if (selectedCategory.isEmpty()) {
                Toast.makeText(this.requireContext(), "카테고리를 1가지 지정해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                postViewModel.addPost(
                    selectedCategory,
                    binding.etTitle.text.toString(),
                    binding.etDetail.text.toString(),
                )
            }
        }
    }

    private fun categoryButtonClicker(button: Button, position: Int){
        button.setOnClickListener{
            if (!isCategoryButtonCheckedList[position]) {
                isCategoryButtonCheckedList[0] = false
                isCategoryButtonCheckedList[1] = false
                isCategoryButtonCheckedList[2] = false
                isCategoryButtonCheckedList[3] = false
                isCategoryButtonCheckedList[4] = false
                isCategoryButtonCheckedList[position] = true
            } else {
                isCategoryButtonCheckedList[position] = false
            }
            updateButtonState(button, position)
            setCategory(button, position)
        }
    }

    private fun updateButtonState(button: Button, position: Int) {
        if (isCategoryButtonCheckedList[position]) {
            button.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_color_orange
                )
            )
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            button.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_color_more_light_gray
                )
            )
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

    private fun setCategory(button: Button, position: Int) {
        if (isCategoryButtonCheckedList[position]) {
            selectedCategory = button.text.toString()
        } else {
            selectedCategory = ""
        }
    }

    // 1. 갤러리에서 복수 이미지 선택할 수 있는 인텐트 생성 및 팝업
    private fun pickImages() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        pickImageLauncher.launch(intent)
    }

    // 선택한 이미지에서 URI를 추출해 저장
    suspend fun handleImages(uris: List<Uri>) {
        for (uri in uris) {
            val bitmap = uriToBitmap(uri)
            bitmap?.let {
                val imageUrl = uploadImageToFirebaseStorage(it)
                imageResources.add(imageUrl.toString())
                // Firebase Storage에서 반환된 다운로드URL을 FireStore에 저장
                imageUrl?.let { url -> postViewModel.saveTemporaryImageUrl(url) }
            }
        }
    }

    // URI를 Bitmap으로 변환
    private suspend fun uriToBitmap(uri: Uri): Bitmap? {
        return withContext(Dispatchers.IO) {
            requireActivity().contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }
    }
    // Bitmap 파일을 Firebase Storage에 저장하고 다운로드 URL을 반환
    private fun uploadImageToFirebaseStorage(bitmap: Bitmap): String? {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${System.currentTimeMillis()}.png")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        var downloadUrl: String? = null
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                downloadUrl = uri.toString()
            }
        }.addOnFailureListener {
            // Handle upload failure
        }
        return downloadUrl
    }


}

