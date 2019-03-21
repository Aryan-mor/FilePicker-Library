package ir.aryanmo.filepicker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.os.Build
import java.io.File
import ir.aryanmo.filepicker.Utils.FileUtils


object FilePicker {
    open val OPEN_CAMERA_FOR_PHOTO = 1100
    open val OPEN_CAMERA_FOR_VIDEO = 1200
    open val OPEN_GALLERY = 1300


    open val ANY_TYPE = 1010
    open val IMAGE_TYPE = 1011
    open val VIDEO_TYPE = 1012
    open val AUDIO_TYPE = 1013

    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?, selectorResult: FileChooserResultListener) {
        try {

            if (resultCode != Activity.RESULT_OK)
                return

            when (requestCode) {
                OPEN_CAMERA_FOR_PHOTO -> {
                    try {
                        selectorResult.onCameraPhotoResult(data!!.extras!!.get("data") as Bitmap)
                    } catch (e: Exception) {
                        logError("onActivityResult::OPEN_CAMERA_FOR_PHOTO", e)
                    }
                }
                OPEN_CAMERA_FOR_VIDEO -> {
                    val videoUri = data!!.data
                    var path = FileUtils.getPathFromUri(activity, videoUri)
                    selectorResult.onCameraVideoResult(Uri.fromFile(File(path)))
                }
                OPEN_GALLERY -> {
                    val result = arrayListOf<Uri>()
                    try {
                        if (null != data!!.clipData) {
                            for (i in 0 until data.clipData.itemCount) {
                                val uri = data.clipData.getItemAt(i).uri
                                result.add(uri)
                            }
                        } else {
                            val uri = data.data
                            result.add(uri)
                        }
                    } catch (e: Exception) {
                        logError("onActivityResult::OPEN_GALLERY", e)
                    }
                    selectorResult.onFileSelectorResult(result)
                }
            }
        } catch (e: Exception) {
            logError("onActivityResult", e)
        }
    }

    fun openCameraForPhoto(activity: Activity, title: String?) {
        try {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            setTitle(cameraIntent, title)
            activity.startActivityForResult(cameraIntent, OPEN_CAMERA_FOR_PHOTO)
        } catch (e: Exception) {
            logError("openCameraForPhoto", e)
        }
    }

    fun openCameraForVideo(activity: Activity, title: String?) {
        try {
            val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            setTitle(cameraIntent, title)
            activity.startActivityForResult(cameraIntent, OPEN_CAMERA_FOR_VIDEO)
        } catch (e: Exception) {
            logError("openCameraForPhoto", e)
        }
    }

    fun openFileManager(activity: Activity, multiple: Boolean, title: String?, fileType: Int) {
        openFileManager(activity, multiple, title, arrayListOf(fileType))
    }

    fun openFileManager(activity: Activity, multiple: Boolean, title: String?, fileTypes: ArrayList<Int>) {
        try {
            val mimeTypes = getMIME(fileTypes)
            val type = getType(mimeTypes)

            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = type
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

            setTitle(intent, title).setMultiple(intent, multiple)

            var pickIntent: Intent? = null
            if (fileTypes.size == 1) {
                pickIntent = getPickIntent(fileTypes[0])
            }

            val chooserIntent: Intent
            if (pickIntent != null) {
                chooserIntent = Intent.createChooser(pickIntent, title)
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intent))
            } else {
                chooserIntent = Intent.createChooser(intent, title)
            }



            activity.startActivityForResult(chooserIntent, OPEN_GALLERY)

        } catch (e: Exception) {
            logError("openFileManager", e)
        }
    }

    private fun isKitKat(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    private fun setTitle(intent: Intent, title: String?): FilePicker {
        if (title != null) {
            Intent.createChooser(intent, title)
        }
        return this

    }

    private fun setMultiple(intent: Intent, multiple: Boolean): FilePicker {
        if (multiple) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        return this
    }

    private fun getMIME(MIMEType: ArrayList<Int>): ArrayList<String> {
        val MIME = arrayListOf<String>()
        for (i in 0 until MIMEType.size) {
            when (MIMEType[i]) {
                ANY_TYPE -> {
                    return arrayListOf("*/*")
                }
                IMAGE_TYPE -> {
                    MIME.add("image/*")
                }
                VIDEO_TYPE -> {
                    MIME.add("video/*")
                }
                AUDIO_TYPE -> {
                    MIME.add("audio/*")
                }
            }
        }
        return MIME
    }

    private fun getType(MIMEType: ArrayList<String>): String {
        var type: String = ""
        for (i in 0 until MIMEType.size) {
            if (type == "") {
                type = MIMEType[i]
                continue
            }
            type = "$type, ${MIMEType[i]}"
        }
        return type
    }

    private fun getPickIntent(MIMEType: Int): Intent? {
        return when (MIMEType) {
            IMAGE_TYPE -> Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            VIDEO_TYPE -> Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            AUDIO_TYPE -> Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
            else -> null
        }


    }

    private fun logError(s: String, e: Exception) {
        Log.e("FilePicker", "FilePicker::$s Error : ${e.message}")
    }
}