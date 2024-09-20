package com.example.allergydetective.data.model.user

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.example.allergydetective.data.model.food.Food

fun createSampleBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint()

    paint.color = Color.WHITE
    canvas.drawCircle(500f, 450f, 150f, paint)

    paint.color = Color.WHITE
    val path = android.graphics.Path()
    path.addCircle(500f, 1000f, 350f, Path.Direction.CCW)
    canvas.drawPath(path, paint)

    return bitmap
}

val sampleBitmap = createSampleBitmap()

// 유저 관리
data class User(val email: String = "",
                val pw: String = "",
                var name: String = "",
                val contact: String = "",
                var nickname: String = "익명",
                var allergy: MutableList<String> = mutableListOf(),
                var group: MutableList<GroupMember> = mutableListOf(
                    GroupMember("", mutableListOf()),
                    GroupMember("", mutableListOf()),
                    GroupMember("", mutableListOf()),
                    GroupMember("", mutableListOf()),
                    GroupMember("", mutableListOf())
                    ),
                val membership: String = "",
                var like: MutableList<Food> = mutableListOf(),
                val mypost: MutableList<Post> = mutableListOf(),
                val scrap: MutableList<Post> = mutableListOf(),
                var photo: String = "",
                var blockedUsers: MutableList<String> = mutableListOf())

// 유저 - 그룹 관리
data class GroupMember(var name: String = "",
                       var allergy: MutableList<String> = mutableListOf())





