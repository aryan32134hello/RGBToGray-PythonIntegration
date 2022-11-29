package com.example.pythonintegration

//import android.util.Base64

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val submit:Button = findViewById(R.id.submit)
        val iv:ImageView = findViewById(R.id.imageView1)
        val iv2:ImageView = findViewById(R.id.imageView2)
        var imageString:String =" "
        //val drawable:BitmapDrawable
        //val bitmap:Bitmap

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }



        submit.setOnClickListener() {
            val drawable: Drawable = iv.drawable

            val bitmap: Bitmap = getBitmapFromDrawable(drawable)
            imageString = getStringImage(bitmap)

            val py = Python.getInstance()
            val pyo: PyObject = py.getModule("script")
            val obj: PyObject = pyo.callAttr("main", imageString)
            val str: String = obj.toString()
            val data = android.util.Base64.decode(str, android.util.Base64.DEFAULT)
            val bmp: Bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            iv2.setImageBitmap(bmp)
        }
    }

    private fun getStringImage(bitmap: Bitmap): String {
        val baos:ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos)
        val imageByte = baos.toByteArray()
        val encodedImage:String = android.util.Base64.encodeToString(imageByte, android.util.Base64.DEFAULT)
        return encodedImage
    }
    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bmp = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bmp
    }
}