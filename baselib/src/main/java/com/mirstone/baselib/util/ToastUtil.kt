package com.mirstone.baselib.util

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.mirstone.baselib.R


object ToastUtil {
    private val TAG = "ToastUtil"
    private var toast: Toast? = null
    private val DEFAULT_GRAVITY = Gravity.CENTER

    @JvmOverloads
    fun show(context: Context, info: String, gravity: Int = DEFAULT_GRAVITY) {
        val linearLayout = LinearLayout(context.applicationContext)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        linearLayout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        linearLayout.layoutParams = params
        linearLayout.gravity = gravity

        val textView = TextView(context.applicationContext)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = context.resources.getDimensionPixelOffset(R.dimen.toast_margin)
        layoutParams.setMargins(margin, 0, margin, 0)
        textView.setBackgroundResource(R.drawable.toast_bg)
        textView.layoutParams = layoutParams
        textView.gravity = gravity
        if (gravity == Gravity.BOTTOM) {
            linearLayout.setPadding(0, 0, 0, context.resources.getDimensionPixelOffset(R.dimen.toast_margin_bottom))
        }
        val padding = context.resources.getDimensionPixelOffset(R.dimen.toast_padding)
        textView.setPadding(padding, padding, padding, padding)
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        linearLayout.addView(textView)

        //        TextView toast_tv = view.findViewById(com.ruking.frame.library.R.id.toast_tv);
        textView.text = info
        textView.background.alpha = 166
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast(context.applicationContext)
        toast!!.setGravity(gravity, 0, 0)
        toast!!.duration = Toast.LENGTH_SHORT
        toast!!.view = linearLayout
        toast!!.show()
    }

    @JvmOverloads
    fun show(context: Context, info: Int, gravity: Int = DEFAULT_GRAVITY) {
        show(context, context.resources.getString(info), gravity)
    }
}
