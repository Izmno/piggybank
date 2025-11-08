package be.izmno.piggybank

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class PiggyBankView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val pigDrawable = ContextCompat.getDrawable(context, R.drawable.piggy_bank_icon)?.mutate()
    
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    var text: String = ""
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        // Calculate desired size: pig should be 50% of width, maintaining aspect ratio (1024x1024)
        val desiredPigWidth = widthSize * 0.5f
        val desiredPigHeight = desiredPigWidth // Square aspect ratio
        val desiredHeight = (desiredPigHeight + padding * 2).toInt()

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> widthSize
            else -> widthSize
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> minOf(desiredHeight, heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        
        // Calculate pig size: 50% of view width
        val pigWidth = width * 0.5f
        val pigHeight = pigWidth // Maintain square aspect ratio (1024x1024)
        
        // Center the pig horizontally and vertically
        val pigLeft = (width - pigWidth) / 2
        val pigTop = (height - pigHeight) / 2
        val pigRight = pigLeft + pigWidth
        val pigBottom = pigTop + pigHeight
        
        // Draw the pig icon
        pigDrawable?.let {
            it.setBounds(
                pigLeft.toInt(),
                pigTop.toInt(),
                pigRight.toInt(),
                pigBottom.toInt()
            )
            it.draw(canvas)
        }
        
        // Draw text centered in the pig
        if (text.isNotEmpty()) {
            // Calculate text size based on pig dimensions
            val textSize = calculateTextSize(pigWidth, pigHeight)
            textPaint.textSize = textSize
            
            // Center text in the pig icon
            val textX = width / 2
            val textY = height / 2 - (textPaint.descent() + textPaint.ascent()) / 2
            canvas.drawText(text, textX, textY, textPaint)
        }
    }

    private fun calculateTextSize(availableWidth: Float, availableHeight: Float): Float {
        val testText = text.ifEmpty { "€0.00" }
        var textSize = 48f
        textPaint.textSize = textSize

        // Adjust text size to fit within the available space (centered in pig)
        while (textPaint.measureText(testText) > availableWidth * 0.8f && textSize > 12f) {
            textSize -= 2f
            textPaint.textSize = textSize
        }

        while ((textPaint.descent() - textPaint.ascent()) > availableHeight * 0.3f && textSize > 12f) {
            textSize -= 2f
            textPaint.textSize = textSize
        }

        return textSize
    }
    
    companion object {
        private const val padding = 20f
    }
}

