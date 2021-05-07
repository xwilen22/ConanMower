package com.example.conanmoverandroidapp

import android.R
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Observer


data class Obstacle (
    val bitmap: Bitmap,
    val x: Float,
    val y: Float, )


class PathCanvas(context: Context, attributeSet: AttributeSet?) : View(context) {
    private var paint: Paint = Paint()
    private var path:  Path = Path()
    private var obstacles = mutableListOf<Obstacle>()

    /*TODO MOVE TO GLOBALS*/
    private val WALL_OBSTACLE = 0
    private val OBJECT_OBSTACLE = 1

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
   /* fun test() {
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
    }*/

    private fun onUpdatePath(xStart: Float, yStart: Float, xStop: Float, yStop: Float, obstacle: Int){
        path.moveTo(xStart, yStart)
        path.lineTo(xStop, yStop)
        obstacles.add(Obstacle(getBitMap(obstacle), xStop, yStop))
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
        obstacles.forEach {
            canvas.drawBitmap(it.bitmap, it.x, it.y, paint)
        }
    }


    private fun drawPathOnCanvas(){
        var startCoordinates = Coordinates(width.toFloat()/2, height.toFloat()/2)
        Globals.traveledPathSessionList[0].traveledPaths.forEach {
            // Parse each data point
            val stopCoordinates = parseToCoordinates(
                startCoordinates.xCoordinate,
                startCoordinates.yCoordinate,
                it.CurrentAngle.toDouble(),
                it.TraveledDistance
            )

            // Update UI
            onUpdatePath(
                startCoordinates.xCoordinate,
                startCoordinates.yCoordinate,
                stopCoordinates.xCoordinate,
                stopCoordinates.yCoordinate,
                it.StoppedByObstacle
            )

            // Set new start coordinates from this data point, for the next data point
            startCoordinates = Coordinates(
                stopCoordinates.xCoordinate,
                stopCoordinates.yCoordinate
            )
        }
    }


    private fun getBitMap(obstacle: Int): Bitmap {
       return when(obstacle){
            WALL_OBSTACLE -> BitmapFactory.decodeResource(resources, R.drawable.checkbox_off_background)
            OBJECT_OBSTACLE -> BitmapFactory.decodeResource(resources, R.drawable.sym_call_incoming)
            else -> BitmapFactory.decodeResource(resources, R.drawable.ic_btn_speak_now)
        }
    }


    private fun parseToCoordinates(oldX: Float, oldY: Float, angle: Double, distance: Int) : Coordinates {
        // Convert angle in degree to angle in radians
        val angleInRadians = angle * Math.PI / 180

        // TODO: Adjust distance depending on screen-size
        var relativeDistance = height/600

        // Calculate x and y coordinates for new point in map based on..
        // previous point, distance and angle
        val newX = oldX + (distance * kotlin.math.cos(angleInRadians).toFloat()) * relativeDistance
        val newY = oldY + (distance * kotlin.math.sin(angleInRadians).toFloat()) * relativeDistance

        return Coordinates(newX, newY)
    }

}
