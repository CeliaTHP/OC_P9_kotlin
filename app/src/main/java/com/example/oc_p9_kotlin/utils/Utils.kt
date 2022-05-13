package com.example.oc_p9_kotlin.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.MediaStore.Images
import android.util.Log
import com.example.oc_p9_kotlin.models.Estate
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.util.GeoPoint
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Utils {
    companion object {
        const val TAG: String = "Utils_"
    }


    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        //was 0.812
        return Math.round(dollars * 0.91945).toInt()
    }

    fun convertEuroToDollars(euros: Int): Int {
        //was 0.812
        return Math.round(euros / 0.91945).toInt()
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    fun getTodayDate(customFormat: CustomFormat): String? {
        val dateFormat: DateFormat = if (customFormat == CustomFormat.DAY_MONTH_YEAR) {
            SimpleDateFormat("dd/MM/yyyy")
        } else {
            SimpleDateFormat("yyyy/MM/dd")
        }
        return dateFormat.format(Date())
    }

    enum class CustomFormat {
        YEAR_MONTH_DAY,
        DAY_MONTH_YEAR
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */

    //was isInternetAvailable
    fun isWifiAvailable(context: Context): Boolean? {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }


    fun getPrice(estate: Estate): String {

        val language = Locale.getDefault().language

        var price: Int = estate.priceInEuros
        val formatter: NumberFormat = DecimalFormat("#,###")

        var currency = "€"

        if (language != Locale.FRENCH.language) {
            currency = "$"
            price = convertEuroToDollars(price)
            Log.d(TAG, "was " + estate.priceInEuros + " is in Dollars : " + price)
        }

        val formattedNumber: String = formatter.format(price)


        return "$formattedNumber $currency"

    }




    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


    fun getRealPathFromURI(contentUri: Uri?, context: Context): String? {

        if (contentUri == null)
            return null
        val proj = arrayOf(Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(contentUri, proj, null, null, null)
        val column_index = cursor?.getColumnIndexOrThrow(Images.Media.DATA)
        cursor?.moveToFirst()
        return column_index?.let { cursor?.getString(it) }
    }

    fun initSlider(slider: RangeSlider, symbol: String? = null) {

        slider.setLabelFormatter(object : LabelFormatter {
            override fun getFormattedValue(value: Float): String {
                val formatter: NumberFormat = DecimalFormat("#,###")
                var finalValue = formatter.format(value)

                // if (value == slider.valueTo)
                //   finalValue = "+ $finalValue"
                symbol?.let {
                    finalValue = "$finalValue $symbol"
                }

                return finalValue
            }

        })


    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

//Verify if internet is enabled on the device
/*

    fun isInternetAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true
                    }
                }
            } else {
                try {
                    val activeNetworkInfo = connectivityManager.activeNetworkInfo
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                        Log.i("update_statut", "Network is available : true")
                        return true
                    }
                } catch (e: Exception) {
                    Log.i("update_statut", "" + e.message)
                }
            }
        }
        Log.i("update_statut", "Network is available : FALSE ")
        return false
    }


 */


/*
fun isInternetAvailable(context: Context):Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val nw = connectivityManager.activeNetwork ?: return false

    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}

 */


}