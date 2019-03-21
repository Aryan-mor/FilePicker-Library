package ir.aryanmo.filepicker.Utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat

interface FileLauncherUtils {

    fun launchImage(context: Context, path: String) {
        launchVideo(context,Uri.parse(path))
    }

    fun launchImage(context: Context, uri: Uri) {
        val intent = Intent(android.content.Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "image/*")
        context.startActivity(intent)
    }

    fun launchVideo(context: Context, path: String) {
        launchVideo(context,Uri.parse(path))
    }

    fun launchVideo(context: Context, uri: Uri) {
        val intent = Intent(android.content.Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "video/*")
        context.startActivity(intent)
    }

    fun launchAudio(context: Context, path: String) {
       launchVideo(context,path)
    }

    fun launchAudio(context: Context, uri: Uri) {
        val intent = Intent(android.content.Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "audio/*")
        context.startActivity(intent)
    }

}