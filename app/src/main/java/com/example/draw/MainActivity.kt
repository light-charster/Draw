package com.example.draw

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.io.File


class MainActivity : AppCompatActivity(){
//    private val list = ArrayList<Picture>()
    private var takePhoto=1
    private lateinit var imageUri: Uri
    private lateinit var outputImage: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val gets = Gets()
        val str = gets.Str()

        val layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = layoutManager
        val adapter = PictureAdapter(str)
        recycler.adapter =adapter
        val g= getSharedPreferences("data", Context.MODE_PRIVATE)
        val picture=g.getString("page","")
        if (picture!="null" && picture!="") {
            val intent = Intent(this, DrawActivity::class.java)
            intent.putExtra("P",picture)
            startActivity(intent)
        }
        val button = findViewById<Button>(R.id.pay)
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
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            takePhoto->{

            }
        }
    }
    private fun rotateRequired(bitmap: Bitmap): Bitmap {
        val exif= ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90-> rotatebitmap(bitmap,90)
            ExifInterface.ORIENTATION_ROTATE_180->rotatebitmap(bitmap,180)
            ExifInterface.ORIENTATION_ROTATE_270->rotatebitmap(bitmap,270)
            else->bitmap
        }
    }
    private fun rotatebitmap(bitmap: Bitmap, degree :Int): Bitmap {
        val matrix= Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap= Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
        bitmap.recycle()
        return rotatedBitmap
    }
}

