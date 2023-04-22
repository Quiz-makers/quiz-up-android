package com.quizmakers.core.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun String.decodeImage(): Bitmap {
    val byteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}