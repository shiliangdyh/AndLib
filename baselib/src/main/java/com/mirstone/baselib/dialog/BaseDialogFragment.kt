package com.mirstone.baselib.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.mirstone.baselib.R

/**
 * @package: com.mirstone.baselib.dialog
 * @fileName: BaseDialogFragment
 * @data: 2018/7/31 21:00
 * @author: ShiLiang
 * @describe:
 */
abstract class BaseDialogFragment : DialogFragment() {

    @get:LayoutRes
    abstract val layoutRes: Int

    val paddingLeft: Int
        get() = 0

    val paddingTop: Int
        get() = 0

    val paddingRight: Int
        get() = 0

    val paddingBottom: Int
        get() = 0

    open val height: Int
        get() = -1

    open var dimAmount: Float = DEFAULT_DIM
        get() = DEFAULT_DIM

    open var cancelOutside: Boolean = true
        get() = true

    val fragmentTag: String
        get() = TAG


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(cancelOutside)
        dialog.setCancelable(false)

        val v = inflater.inflate(layoutRes, container, false)
        bindView(v)
        return v
    }

    abstract fun bindView(v: View)

    override fun onStart() {
        super.onStart()

        val window = dialog.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val params = window.attributes

            params.dimAmount = dimAmount
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            if (height > 0) {
                params.height = height
            } else {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            params.gravity = Gravity.BOTTOM
            window.decorView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

            window.attributes = params
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, fragmentTag)
    }

    companion object {
        private val TAG = "base_dialog_fragment"

        private val DEFAULT_DIM = 0.5f
    }
}
