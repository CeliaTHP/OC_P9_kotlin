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

    private val dateMock = Mockito.mock(Date::class.java)

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(estateDaoMock)
    }

    @Test
    fun getByFiltersSuccess() {
        val expectedType = EstateType.BOAT

        val expectedEstateList = TestEstateList().getList().toMutableList()

        whenever(
            estateDaoMock.getWithFilters(
               any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()
            )
        ).thenReturn(Observable.just(expectedEstateList))

        viewModel.getWithFilters(
        any(), any(), any(), any(), any(), any(),
            any(), any(), any(), any(), any(), any(),
            any(), dateMock, any(), any(), any(), any(),
            any(), any(), any(), any(), any()
        )
            .test()
            .assertComplete()
         .assertValue { estateList -> estateList.all { it.type == expectedType } }

        verify(estateDaoMock).getWithFilters(
          any(), any(), any(), any(), any(), any(), any(),
            any(), any(), any(), any(), any(), any(),
            dateMock, any(), any(), any(), any(), any(),
            any(), any(), any(), any()
        )

    }


}