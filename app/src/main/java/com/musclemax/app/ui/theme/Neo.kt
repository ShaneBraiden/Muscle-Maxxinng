package com.musclemax.app.ui.theme

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Soft outer neumorphic shadow: dark bottom-right + light top-left.
 * Use for cards, buttons, and icon tiles sitting on the neutral surface.
 */
fun Modifier.neoRaised(
    shape: Shape = RoundedCornerShape(16.dp),
    offset: Dp = 6.dp,
    blur: Dp = 12.dp,
    fill: Color = SurfaceBackground
): Modifier = this
    .drawBehind {
        val outline = shape.createOutline(size, layoutDirection, this)
        val outlinePath = Path().apply { addOutline(outline) }
        drawIntoCanvas { canvas ->
            val blurPx = blur.toPx()
            val offPx = offset.toPx()
            val darkPaint = Paint().apply {
                asFrameworkPaint().apply {
                    color = android.graphics.Color.TRANSPARENT
                    setShadowLayer(blurPx, offPx, offPx, NeoShadowDark.toArgb())
                }
            }
            val lightPaint = Paint().apply {
                asFrameworkPaint().apply {
                    color = android.graphics.Color.TRANSPARENT
                    setShadowLayer(blurPx, -offPx, -offPx, NeoShadowLight.toArgb())
                }
            }
            canvas.drawPath(outlinePath, darkPaint)
            canvas.drawPath(outlinePath, lightPaint)
        }
    }
    .background(fill, shape)

/**
 * Inset / "pressed" neumorphic look — approximated with a clipped blurred
 * stroke along the edge (dark top-left, light bottom-right).
 */
fun Modifier.neoPressed(
    shape: Shape = RoundedCornerShape(16.dp),
    blur: Dp = 8.dp,
    fill: Color = SurfaceBackground
): Modifier = this
    .background(fill, shape)
    .drawBehind {
        val outline = shape.createOutline(size, layoutDirection, this)
        val clip = Path().apply { addOutline(outline) }
        drawIntoCanvas { canvas ->
            val blurPx = blur.toPx()
            canvas.save()
            canvas.clipPath(clip)

            val darkPaint = Paint().apply {
                asFrameworkPaint().apply {
                    style = android.graphics.Paint.Style.STROKE
                    strokeWidth = blurPx
                    color = NeoShadowDarkInset.toArgb()
                    maskFilter = BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
                }
            }
            val lightPaint = Paint().apply {
                asFrameworkPaint().apply {
                    style = android.graphics.Paint.Style.STROKE
                    strokeWidth = blurPx
                    color = NeoShadowLightInset.toArgb()
                    maskFilter = BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
                }
            }
            canvas.translate(blurPx / 3f, blurPx / 3f)
            canvas.drawPath(clip, darkPaint)
            canvas.translate(-blurPx / 3f, -blurPx / 3f)

            canvas.translate(-blurPx / 3f, -blurPx / 3f)
            canvas.drawPath(clip, lightPaint)
            canvas.translate(blurPx / 3f, blurPx / 3f)

            canvas.restore()
        }
    }

fun Modifier.neoCircle(
    offset: Dp = 4.dp,
    blur: Dp = 8.dp,
    fill: Color = SurfaceBackground
): Modifier = neoRaised(shape = CircleShape, offset = offset, blur = blur, fill = fill)
