package ir.aryanmo.filechooserexample

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ir.aryanmo.filepicker.FilePicker
import ir.aryanmo.filepicker.FileChooserResultListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.view.View
import ir.aryanmo.filepicker.Utils.FileUtils


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val a = ArrayList<String>()
        Toast.makeText(this, a.size.toString(), Toast.LENGTH_LONG).show()

        text.setOnClickListener {
            FilePicker.openFileManager(this, true, null, arrayListOf(FilePicker.OPEN_CAMERA_FOR_PHOTO))
        }

        image.setOnClickListener {
            image.visibility = View.GONE
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        FilePicker.onActivityResult(this, requestCode, resultCode, data, object : FileChooserResultListener() {
            override fun onCameraPhotoResult(bitmap: Bitmap) {
                super.onCameraPhotoResult(bitmap)
                image.visibility = View.VISIBLE
                image.setImageBitmap(bitmap)
            }

            override fun onCameraVideoResult(uri: Uri) {
                super.onCameraVideoResult(uri)
                val f = File(uri.path)
                val size = f.length()

                Log.e("Aryan", "Path -> ${uri.path}")
                Log.e("Aryan", "Size -> $size")
                Log.e("Aryan", "mimeType -> ${FileUtils.getMimeType(this@MainActivity, uri)}")
                Log.e("Aryan", "File name -> ${f.name}")
                Log.e("Aryan", "File nameWithoutExtension -> ${f.nameWithoutExtension}")

//                FileUtils.launchVideo(this@MainActivity, uri.path)
            }

            override fun onFileSelectorResult(uris: ArrayList<Uri>) {
                super.onFileSelectorResult(uris)

                for (i in 0 until uris.size) {
                    if (FileUtils.isImageFile(this@MainActivity, uris[i])) {
                        image.visibility = View.VISIBLE
                        image.setImageBitmap(FileUtils.uriToBitmap(this@MainActivity, uris[i]))
                    }

                    if (FileUtils.isVideoFile(this@MainActivity, uris[i])) {
                        Log.e("Ari", uris[i].path)
                        Log.e("Ari", FileUtils.getPathFromUri(this@MainActivity, uris[i]))
                        FileUtils.launchVideo(this@MainActivity, uris[i])
                    }

                    if (FileUtils.isAudioFile(this@MainActivity, uris[i])) {
                        FileUtils.launchAudio(this@MainActivity, uris[i])
                    }
                }
            }
        })
    }

}
