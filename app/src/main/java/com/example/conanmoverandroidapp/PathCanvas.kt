package com.example.conanmoverandroidapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random


class PathCanvas(context: Context, attributeSet: AttributeSet?) : View(context) {

    private var paint: Paint = Paint()
    private var path:  Path = Path()
    private var screenHeigth = 1000
    private var screenWidth = 1000


    init {
        paint.isAntiAlias = true
        paint.color = Color.RED
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
    }

   /* override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        screenHeigth = heightMeasureSpec
        screenWidth = widthMeasureSpec
    }*/

   /*PUT CODE TO TEST IN HERE FOR NOW*/
    fun test() {
        val x = Random.nextInt(0, screenWidth) * (1 + Random.nextFloat())
        val y = Random.nextInt(0, screenHeigth) *(1 + Random.nextFloat())
        val x2 = Random.nextInt(0, screenWidth) * (1 + Random.nextFloat())
        val y2 = Random.nextInt(0, screenHeigth) * (1 + Random.nextFloat())
        onUpdatePath(x,y,x2,y2)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.x
        val y = event.y

        when(event.action){
            MotionEvent.ACTION_DOWN -> test()//path.moveTo(x,y)
           // MotionEvent.ACTION_MOVE -> path.lineTo(x, y)
            else -> return false
        }
        invalidate()
        return true
    }

    fun onUpdatePath(xStart: Float,yStart: Float,xStop: Float,yStop: Float){
        path.moveTo(xStart, yStart)
        path.lineTo(xStop, yStop)
        invalidate()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

}
