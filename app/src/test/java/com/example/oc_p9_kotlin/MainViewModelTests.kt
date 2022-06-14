package com.example.oc_p9_kotlin

import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.models.EstateType
import com.example.oc_p9_kotlin.view_models.MainViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MainViewModelTests {

    private val testNetworkSchedulers = TestMySchedulers()
    private val estateDaoMock = Mockito.mock(EstateDao::class.java)
    private val viewModel = MainViewModel(estateDaoMock, testNetworkSchedulers)

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(estateDaoMock)
    }

    @Test
    fun generateDataTestSuccess() {

        whenever(estateDaoMock.insertAllEstates(any())).thenReturn(Completable.complete())

        viewModel.generateData()
            .test()
            .assertComplete()

        verify(estateDaoMock).insertAllEstates(any())
    }

    @Test
    fun generateDataTestError() {

        val expectedException = Exception("testError")
        whenever(estateDaoMock.insertAllEstates(any()))
            .thenReturn(Completable.error(expectedException))

        viewModel.generateData()
            .test()
            .assertError(expectedException)

        verify(estateDaoMock).insertAllEstates(any())
    }


    @Test
    fun getAllTestSuccess() {

        val expectedEstateList = TestEstateList().getList().toMutableList()
        whenever(estateDaoMock.getAll()).thenReturn(Observable.just(expectedEstateList))

        viewModel.getAll()
            .test()
            .assertComplete()
            .assertValueCount(1)
            .assertValue(expectedEstateList)

        verify(estateDaoMock).getAll()
    }

    @Test
    fun getAllTestError() {

        val expectedException = Exception("testError")
        whenever(estateDaoMock.getAll()).thenReturn(Observable.error(expectedException))

        viewModel.getAll()
            .test()
            .assertError(expectedException)

        verify(estateDaoMock).getAll()
    }

    @Test
    fun getByTypeSuccess() {

        val expectedType = EstateType.BOAT
        val expectedEstateList = TestEstateList().getList().toMutableList()
        whenever(estateDaoMock.getByType(any())).thenReturn(Observable.just(expectedEstateList))

        viewModel.getByType(expectedType)
            .test()
            .assertComplete()
            .assertValue { estateList -> estateList.all { it.type == expectedType } }

        verify(estateDaoMock).getByType(any())
    }

    @Test

    fun getByTypeError() {

        val expectedType = EstateType.BOAT
        val expectedException = Exception("testError")
        whenever(estateDaoMock.getByType(any())).thenReturn(Observable.error(expectedException))

        viewModel.getByType(expectedType)
            .test()
            .assertError(expectedException)

        verify(estateDaoMock).getByType(any())
    }


}