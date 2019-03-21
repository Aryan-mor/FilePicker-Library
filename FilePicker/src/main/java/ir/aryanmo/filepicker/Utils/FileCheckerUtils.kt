package ir.aryanmo.filepicker.Utils

import android.content.Context
import android.net.Uri
import android.util.Log
import java.lang.Exception


interface FileCheckerUtils {


    fun isImageFile(context: Context, uri: Uri): Boolean {
        val mime = FileUtils.getMimeType(context, uri)
        if (mime != null && mime.startsWith("image")) {
            return true
        }
        return arrayListOf("jpg", "jpeg", "png", "gif").indexOf(FileUtils.getMimeType(context, uri)) != -1
    }

    fun isVideoFile(context: Context, uri: Uri): Boolean {
        val mime = FileUtils.getMimeType(context, uri)
        if (mime != null && mime.startsWith("video")) {
            return true
        }
        return arrayListOf("mp4", "mpg", "3gp", "avi").indexOf(FileUtils.getMimeType(context, uri)) != -1
    }

    fun isAudioFile(context: Context, uri: Uri): Boolean {
        val mime = FileUtils.getMimeType(context, uri)
        if (mime != null && mime.startsWith("audio")) {
            return true
        }
        return arrayListOf("mp3", "wav").indexOf(FileUtils.getMimeType(context, uri)) != -1
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }


    private fun logError(s: String, e: Exception) {
        Log.e("FileCheckerUtils", "FileCheckerUtils::$s Error : ${e.message}")
    }

}