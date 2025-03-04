package com.flexnet.presentation.feature

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

internal fun String.toIntOrZero(): Int {
    return try {
        this.toInt()
    } catch (e: Exception) {
        0
    }
}

internal fun String.toCapitalizeEachWord(): String {
    return this.split(" ").joinToString(" ") {
        if (it.contains("/")) {
            it.split("/").joinToString("/") { word ->
                word.replaceFirstChar { firstChar -> firstChar.uppercase() }
            }
        } else {
            it.replaceFirstChar { firstChar -> firstChar.uppercase() }
        }
    }
}

internal fun Context.shareText(text: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_TEXT, text)
    intent.type = "text/plain"
    this.startActivity(Intent.createChooser(intent, "Share Via"))
}

internal fun shareTextAsFile(context: Context, text: String, fileName: String = "http_inspector.txt") {
    try {
        // Create or overwrite the .txt file
        val file = File(context.getExternalFilesDir(null), fileName)
        FileOutputStream(file).use {
            it.write(text.toByteArray())
        }

        // Get the content Uri for the file using FileProvider
        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Use the authority defined in the manifest
            file,
        )

        // Create the intent to share the file
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant permission to the receiving app
        }

        // Start the sharing activity
        context.startActivity(Intent.createChooser(shareIntent, "Share Via"))
    } catch (e: IOException) {
        e.printStackTrace()
        // Handle the error
    }
}
