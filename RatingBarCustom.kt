package com.ems.mybox.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.KeyEvent.ACTION_DOWN
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import com.ems.mybox.R
import com.ems.mybox.application.AppApplication

class RatingBarCustom @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    val bitmapUnSelected: Bitmap by lazy {
        return@lazy BitmapFactory.decodeResource(
            resources,
            R.drawable.star_grey_padd
        );
    }

    val bitmapSelected: Bitmap by lazy {
        return@lazy BitmapFactory.decodeResource(
            resources,
            R.drawable.starr_green_padd
        );
    }


    var paddingStart = AppApplication.instance.resources.getDimension(R.dimen._8sdp)
    var paddingEnd = AppApplication.instance.resources.getDimension(R.dimen._8sdp)
    var dividerWidth = AppApplication.instance.resources.getDimension(R.dimen._8sdp)
    var paddingTop = AppApplication.instance.resources.getDimension(R.dimen._8sdp)
    var paddingBottom = AppApplication.instance.resources.getDimension(R.dimen._8sdp)

    var startX = 0f
    var endX = 0f
    var startY = 0f
    var canvasWidth = 0f
    var canvasHeight = 0f
    var bitMapWidth = 0f
    var total5startWidth = 0f

    var isRtl: Boolean = false
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { canvas ->
            isRtl = layoutDirection == View.LAYOUT_DIRECTION_RTL
            canvasWidth = canvas.width.toFloat()
            canvasHeight = canvas.height.toFloat()
            bitMapWidth = bitmapSelected.width.toFloat()
            total5startWidth = bitMapWidth * 5f
            startX = (canvasWidth - (bitmapSelected.width * 5f)) / 2f
            endX = canvasWidth - startX
            startY = (canvasHeight - (bitmapSelected.height)) / 2f


            if (isRtl) {
                for (i in 0..4) {
                    val bs = if (i + 1 <= rating) bitmapSelected else bitmapUnSelected
                    canvas.drawBitmap(bs, endX - ((i + 1) * bitmapSelected.width), startY, null)
                }
            } else {
                for (i in 0..4) {
                    val bs = if (i + 1 <= rating) bitmapSelected else bitmapUnSelected
                    canvas.drawBitmap(bs, startX + (i * bitmapSelected.width), startY, null)
                }
            }


        }
    }

    var rating = 5
    var firstDownX = 0f


    var onRatingChange: ((rating: Int) -> Unit)? = null
    var onTouchStarted: (() -> Unit)? = null
    var onTouchEnd: (() -> Unit)? = null


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.action == ACTION_DOWN) {
                onTouchStarted?.invoke()
                println("event.x ${event.x}")
                firstDownX = event.x
                if (event.x in startX..endX) {
                    if (isRtl)
                        for (i in 4 downTo 0) {
                            if (event.x <= endX - (i * bitmapSelected.width)) {
                                val prevrating = rating
                                rating = i + 1
                                if (prevrating != rating) {
                                    onRatingChange?.invoke(rating)
                                }

                                performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
                                postInvalidate()
                                break
                            }
                        }
                    else
                        for (i in 4 downTo 0) {
                            if (event.x >= startX + (i * bitmapSelected.width)) {
                                val prevrating = rating
                                rating = i + 1
                                if (prevrating != rating) {
                                    onRatingChange?.invoke(rating)
                                }

                                performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
                                postInvalidate()
                                break
                            }
                        }

                }

            } else if (event.action == ACTION_MOVE) {
                println("event.x ${event.x}")
                if (isRtl) {
                    for (i in 4 downTo 0) {
                        if (event.x <= endX - (i * bitmapSelected.width)) {
                            val prevrating = rating
                            rating = i + 1
                            if (prevrating != rating) {
                                onRatingChange?.invoke(rating)
                            }
                            postInvalidate()
                            break
                        }
                    }
                } else {
                    for (i in 4 downTo 0) {
                        if (event.x >= startX + (i * bitmapSelected.width)) {
                            val prevrating = rating
                            rating = i + 1
                            if (prevrating != rating) {
                                onRatingChange?.invoke(rating)
                            }
                            postInvalidate()
                            break
                        }
                    }
                }


            } else if (event.action == ACTION_UP) {
                onTouchEnd?.invoke()
                performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
            }
        }


        return true
    }


}
