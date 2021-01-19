package net.memish.durakcepte.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import net.memish.durakcepte.R

object Utils {
    /*
    fun getRawResourceFromPosition(position: Int): Int {
        when (position) {
            0 -> return R.raw.c1
            1 -> return R.raw.c2
            2 -> return R.raw.c3
            3 -> return R.raw.c3_eks
            4 -> return R.raw.c4
            5 -> return R.raw.c5
            6 -> return R.raw.c7
            7 -> return R.raw.c8
            8 -> return R.raw.c9
            9 -> return R.raw.c10
            10 -> return R.raw.c11c
            11 -> return R.raw.c11g
            12 -> return R.raw.c11k
            13 -> return R.raw.c11k_eks
            14 -> return R.raw.c11ks
            15 -> return R.raw.c960
            16 -> return R.raw.ct1
            17 -> return R.raw.ct2
            18 -> return R.raw.ct3
            else -> return -1
        }
    }
    val stations = arrayOf(
        "Ç1",
        "Ç2",
        "Ç3",
        "Ç3-E",
        "Ç4",
        "Ç5",
        "Ç7",
        "Ç8",
        "Ç9",
        "Ç10",
        "Ç11Ç",
        "Ç11G",
        "Ç11K",
        "Ç11K-E",
        "Ç11KS",
        "Ç960",
        "ÇT1",
        "ÇT2",
        "ÇT3"
    )
    */
    fun getFilePath(context: Context?, uri: Uri): String? {

        var data = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null

        when {
            isExternalStorageDocument(data) -> {
                val docId = DocumentsContract.getDocumentId(data)
                val split = docId.split(":").toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

            isDownloadsDocument(data) -> {
                val id = DocumentsContract.getDocumentId(data)
                data = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
            }

            isMediaDocument(data) -> {
                val docId = DocumentsContract.getDocumentId(data)
                val split = docId.split(":").toTypedArray()
                val type = split[0]

                when (type) {

                    "image" -> {
                        data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }

                    "video" -> {
                        data = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }

                    "audio" -> {
                        data = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                }

                selection = "_id=?"
                selectionArgs = arrayOf(split[1])

            }

        }

        if ("content".equals(data.scheme, ignoreCase = true)) {

            if (isGooglePhotosUri(data)) {
                return data.lastPathSegment
            }

            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context?.contentResolver?.query(data, projection, selection, selectionArgs, null) ?: return null
            val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            cursor.moveToFirst()

            val path = cursor.getString(index)

            cursor.close()

            return path

        } else if ("file".equals(data.scheme, ignoreCase = true)) {
            return data.path
        }

        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}