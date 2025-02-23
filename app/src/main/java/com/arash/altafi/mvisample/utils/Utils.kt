package com.arash.altafi.mvisample.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object Utils {

    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    fun setPersianDigits(src: String?): String? {
        val result = StringBuilder("")
        var unicode = 0
        if (src != null) {
            for (i in src.indices) {
                unicode = src[i].code
                if (unicode in 48..57) {
                    result.append((unicode + 1728).toChar())

                    // Log.e("unicode", unicode + "");
                    // Log.e("persian character", (unicode + 1728) + " " + (char) (unicode + 1728));
                } else {
                    result.append(src[i])
                }
            }
        }
        return result.toString()
    }

    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime()
                .exec(arrayOf("/system/xbin/which", "su"))
            val `in` = BufferedReader(InputStreamReader(process.inputStream))
            `in`.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    fun isDevMode(context: Context): Boolean =
        Settings.Secure.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            0
        ) != 0

}