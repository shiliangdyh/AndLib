package com.mirstone.module.transition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.ViewTreeObserver
import com.alexvasilkov.gestures.Settings
import com.alexvasilkov.gestures.animation.ViewPosition
import com.alexvasilkov.gestures.views.GestureImageView
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @package: com.mirstone.module.transition
 * @fileName: FullImageActivity
 * @data: 2018/8/10 10:59
 * @author: ShiLiang
 * @describe:
 */
class FullImageActivity : BaseSwipeBackActivity() {


    private lateinit var image: GestureImageView
    private lateinit var background: View

    companion object {
        private val EXTRA_POSITION = "position"
        private val EXTRA_PAINTING_ID = "painting_id"

        fun open(from: Activity, position: ViewPosition, paintingId: Int) {
            val intent = Intent(from, FullImageActivity::class.java)
            intent.putExtra(EXTRA_POSITION, position.pack())
            intent.putExtra(EXTRA_PAINTING_ID, paintingId)
            from.startActivity(intent)
            from.overridePendingTransition(0, 0) // No activity animation
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_full_image)

        image = findViewById(R.id.single_image_to) as GestureImageView
        background = findViewById(R.id.single_image_to_back)

        image.setOnClickListener { onBackPressed() }

        image.controller.settings
                .setExitType(Settings.ExitType.ALL)
                .isExitEnabled = true

        // 确保背景和图片刚开始不可见
        image.visibility = View.INVISIBLE
        background.visibility = View.INVISIBLE

        // Loading image. Note, that this image should already be cached in the memory to ensure
        // very fast loading. Consider using same image or its thumbnail as on prev screen.
        val paintingId = intent.getIntExtra(EXTRA_PAINTING_ID, 0)
        image.setImageResource(paintingId)

        // Listening for animation state and updating our view accordingly
        image.positionAnimator.addPositionUpdateListener({ position, isLeaving -> this.applyImageAnimationState(position, isLeaving) })

        // Starting enter image animation only once image is drawn for the first time to prevent
        // image blinking on activity start
        runAfterImageDraw(Runnable {
            // Enter animation should only be played if activity is not created from saved state
            enterFullImage(savedInstanceState == null)

            // Hiding original image
//            Events.create(CrossEvents.SHOW_IMAGE).param(false).post()
            val message = Message.obtain()
            message.what = 200
            message.obj = false
            EventBus.getDefault().post(message)
        })
    }

    private fun enterFullImage(animate: Boolean) {
        // Updating gesture image settings
//        getSettingsController().apply(image)

        // Playing enter animation from provided position
        val position = ViewPosition.unpack(intent.getStringExtra(EXTRA_POSITION))
        image.positionAnimator.enter(position, animate)
    }

    /**
     * Runs provided action after image is drawn for the first time.
     */
    private fun runAfterImageDraw(action: Runnable) {
        image.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                image.viewTreeObserver.removeOnPreDrawListener(this)
                runOnNextFrame(action)
                return true
            }
        })
        image.invalidate()
    }

    private fun runOnNextFrame(action: Runnable) {
        val frameLength = 17L // 1 frame at 60 fps
        image.postDelayed(action, frameLength)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImagePositionChanged(message: Message) {
        // If original image position is changed we should update animator with its new position.
        if (message.what == 100) {
            val position = message.obj as ViewPosition
            if (image.positionAnimator.position > 0f) {
                image.positionAnimator.update(position)
            }
        }
    }

    private fun applyImageAnimationState(position: Float, isLeaving: Boolean) {
        val isFinished = position == 0f && isLeaving // Exit animation is finished

        background.alpha = position
        background.visibility = if (isFinished) View.INVISIBLE else View.VISIBLE
        image.visibility = if (isFinished) View.INVISIBLE else View.VISIBLE

        if (isFinished) {
            // Showing back original image
//            Events.create(CrossEvents.SHOW_IMAGE).param(true).post()
            val message = Message.obtain()
            message.what = 200
            message.obj = true
            EventBus.getDefault().post(message)

            // By default end of exit animation will return GestureImageView into
            // fullscreen state, this will make the image blink. So we need to hack this
            // behaviour and keep image in exit state until activity is finished.
            image.controller.settings.disableBounds()
            image.positionAnimator.setState(0f, false, false)

            runOnNextFrame(Runnable {
                finish()
                overridePendingTransition(0, 0)
            })
        }
    }

    override fun onBackPressed() {
        // We should leave full image mode instead of finishing this activity,
        // activity itself should only be finished in the end of the "exit" animation.
        if (!image.positionAnimator.isLeaving) {
            image.positionAnimator.exit(true)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
