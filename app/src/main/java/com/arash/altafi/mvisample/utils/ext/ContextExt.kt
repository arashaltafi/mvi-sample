package com.arash.altafi.mvisample.utils.ext

import android.app.Activity
import android.app.ActivityManager
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.arash.altafi.mvisample.MainActivity
import com.arash.altafi.mvisample.R
import java.io.File
import java.io.FileOutputStream

fun Context.isDarkTheme(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Context.inflateView(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(this)
        .inflate(layoutRes, null)
}

fun Context.makeDialog(
    @LayoutRes layoutRes: Int,
    @StyleRes themRes: Int,
    isBottom: Boolean = false
): AppCompatDialog {
    return AppCompatDialog(this, themRes)
        .apply {
            setContentView(inflateView(layoutRes))
            if (isBottom) {
                val wManager: WindowManager.LayoutParams = this.window?.attributes!!
                wManager.gravity = Gravity.BOTTOM or Gravity.CENTER
            }
        }
}

fun Context.openGoogleMap(lat: String, lng: String) {
    try {
        val strUri = "http://maps.google.com/maps?q=loc:$lat,$lng"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        ContextCompat.startActivity(this, intent, null)
    } catch (e: ActivityNotFoundException) {
        Log.e("openGoogleMap", "${e.message}")
    }
}

fun Context.openMap(lat: String, lng: String) {
    val intent =
        Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:$lat,$lng"))
    ContextCompat.startActivity(this, intent, null)
}

fun Context.openAppInfoSetting() {
    //redirect user to app Settings
    val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    i.addCategory(Intent.CATEGORY_DEFAULT)
    i.data = Uri.parse("package:$packageName")
    ContextCompat.startActivity(this, i, null)
}

fun Context.openURL(url: String) {
    try {
        val fullUrl = if (url.startsWith("http")) url else "http://$url"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
    }
}

fun Context.openDownloadURL(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    when {
        this.isInstalled("com.android.chrome") -> intent.setPackage("com.android.chrome")
        this.isInstalled("org.mozilla.firefox") -> intent.setPackage("org.mozilla.firefox")
        this.isInstalled("com.opera.mini.android") -> intent.setPackage("com.opera.mini.android")
        this.isInstalled("com.opera.mini.android.Browser") -> intent.setPackage("com.opera.mini.android.Browser")
        else -> this.openURL(url)
    }
    startActivity(intent)
}

private fun Context.isInstalled(packageName: String): Boolean {
    return try {
        this.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (_: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.share(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun Context.shareTextWithImage(
    bitmap: Bitmap,
    title: String
) {
    val file = File(externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
    val out = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    out.close()
    val bmpUri = if (Build.VERSION.SDK_INT < 24) {
        Uri.fromFile(file)
    } else {
        FileProvider.getUriForFile(
            this, "$packageName.fileprovider", file
        )
    }

    val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
    StrictMode.setVmPolicy(builder.build())

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "image/*"
        putExtra(Intent.EXTRA_TEXT, title)
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_STREAM, bmpUri)
    }

    val shareIntent = Intent.createChooser(sendIntent, "Share Image")
    startActivity(shareIntent)
}

fun Context.openCall(phoneNumber: String) {
    ContextCompat.startActivity(
        this,
        Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", phoneNumber, null)
        ),
        null
    )
}

fun Context.openSMS(mobile: String, body: String = "") {
    val smsIntent = Intent(Intent.ACTION_VIEW)
    smsIntent.data = Uri.parse("sms:$mobile")
    smsIntent.putExtra("sms_body", body)
    ContextCompat.startActivity(this, smsIntent, null)
}

fun Context.shareApp() {
    val app: ApplicationInfo = applicationContext.applicationInfo
    val filePath: String = app.sourceDir
    val intent = Intent(Intent.ACTION_SEND)

    // MIME of .apk is "application/vnd.android.package-archive".
    // but Bluetooth does not accept this. Let's use "*/*" instead.
    intent.type = "*/*"


    // Append file and send Intent
    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(File(filePath)))
    startActivity(Intent.createChooser(intent, getString(R.string.app_name)))
}

fun Context.addContactIntent(): Intent {
    return Intent(Intent.ACTION_INSERT_OR_EDIT).apply {
        type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
    }
}


fun Context.addAsContactAutomatic(displayName: String?, mobileNumber: String?) {
    val ops = ArrayList<ContentProviderOperation>()
    ops.add(
        ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build()
    )

    // Names
    if (displayName != null) {
        ops.add(
            ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                    displayName
                ).build()
        )
    }

    // Mobile Number
    if (mobileNumber != null) {
        ops.add(
            ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                ).build()
        )
    }

    // Asking the Contact provider to create a new contact
    try {
        contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
    } catch (_: Exception) {
    }

//    Toast.makeText(context, "Contact $displayName added.", Toast.LENGTH_SHORT)
//        .show()
}

