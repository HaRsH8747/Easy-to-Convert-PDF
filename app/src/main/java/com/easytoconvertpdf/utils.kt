package com.easytoconvertpdf

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import com.easytoconvertpdf.adapter.ImageDocument


class utils {
    companion object{
        var documents: ArrayList<ImageDocument>? = ArrayList()
    }

    fun isPermissionGranted(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readExtStorage = ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            readExtStorage == PackageManager.PERMISSION_GRANTED
        }
    }
}