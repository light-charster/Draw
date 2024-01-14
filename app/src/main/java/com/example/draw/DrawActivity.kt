package com.example.draw

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.io.File


class DrawActivity: AppCompatActivity(){
    private val mediaPlayer= MediaPlayer()
    private var takePhoto=1
    private lateinit var imageUri:Uri
    private lateinit var outputImage: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        val id = intent.getStringExtra("P")
        val edit= getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        edit.putString("page",id)
        edit.apply()
        val img = findViewById<DrawingImageView>(R.id.imageViews)
        img.setImageResource(R.drawable.ba)
        id?.let { img.setImageURL(it) }
        val layoutManager = StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL)
        val recycler = findViewById<RecyclerView>(R.id.pencilView)
        recycler.layoutManager = layoutManager
        val adapter= img.paint?.let { PencilAdapter(this, it) }
        recycler.adapter =adapter
        val assertManager= assets
        val openFd = assertManager.openFd("123.mp3")
        mediaPlayer.setDataSource(openFd.fileDescriptor,openFd.startOffset,openFd.length)
        mediaPlayer.prepare()
        mediaPlayer.start()
        val button = findViewById<Button>(R.id.crea)
        button.setOnClickListener {
            outputImage = File(externalCacheDir,"output.jpg")
            if (outputImage.exists()){
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri=
                FileProvider.getUriForFile(this,"com.example.draw",outputImage)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
            startActivityForResult(intent,takePhoto)
        }
    }
    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            takePhoto->{
                if (resultCode== Activity.RESULT_OK){
                    val bitmap= BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    val img = findViewById<ImageView>(R.id.imageViews)
                    img.setImageBitmap(bitmap)
                }
            }
        }
    }
    private fun rotateRequired(bitmap: Bitmap):Bitmap{
        val exif= ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL)
        return when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90-> rotatebitmap(bitmap,90)
            ExifInterface.ORIENTATION_ROTATE_180->rotatebitmap(bitmap,180)
            ExifInterface.ORIENTATION_ROTATE_270->rotatebitmap(bitmap,270)
            else->bitmap
        }
    }
    private fun rotatebitmap(bitmap: Bitmap,degree :Int):Bitmap{
        val matrix= Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
        bitmap.recycle()
        return rotatedBitmap
    }
}