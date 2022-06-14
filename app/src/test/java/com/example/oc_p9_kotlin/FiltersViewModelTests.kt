package com.example.oc_p9_kotlin

import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.view_models.FiltersViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Observable
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class FiltersViewModelTests {

    private val testNetworkSchedulers = TestMySchedulers()
    private val estateDaoMock = Mockito.mock(EstateDao::class.java)
    private val viewModel = FiltersViewModel(estateDaoMock, testNetworkSchedulers)

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(estateDaoMock)
    }

    @Test
    fun getByFiltersSuccess() {

        val expectedType = EstateType.BOAT
        val expectedEstateList = TestEstateList().getList(expectedType).toMutableList()
        val date = Date()
        val expectedCity = "testCity"
        val expectedMin = 0
        val expectedMax = 10000

        whenever(
            estateDaoMock.getWithFilters(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any()
            )
        ).thenReturn(Observable.just(expectedEstateList))

        viewModel.getWithFilters(
            expectedType.name, expectedMin, expectedMax, expectedMin, expectedMax, expectedMin,
            expectedMax, expectedMin, expectedMax, expectedMin, expectedMax, expectedMin,
            expectedMax, expectedCity,date, date, false, false, false,
            false, false, false, false, false
        )
            .test()
            .assertComplete()
            .assertValue { estateList -> estateList.all { it.type == expectedType } }
            .assertValue { estateList -> estateList.all { it.city == expectedCity } }


        verify(estateDaoMock).getWithFilters(
            eq(expectedType.name), eq(expectedMin), eq(expectedMax),  eq(expectedMin), eq(expectedMax),
            eq(expectedMin), eq(expectedMax), eq(expectedMin), eq(expectedMax),
            eq(expectedMin), eq(expectedMax), eq(expectedMin), eq(expectedMax), eq(expectedCity),
            eq(date), eq(date), eq(false), eq(false), eq(false), eq(false),
            eq(false),eq(false),eq(false),eq(false)
        )

    }

    @Test
    fun getByFiltersError() {

        val expectedType = EstateType.BOAT
        val date = Date()
        val expectedCity = "testCity"
        val expectedMin = 0
        val expectedMax = 10000
        val expectedException = Exception("testError")

        whenever(
            estateDaoMock.getWithFilters(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any()
            )
        ).thenReturn(Observable.error(expectedException))

        viewModel.getWithFilters(
            expectedType.name, expectedMin, expectedMax, expectedMin, expectedMax, expectedMin,
            expectedMax, expectedMin, expectedMax, expectedMin, expectedMax, expectedMin,
            expectedMax,expectedCity, date, date, false, false, false,
            false, false, false, false, false
        )
            .test()
            .assertError(expectedException)

        verify(estateDaoMock).getWithFilters(
            eq(expectedType.name), eq(expectedMin), eq(expectedMax),  eq(expectedMin), eq(expectedMax),
            eq(expectedMin), eq(expectedMax), eq(expectedMin), eq(expectedMax),
            eq(expectedMin), eq(expectedMax), eq(expectedMin), eq(expectedMax),
            eq(expectedCity), eq(date), eq(date), eq(false), eq(false), eq(false), eq(false),
            eq(false),eq(false),eq(false),eq(false)

        )

    }


}