package com.example.conanmoverandroidapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Xml
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import java.util.*


class PathCanvas(context: Context, attributeSet: AttributeSet?) : View(context) {

    private var paint: Paint = Paint()
    private var path:  Path = Path()

    init {
        paint.isAntiAlias = true
        paint.color = Color.RED
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
    }

    override fun performClick(): Boolean {
        super.performClick()
        path.moveTo(x,y)
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.x
        val y = event.y

        when(event.action){
            MotionEvent.ACTION_DOWN -> performClick()
            //MotionEvent.ACTION_DOWN -> path.moveTo(x,y)
            MotionEvent.ACTION_MOVE -> path.moveTo(x,y)
            else -> return false
        }
        invalidate()
        return true
    }

    fun drawFromAttributes(xStart: Float,yStart: Float,xStop: Float,yStop: Float){
        path.moveTo(xStart, yStart)
        path.lineTo(xStop, yStop)
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    /*override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }*/
}
