package com.example.allergydetective.presentation.filter


import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.allergydetective.R

class CustomToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr) {

    private var isChecked = false

    init {
        // 버튼 클릭 시 상태를 토글
        setOnClickListener {
            isChecked = !isChecked
            updateButtonState()
        }
        // 초기 상태 업데이트
        updateButtonState()
    }

    private fun updateButtonState() {
        val colorRes = if (isChecked) R.color.main_color_orange else R.color.main_color_light_gray
        backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
    }

    // 상태 변경 메서드
    fun setChecked(checked: Boolean) {
        isChecked = checked
        updateButtonState()
    }

    // 현재 상태 반환
    fun isChecked(): Boolean {
        return isChecked
    }
}
