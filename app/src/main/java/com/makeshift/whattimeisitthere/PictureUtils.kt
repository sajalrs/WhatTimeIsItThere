package com.makeshift.whattimeisitthere

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.media.ExifInterface
import java.io.IOException


fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int):
        Bitmap {
    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true

    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    var inSampleSize = 1

    if (srcHeight > destHeight || srcWidth > destWidth) {

        val heightScale = srcHeight / destHeight
        val widthScale = srcWidth / destWidth

        val sampleScale = if (heightScale > widthScale) {
            heightScale
        } else {
            widthScale
        }

        inSampleSize = Math.round(sampleScale)

    }

    options = BitmapFactory.Options()
    options.inSampleSize = inSampleSize

    return rotateImageIfRequired(BitmapFactory.decodeFile(path, options), path)
}

fun getScaledBitmap(path: String, activity: Activity): Bitmap {
    val size = Point()
    activity.windowManager.defaultDisplay.getSize(size)
    return getScaledBitmap(path, size.x, size.y)
}

@Throws(IOException::class)
fun rotateImageIfRequired(img: Bitmap, path: String): Bitmap {
    val ei = ExifInterface(path)
    val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    when(orientation){
        ExifInterface.ORIENTATION_ROTATE_90 ->
            return rotateImage(img, 90)
        ExifInterface.ORIENTATION_ROTATE_180 ->
            return rotateImage(img, 180)
        ExifInterface.ORIENTATION_ROTATE_270 ->
            return rotateImage(img, 270)
        else ->
            return img

    }
}

fun rotateImage(img: Bitmap, degree: Int): Bitmap{
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImage = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImage
}