fun Context.openEmail(
    addresses: Array<String>,
    cc: Array<String> = emptyArray(),
    bcc: Array<String> = emptyArray(),
    subject: String? = null,
    message: String? = null
) {
    //https://developer.android.com/guide/components/intents-common#ComposeEmail
    val intentGoogle = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, addresses)
        putExtra(Intent.EXTRA_CC, cc)
        putExtra(Intent.EXTRA_BCC, bcc)
        putExtra(Intent.EXTRA_SUBJECT, subject ?: "")
        putExtra(Intent.EXTRA_TEXT, message ?: "")
    }

    if (intentGoogle.resolveActivity(packageManager) != null)
        startActivity(intentGoogle)
}

@ColorInt
fun Context.getAttrColor(@AttrRes attrID: Int): Int {
    val typedValue = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(attrID, typedValue, true)
    return typedValue.data
}

fun Context.getAttr(attrID: Int): Int {
    val typedValue = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(attrID, typedValue, true)
    return typedValue.data
}

fun Context.getDrawableCompat(res: Int): VectorDrawableCompat? {
    return VectorDrawableCompat.create(resources, res, theme)
}

fun Context.hasPermissions(vararg permission: String): Boolean {
    var result = true

    permission.forEach {
        val a = ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        result = result && a
    }

    return result
}


fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.getStatusBarHeightPixel(): Int = try {
    val res = resources.getIdentifier("status_bar_height", "dimen", "android")

    resources.getDimensionPixelSize(res)
} catch (e: Exception) {
    80
}

fun Context.getActionBarHeightPixel(): Int {
    val styledAttributes: TypedArray =
        theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
    val mActionBarSize = styledAttributes.getDimension(0, 157f).toInt()
    styledAttributes.recycle()

    return mActionBarSize
}

fun Context.copyToClipboard(text: String) {
    val myClipboard: ClipboardManager? =
        ContextCompat.getSystemService(this, ClipboardManager::class.java)

    val myClip = ClipData.newPlainText("copied:", text)
    myClipboard!!.setPrimaryClip(myClip)
    toast(getString(R.string.copy_to_clipboard))
}

fun Context.otherAppsMyket() {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("myket://developer/com.arash.altafi.documentbag")
        startActivity(intent)
    } catch (_: Exception) {
    }
}

fun Context.otherAppsCaffeBazaar() {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data =
            Uri.parse("bazaar://collection?slug=by_author&aid=" + "arashaltafi")
        intent.setPackage("com.farsitel.bazaar")
        startActivity(intent)
    } catch (_: Exception) {
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.addToHomeScreen(activity: Activity) {
    val shortcutManager = activity.getSystemService(ShortcutManager::class.java)

    if (shortcutManager.isRequestPinShortcutSupported) {
        val shortcutIntent = Intent(activity, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
        }
        val shortcutInfo = ShortcutInfo.Builder(this, "123")
            .setShortLabel(getString(R.string.app_name))
            .setIcon(Icon.createWithResource(this, R.drawable.icon))
            .setIntent(shortcutIntent)
            .build()

        shortcutManager.requestPinShortcut(shortcutInfo, null)
    }
}

fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}