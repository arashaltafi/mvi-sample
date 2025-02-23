package com.arash.altafi.mvisample.utils.ext

import android.content.Context
import android.net.Uri
import android.util.Base64
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException

/**
 * Reads the content of the URI and returns it as a Base64 encoded string.
 */
fun Context.uriToBase64(uri: Uri): String {
    return try {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val bytes = inputStream.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } ?: ""
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}

/**
 * Converts the image at the given URI into a MultipartBody.Part for form data upload.
 *
 * @param partName The form field name, e.g. "image"
 * @param fileName The file name to be used in the form data, e.g. "image.jpg"
 */
fun Context.uriToFormData(
    uri: Uri,
    partName: String = "image",
    fileName: String = "image.png"
): MultipartBody.Part? {
    return try {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val bytes = inputStream.readBytes()
            val mediaType = "image/*".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, bytes)
            MultipartBody.Part.createFormData(partName, fileName, requestBody)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
