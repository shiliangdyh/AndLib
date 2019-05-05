package com.mirstone.module.transition

import android.os.Bundle
import android.os.Message
import android.view.View
import com.alexvasilkov.gestures.animation.ViewPosition
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import kotlinx.android.synthetic.main.activity_image_view_anim.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @package: com.mirstone.module.transition
 * @fileName: ImageViewAnimAct
 * @data: 2018/8/10 10:45
 * @author: ShiLiang
 * @describe:
 */
class ImageViewAnimAct:BaseSwipeBackActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_image_view_anim)

        imageView.setOnClickListener {showFullImage(imageView)}
        imageView.viewTreeObserver.addOnGlobalLayoutListener({ this.onLayoutChanges() })
    }

    private fun showFullImage(image: View) {
        // Requesting opening image in a new activity with animation.
        // First of all we need to get current image position:
        val position = ViewPosition.from(image)
        // Now pass this position to a new activity. New activity should start without any
        // animations and should have transparent window (set through activity theme).
        FullImageActivity.open(this, position, R.drawable.dog)
    }

    private fun onLayoutChanges() {
        // Notifying fullscreen image activity about image position changes.
        val position = ViewPosition.from(imageView)
//        Events.create(CrossEvents.POSITION_CHANGED).param(position).post()
        val message = Message.obtain()
        message.what = 10
        message.obj = position
        EventBus.getDefault().post(message)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImagePositionChanged(message: Message) {
        // Fullscreen activity requested to show or hide original image
//        image.setVisibility(if (show) View.VISIBLE else View.INVISIBLE)
        if (message.what == 200) {
            val show = message.obj as Boolean
            imageView.visibility = if (show) View.VISIBLE else View.INVISIBLE
        }
    }
}
