package com.example.customprogressbar.Views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.customprogressbar.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ProgressBarCustomView(context: Context, attrs: AttributeSet): View(context, attrs) {

    companion object {
        private const val DEFAULT_BACKGROUND_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_COLOR = Color.LTGRAY
        private const val DEFAULT_VALUE = 0.5f
        private const val DEFAULT_BRUSH_SIZE = 3f
        private const val DEFAULT_BORDER_BRUSH_SIZE = 10f
        private const val DEFAULT_TACT_TIME = 25
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var backgroundColor = DEFAULT_BACKGROUND_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR
    private var brushSize = DEFAULT_BRUSH_SIZE
    private var borderBrushSize = DEFAULT_BORDER_BRUSH_SIZE
    private var tactTime = DEFAULT_TACT_TIME

    private var value = DEFAULT_VALUE
    private var targetValue = DEFAULT_VALUE
    private var animation = ValueAnimator()
    init {
        paint.isAntiAlias = true
        animation.interpolator = LinearInterpolator()
        setupAttributes(attrs)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawBackground(it)
            drawFill(it)
            drawFrame(it)
        }
    }

    private fun drawBackground(canvas: Canvas) {
        paint.color = backgroundColor
        paint.strokeWidth = brushSize
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun drawFill(canvas: Canvas) {
        paint.color = if (value < 0.5)
            Color.rgb(255, (255*2*value).toInt(), 0)
        else
            Color.rgb((255*2*(1-value)).toInt(), 255, 0)

        canvas.drawRect(
            0f,
            height.toFloat()*(1-value),
            width.toFloat(),
            height.toFloat(),
            paint
        )
    }

    private fun drawFrame(canvas: Canvas) {
        paint.color = borderColor
        paint.strokeWidth = borderBrushSize
        paint.style = Paint.Style.STROKE
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    fun setProgress(newValue: Float) {
        targetValue = if (newValue in 0f..1f) {
            newValue
        } else if (newValue < 0) {
            0f
        } else {
            1f
        }
        startAnimation()
    }

    private fun startAnimation() {
        animation.setFloatValues(value, targetValue)
        animation.duration = kotlin.math.abs(((targetValue - value) * 100).toLong()) * tactTime
        animation.addUpdateListener { valueAnimator ->
                value = valueAnimator.animatedValue as Float
                invalidate()
            }

        animation.start()
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ProgressBarCustomView,
            0, 0)
        backgroundColor = typedArray.getColor(
            R.styleable.ProgressBarCustomView_backgroundColor,
            DEFAULT_BACKGROUND_COLOR
        )
        borderColor = typedArray.getColor(
            R.styleable.ProgressBarCustomView_borderColor,
            DEFAULT_BORDER_COLOR
        )
        borderBrushSize = typedArray.getFloat(
            R.styleable.ProgressBarCustomView_borderWidth,
            DEFAULT_BORDER_BRUSH_SIZE
        )
        value = typedArray.getFloat(
            R.styleable.ProgressBarCustomView_value,
            DEFAULT_VALUE
        )
        tactTime = typedArray.getInt(
            R.styleable.ProgressBarCustomView_tactTime,
            DEFAULT_TACT_TIME
        )
        typedArray.recycle()
    }

}