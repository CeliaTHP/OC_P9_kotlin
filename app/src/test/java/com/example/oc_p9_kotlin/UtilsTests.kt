package com.example.oc_p9_kotlin

import com.example.oc_p9_kotlin.utils.Utils
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.Date


@RunWith(MockitoJUnitRunner::class)
class UtilsTests {


    @Test
    fun fromDollarToEuroTestSuccess() {

        val priceInDollars = 5000
        val exchangeRate = 0.94
        val expectedValue = 4700

        assert((priceInDollars * exchangeRate).toInt() == expectedValue)
        assert(Utils.convertDollarToEuro(priceInDollars) == expectedValue)

    }

    @Test
    fun fromEuroToDollarTestSuccess() {

        val priceInEuros = 5000
        val exchangeRate = 1.06
        val expectedValue = 5300

        assert((priceInEuros * exchangeRate).toInt() == expectedValue)
        assert(Utils.convertEuroToDollars(priceInEuros) == expectedValue)

    }

    @Test
    fun dateConverterDayMonthYear() {

        val expectedDate = SimpleDateFormat("dd/MM/yyyy").format(Date())

        assert(expectedDate.equals(Utils.getFormattedDate(Date(), true)))
    }

    @Test
    fun dateConverterYearMonthDay() {

        val expectedDate = SimpleDateFormat("yyyy/MM/dd").format(Date())

        assert(expectedDate.equals(Utils.getFormattedDate(Date(), false)))
    }



}