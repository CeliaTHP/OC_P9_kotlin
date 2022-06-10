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
import androidx.core.content.res.ResourcesCompat
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.POIType
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Utils {

    const val TAG: String = "Utils_"

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        //was 0.812
        return Math.round(dollars * 0.94).toInt()
    }

    fun convertEuroToDollars(euros: Int): Int {
        //was 0.812
        return Math.round(euros * 1.06).toInt()
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    fun getFormattedDate(date: Date?, isDayMonthYear: Boolean? = null): String? {
        if (date == null)
            return null
        if (isDayMonthYear != null) {
            val dateFormat: DateFormat =
                if (isDayMonthYear) {
                    SimpleDateFormat("dd/MM/yyyy")
                } else {
                    SimpleDateFormat("yyyy/MM/dd")
                }
            return dateFormat.format(date)

        } else {
            val dateFormat: DateFormat =
                if (Locale.getDefault().language == Locale.FRENCH.language) {
                    SimpleDateFormat("dd/MM/yyyy")
                } else {
                    SimpleDateFormat("yyyy/MM/dd")
                }
            return dateFormat.format(date)
        }
    }

    fun getPrice(estate: Estate): String {
        val language = Locale.getDefault().language
        var price: Int = estate.priceInEuros
        val formatter: NumberFormat = DecimalFormat("#,###")
        var currency = "€"

        if (language != Locale.FRENCH.language) {
            currency = "$"
            price = convertEuroToDollars(price)
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

    fun initSlider(slider: RangeSlider, symbol: String? = null) {

        slider.setLabelFormatter(object : LabelFormatter {
            override fun getFormattedValue(value: Float): String {
                val formatter: NumberFormat = DecimalFormat("#,###")
                var finalValue = formatter.format(value)

                symbol?.let {
                    finalValue = "$finalValue $symbol"
                }
                return finalValue
            }
        })
    }

    fun getIconForPoi(poiType: POIType, context: Context?): Drawable? {

        if (context == null)
            return null

        return when (poiType) {
            POIType.STATION -> ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.ic_train,
                null
            )
            POIType.PUB -> ResourcesCompat.getDrawable(context.resources, R.drawable.ic_bar, null)
            POIType.HOSTEL -> ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.ic_hostel,
                null
            )
            POIType.HOSPITAL -> ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.ic_hospital,
                null
            )
            POIType.SCHOOL -> ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.ic_school,
                null
            )
            POIType.PARK -> ResourcesCompat.getDrawable(context.resources, R.drawable.ic_park, null)
            POIType.RESTAURANT -> ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.ic_restaurant,
                null
            )
            POIType.OTHER -> ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.ic_question_mark,
                null
            )
        }
    }

}