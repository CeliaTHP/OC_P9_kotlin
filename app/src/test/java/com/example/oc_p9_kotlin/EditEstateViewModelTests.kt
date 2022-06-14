package com.example.oc_p9_kotlin

import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.view_models.EditEstateViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Completable
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
class EditEstateViewModelTests {

    private val testNetworkSchedulers = TestMySchedulers()
    private val estateDaoMock = Mockito.mock(EstateDao::class.java)
    private val viewModel = EditEstateViewModel(estateDaoMock, testNetworkSchedulers)

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(estateDaoMock)
    }

    @Test
    fun insertEstateTestSuccess() {
         val expectedEstate = TestEstateList().getList()[0]
        whenever(estateDaoMock.insertEstate(any())).thenReturn(Completable.complete())

        viewModel.insertEstate(expectedEstate)
            .test()
            .assertComplete()

        verify(estateDaoMock).insertEstate(any())
    }

    @Test
    fun insertEstateTestError() {
        val expectedEstate = TestEstateList().getList()[0]
        val expectedException = Exception("testError")
        whenever(estateDaoMock.insertEstate(any())).thenReturn(Completable.error(expectedException))

        viewModel.insertEstate(expectedEstate)
            .test()
            .assertError(expectedException)

        verify(estateDaoMock).insertEstate(any())
    }


    @Test
    fun updateEstateTestSuccess() {
        val expectedEstate = TestEstateList().getList()[0]
        whenever(estateDaoMock.updateEstate(any())).thenReturn(Completable.complete())

        viewModel.updateEstate(expectedEstate)
            .test()
            .assertComplete()

        verify(estateDaoMock).updateEstate(any())
    }

    @Test
    fun updateEstateTestError() {
        val expectedEstate = TestEstateList().getList()[0]
        val expectedException = Exception("testError")
        whenever(estateDaoMock.updateEstate(any())).thenReturn(Completable.error(expectedException))

        viewModel.updateEstate(expectedEstate)
            .test()
            .assertError(expectedException)

        verify(estateDaoMock).updateEstate(any())
    }

}