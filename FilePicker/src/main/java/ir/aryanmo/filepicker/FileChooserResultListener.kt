package ir.aryanmo.filepicker

import android.graphics.Bitmap
import android.net.Uri

abstract class FileChooserResultListener {
    open fun onCameraPhotoResult(bitmap:Bitmap) {

    }
    open fun onCameraVideoResult(uri: Uri) {

    }

    open fun onFileSelectorResult(uris:ArrayList<Uri>) {

    }
}
