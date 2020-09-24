package com.makeshift.whattimeisitthere

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageButton
import androidx.databinding.BindingAdapter

object BitmapBindingAdapter{ @BindingAdapter("imageBitmap")
@JvmStatic
fun loadImage(imageButton: ImageButton, bitmap: Any){

    if(bitmap is Bitmap){
        imageButton.setImageBitmap(bitmap)
    } else if(bitmap is BitmapDrawable) {
        imageButton.setImageDrawable(bitmap)
    }


}



}