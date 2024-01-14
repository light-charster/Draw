package com.example.draw

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class DrawingImageView : AppCompatImageView {
    var paint: Paint? = null
    private var canvasBitmap: Canvas? = null
    private var bitmap: Bitmap? = null
    private var previousX = 0f
    private var previousY = 0f
    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        val drawable = drawable as BitmapDrawable
        bitmap = drawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        canvasBitmap = Canvas(bitmap!!)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        val drawable = drawable as BitmapDrawable
        bitmap = drawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        canvasBitmap = Canvas(bitmap!!)
    }
    private fun init() {
        paint = Paint()
        paint!!.color = Color.BLACK
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeWidth = 5f
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas);
        canvas.drawBitmap(bitmap!!, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                previousX = x
                previousY = y
            }

            MotionEvent.ACTION_MOVE -> {
                canvasBitmap!!.drawLine(previousX, previousY, x, y, paint!!)
                invalidate()
                previousX = x
                previousY = y
            }

            MotionEvent.ACTION_UP -> {}
        }
        invalidate()
        return true
    }
    val GET_DATA_SUCCESS = 1
    val NETWORK_ERROR = 2
    val SERVER_ERROR = 3
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MyImageView.GET_DATA_SUCCESS -> {
                    val bitmap = msg.obj as Bitmap
                    setImageBitmap(bitmap)
                }

                MyImageView.NETWORK_ERROR -> Toast.makeText(
                    context,
                    "网络连接失败",
                    Toast.LENGTH_SHORT
                ).show()

                MyImageView.SERVER_ERROR -> Toast.makeText(
                    context,
                    "服务器发生错误",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun setImageURL(path: String?) {
        //开启一个线程用于联网
        object : Thread() {
            override fun run() {
                try {
                    //把传过来的路径转成URL
                    val url = URL(path)
                    //获取连接
                    val connection = url.openConnection() as HttpURLConnection
                    //使用GET方法访问网络
                    connection.requestMethod = "GET"
                    //超时时间为10秒
                    connection.connectTimeout = 10000
                    //获取返回码
                    val code = connection.responseCode
                    if (code == 200) {
                        val inputStream = connection.inputStream
                        //使用工厂把网络的输入流生产Bitmap
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        //利用Message把图片发给Handler
                        val msg = Message.obtain()
                        msg.obj = bitmap
                        msg.what = MyImageView.GET_DATA_SUCCESS
                        handler.sendMessage(msg)
                        inputStream.close()
                    } else {
                        //服务启发生错误
                        handler.sendEmptyMessage(MyImageView.SERVER_ERROR)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    //网络连接错误
                    handler.sendEmptyMessage(MyImageView.NETWORK_ERROR)
                }
            }
        }.start()
    }
}
