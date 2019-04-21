package ar.edu.utn.frba.mobile.a2019c1.utils.storage.fileSystem

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ExternalStorage {
    companion object {
        fun getFileUri(fileName: String): String? {
            val file = File(getAppFolder() + fileName)
            if (file.exists()) {
                return file.toURI().toString()
            }
            return null
        }

        fun saveFile(bitmap: Bitmap, fileName: String) : File {
            val file = File(getAppFolder() + fileName + ".JPEG")
            try {
                file.createNewFile()
                val ostream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
                ostream.flush()
                ostream.close()
            } catch (e: IOException) {
                Log.e("IOException", e.localizedMessage)
            }

            return file
        }

        fun deleteFile(fileName: String) {
            val file = File(getAppFolder() + fileName)
            if (file.exists()) {
                file.delete()
            }
        }

        fun getCacheFileUri(context: Context, fileName: String): String? {
            val file = File(context.externalCacheDir.path + fileName)
            if (file.exists()) {
                return file.toURI().toString()
            }
            return null
        }

        fun saveFileInCache(context: Context, bitmap: Bitmap, fileName: String) : File {
            val file = File(context.externalCacheDir.path + fileName)
            try {
                file.createNewFile()
                val ostream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
                ostream.flush()
                ostream.close()
            } catch (e: IOException) {
                Log.e("IOException", e.localizedMessage)
            }

            return file
        }

        fun deleteFileFromCache(context: Context, fileName: String) {
            val file = File(context.externalCacheDir.path + fileName)
            if (file.exists()) {
                file.delete()
            }
        }

        fun getFiles(): Array<out File>? {
            val appFolder = ExternalStorage.getAppFolder()

            val directory = File(appFolder)

            return directory.listFiles()
        }

        fun getBitmapFromGallery(context: Context, path: Uri, width: Int, height: Int): Bitmap {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(path, filePathColumn, null, null, null)
            var picturePath:String? = path.path
            if(cursor != null){
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                picturePath = cursor.getString(columnIndex)
                cursor.close()
            }

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(picturePath, options)

            // Calculate inSampleSize
            options.inSampleSize =
                calculateInSampleSize(options, width, height)

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(picturePath, options)
        }

        private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
        ): Int {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                val halfHeight = height / 2
                val halfWidth = width / 2

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }

        private fun getAppFolder(): String {
            val folder = File(Environment.getExternalStorageDirectory().path + "/clases_2018c1/")
            if(!folder.exists()){
                folder.mkdirs()
            }

            return Environment.getExternalStorageDirectory().path + "/clases_2018c1/"
        }
    }
}