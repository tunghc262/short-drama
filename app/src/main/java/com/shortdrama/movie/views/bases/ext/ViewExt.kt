package com.shortdrama.movie.views.bases.ext

import android.graphics.Paint
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.math.abs

internal const val DISPLAY = 1080

fun View.goneView() {
    visibility = View.GONE
}

fun View.visibleView() {
    visibility = View.VISIBLE
}

fun View.invisibleView() {
    visibility = View.INVISIBLE
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.isGone() = visibility == View.GONE

fun ViewBinding.goneView() {
    this.root.goneView()
}

fun ViewBinding.visibleView() {
    this.root.visibleView()
}

fun ViewBinding.invisibleView() {
    this.root.invisibleView()
}

fun ViewBinding.isVisible() = this.root.visibility == View.VISIBLE

fun ViewBinding.isInvisible() = this.root.visibility == View.INVISIBLE

fun ViewBinding.isGone() = this.root.visibility == View.GONE

fun View.resizeView(width: Int, height: Int = 0) {
    val pW = context.getWidthScreenPx() * width / DISPLAY
    val pH = if (height == 0) pW else pW * height / width
    val params = layoutParams
    params.let {
        it.width = pW
        it.height = pH
    }
}

fun View.onClick(action: (view: View?) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        override fun onClick(v: View) {
            if (!context.canTouch()) return
            action(v)
        }
    })
}

fun View.onClickAlpha(action: (view: View?) -> Unit) {
    this.setOnCustomTouchViewAlphaNotOther(object : OnCustomClickListener {
        override fun onCustomClick(view: View, event: MotionEvent) {
            if (!context.canTouch()) return
            action(view)
        }
    })
}

fun EditText.addCharacter(char: Char) {
    dispatchKeyEvent(getKeyEvent(getCharKeyCode(char)))
}

fun EditText.getKeyEvent(keyCode: Int) = KeyEvent(0, 0, KeyEvent.ACTION_DOWN, keyCode, 0)

private fun getCharKeyCode(char: Char) = when (char) {
    '0' -> KeyEvent.KEYCODE_0
    '1' -> KeyEvent.KEYCODE_1
    '2' -> KeyEvent.KEYCODE_2
    '3' -> KeyEvent.KEYCODE_3
    '4' -> KeyEvent.KEYCODE_4
    '5' -> KeyEvent.KEYCODE_5
    '6' -> KeyEvent.KEYCODE_6
    '7' -> KeyEvent.KEYCODE_7
    '8' -> KeyEvent.KEYCODE_8
    '9' -> KeyEvent.KEYCODE_9
    '*' -> KeyEvent.KEYCODE_STAR
    '+' -> KeyEvent.KEYCODE_PLUS
    else -> KeyEvent.KEYCODE_POUND
}

fun EditText.disableKeyboard() {
    showSoftInputOnFocus = false
}

fun String.isVideoFast() = videoExtensions.any { endsWith(it, true) }

fun String.isImageFast() = photoExtensions.any { endsWith(it, true) }
fun String.isAudioFast() = audioExtensions.any { endsWith(it, true) }
fun String.isRawFast() = rawExtensions.any { endsWith(it, true) }

fun TextView.setTextById(idString: Int) {
    text = resources.getString(idString)
}

fun TextView.setTextByString(string: String) {
    text = string
}

val TextView.value: String get() = text.toString().trim()

fun TextView.underlineText() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.removeUnderlines() {
    val spannable = SpannableString(text)
    for (u in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
        spannable.setSpan(object : URLSpan(u.url) {
            override fun updateDrawState(textPaint: TextPaint) {
                super.updateDrawState(textPaint)
                textPaint.isUnderlineText = false
            }
        }, spannable.getSpanStart(u), spannable.getSpanEnd(u), 0)
    }
    text = spannable
}

fun TextView.setTextColorById(idColor: Int) {
    setTextColor(ContextCompat.getColor(context, idColor))
}


fun RecyclerView.setHorizontalNestedScrollFix() {
    this.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
        private var startX = 0f
        private var startY = 0f
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startX = e.x
                    startY = e.y
                    rv.parent.requestDisallowInterceptTouchEvent(true)
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = abs(e.x - startX)
                    val dy = abs(e.y - startY)

                    if (dx > dy) {
                        // Nếu vuốt ngang nhiều hơn vuốt dọc -> RecyclerView ngang chiếm quyền
                        rv.parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        // Nếu vuốt dọc nhiều hơn -> Trả quyền cho NestedScrollView/ViewPager2 cha
                        rv.parent.requestDisallowInterceptTouchEvent(false)
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    rv.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            return false
        }
        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    })
}