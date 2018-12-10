package com.fueledbycaffeine.bunnypedia.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan
import androidx.annotation.ColorInt

class RoundedBackgroundSpan(
  @ColorInt private val backgroundColor: Int,
  @ColorInt private val textColor: Int
) : ReplacementSpan() {
  companion object {
    private const val PADDING = 20.0f
    private const val RADIUS = 5.0f
  }

  override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
    val textLength = paint.measureText(text.subSequence(start, end).toString())
    return (PADDING + textLength + PADDING).toInt()
  }

  override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
    val width = paint.measureText(text.subSequence(start, end).toString())
    val rect = RectF(
      x - PADDING,
      top.toFloat(),
      x + width + PADDING,
      bottom.toFloat()
    )
    paint.color = backgroundColor
    canvas.drawRoundRect(rect, RADIUS, RADIUS, paint)
    paint.color = textColor
    canvas.drawText(text, start, end, x, y.toFloat(), paint)
  }
}
