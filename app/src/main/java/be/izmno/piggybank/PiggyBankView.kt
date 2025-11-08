package be.izmno.piggybank

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PiggyBankDisplay(
    text: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pigDrawable = remember {
        ContextCompat.getDrawable(context, R.drawable.piggy_bank_icon)?.mutate()
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        // Calculate pig size: 50% of view width
        val pigWidth = width * 0.5f
        val pigHeight = pigWidth // Maintain square aspect ratio (1024x1024)
        
        // Center the pig horizontally and vertically
        val pigLeft = (width - pigWidth) / 2
        val pigTop = (height - pigHeight) / 2
        val pigRight = pigLeft + pigWidth
        val pigBottom = pigTop + pigHeight
        
        // Draw the pig icon
        pigDrawable?.let { drawable ->
            drawable.setBounds(
                pigLeft.toInt(),
                pigTop.toInt(),
                pigRight.toInt(),
                pigBottom.toInt()
            )
            drawIntoCanvas { canvas ->
                drawable.draw(canvas.nativeCanvas)
            }
        }
        
        // Draw text centered in the pig
        if (text.isNotEmpty()) {
            val testText = text.ifEmpty { "€0.00" }
            val pigWidthForText = pigWidth
            val pigHeightForText = pigHeight
            
            // Calculate text size based on pig dimensions
            var textSize = 48f
            val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.BLACK
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
            }
            
            // Adjust text size to fit within the available space (centered in pig)
            paint.textSize = textSize
            while (paint.measureText(testText) > pigWidthForText * 0.8f && textSize > 12f) {
                textSize -= 2f
                paint.textSize = textSize
            }
            
            while ((paint.descent() - paint.ascent()) > pigHeightForText * 0.3f && textSize > 12f) {
                textSize -= 2f
                paint.textSize = textSize
            }
            
            // Center text in the pig icon
            val textX = width / 2
            val textY = height / 2 - (paint.descent() + paint.ascent()) / 2
            
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(text, textX, textY, paint)
            }
        }
    }
}
