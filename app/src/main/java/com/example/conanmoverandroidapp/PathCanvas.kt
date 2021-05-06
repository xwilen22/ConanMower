package com.example.conanmoverandroidapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import kotlin.random.Random


class PathCanvas(context: Context, attributeSet: AttributeSet?) : View(context) {
    private var paint: Paint = Paint()
    private var path:  Path = Path()
    private var screenHeight = 1000
    private var screenWidth = 1000

    init {
        paint.isAntiAlias = true
        paint.color = Color.RED
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE

        val pathSessionsObserver = Observer<MutableList<TraveledPathSession>> {
            drawPathOnCanvas()
        }

        Globals.pathViewModel.traveledPathSessions.observe(Globals.pathLifeCycleOwner, pathSessionsObserver)
        Globals.pathViewModel.readDataFromRealtimeDatabase()
    }

   /* override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        screenHeight = heightMeasureSpec
        screenWidth = widthMeasureSpec
    }*/

   /*PUT CODE TO TEST IN HERE FOR NOW*/
    fun test() {
        val x = Random.nextInt(0, screenWidth) * (1 + Random.nextFloat())
        val y = Random.nextInt(0, screenHeight) *(1 + Random.nextFloat())
        val x2 = Random.nextInt(0, screenWidth) * (1 + Random.nextFloat())
        val y2 = Random.nextInt(0, screenHeight) * (1 + Random.nextFloat())

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

    private fun onUpdatePath(xStart: Float, yStart: Float, xStop: Float, yStop: Float){
        path.moveTo(xStart, yStart)
        path.lineTo(xStop, yStop)

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /* Place the starting point at the bottom left instead of upper left corner */
        canvas.scale(1f, -1f, width / 2f, height / 2f)
        canvas.drawPath(path, paint)
    }

    private fun drawPathOnCanvas(){
        var startCoordinates = Coordinates(0.toFloat(), 0.toFloat())
        Globals.traveledPathSessionList[0].traveledPaths.forEach {
            /* Parse each data point */
            val stopCoordinates = parseToCoordinates(
                startCoordinates.xCoordinate,
                startCoordinates.yCoordinate,
                it.CurrentAngle.toDouble(),
                it.TraveledDistance
            )

            /* Update UI */
            onUpdatePath(
                startCoordinates.xCoordinate,
                startCoordinates.yCoordinate,
                stopCoordinates.xCoordinate,
                stopCoordinates.yCoordinate
                //it.StoppedByObstacle
            )

            /* Set new start coordinates from this data point, for the next data point */
            startCoordinates = Coordinates(
                stopCoordinates.xCoordinate,
                stopCoordinates.yCoordinate
            )
        }
    }

    private fun parseToCoordinates(oldX: Float, oldY: Float, angle: Double, distance: Int) : Coordinates {
        // Convert angle in degree to angle in radians
        val angleInRadians = angle * Math.PI / 180

        // TODO: Adjust distance depending on screen-size
        // var relativeDistance = This is some code

        // Calculate x and y coordinates for new point in map based on..
        // previous point, distance and angle
        val newX = oldX + (distance * kotlin.math.cos(angleInRadians).toFloat())
        val newY = oldY + (distance * kotlin.math.sin(angleInRadians).toFloat())

        return Coordinates(newX, newY)
    }

}
