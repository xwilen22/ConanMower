package com.example.conanmoverandroidapp

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer


data class Obstacle(
    val bitmap: Bitmap,
    val x: Float,
    val y: Float,
)


class PathCanvas(context: Context, attributeSet: AttributeSet?) : View(context) {
    private var paint: Paint = Paint()
    private var path: Path = Path()
    private var obstacles = mutableListOf<Obstacle>()
    private var oldAngle = 0
    private lateinit var wallDrawable: Drawable
    private lateinit var objectsDrawable: Drawable


    /*The boarder from the edges we do not want to cross*/
    private var eventHorizon = 300

    //WALL
    //Width="25.061384dp" Height="22.697dp"
    private val wallSize = arrayListOf(25, 23)

    //OBJECT
    //Width="20.9204dp" Height="25.247"
    private val objectsSize = arrayListOf(21, 25)

    /*TODO MOVE TO GLOBALS*/
    private val WALL_OBSTACLE = 0
    private val OBJECT_OBSTACLE = 1

    private var relativeFactor = 10f

    init {
        paint.isAntiAlias = true
        paint.color = Color.RED
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 12f
        paint.style = Paint.Style.STROKE
        setUpDrawables()

        val pathSessionsObserver = Observer<MutableList<TraveledPathSession>> {
            drawPathOnCanvas()
        }


        Globals.pathViewModel.traveledPathSessions.observe(
            Globals.pathLifeCycleOwner,
            pathSessionsObserver
        )
        Globals.pathViewModel.readDataFromRealtimeDatabase()
    }

/*
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

    private fun setUpDrawables()
    {
        val wallResourcesId =
            resources.getIdentifier("ic_obstacle_wall", "drawable", context.packageName)
        val objectsResourcesId =
            resources.getIdentifier("ic_obstacle_object", "drawable", context.packageName)
        wallDrawable = context.getDrawable(wallResourcesId)!!
        objectsDrawable = context.getDrawable(objectsResourcesId)!!
        //startDrawble = context.getDrawable(objectsResourcesId)

    }

    private fun onUpdatePath(
        xStart: Float,
        yStart: Float,
        xStop: Float,
        yStop: Float,
        obstacle: Int
    ) {
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


    private fun drawPathOnCanvas() {
        var startCoordinates = Coordinates(width.toFloat() / 2, height.toFloat() / 2)
        Globals.traveledPathSessionList[1].traveledPaths.forEach {
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
        return when (obstacle) {
            WALL_OBSTACLE -> wallDrawable!!.toBitmap(
                wallSize[0],
                wallSize[1],
                config = null
            )
            OBJECT_OBSTACLE -> objectsDrawable!!.toBitmap(
                objectsSize[0],
                objectsSize[1],
                config = null
            )
            else -> objectsDrawable!!.toBitmap(wallSize[0], wallSize[0], config = null)
        }
    }


    private fun parseToCoordinates(
        oldX: Float,
        oldY: Float,
        angle: Double,
        distance: Int
    ): Coordinates {
        // Convert angle in degree to angle in radians (only turns left)
        val angleInRadians = (oldAngle + angle) * Math.PI / 180

        oldAngle += (angle).toInt()

        // TODO: Adjust distance depending on screen-size
        /// var relativeDistance
        // updateRelativeFactor(oldX.toInt(),oldY.toInt())
        //updateRelativeFactor(oldX.toInt(), oldY.toInt())
        /*GETS THE NUMBER OF PATHS THAT CAN FIT ON THE X-AXIS, div it with a good number*/
        //var relativeWidth = Math.floorDiv(width,distance) / relativeFactor
        /*GETS THE NUMBER OF PATHS THAT CAN FIT ON THE Y-AXIS, div it with a good number*/
        //var relativeHeight = Math.floorDiv(height,distance) / relativeFactor

        // Calculate x and y coordinates for new point in map based on..
        // previous point, distance and angle
        val newX = oldX + (distance * kotlin.math.cos(angleInRadians).toFloat()) * 5
        val newY = oldY + (distance * kotlin.math.sin(angleInRadians).toFloat()) * 5

        return Coordinates(newX, newY)
    }

    private fun updateRelativeFactor(x: Int, y: Int) {
        if (x > (width - eventHorizon) || y > (height - eventHorizon)) {
            relativeFactor *= 1.2f
            //TODO: Find a way to redraw the path here
        }

    }


}
