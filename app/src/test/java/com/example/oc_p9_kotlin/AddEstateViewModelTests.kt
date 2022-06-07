package com.example.oc_p9_kotlin

import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.view_models.AddEstateViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Completable
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class AddEstateViewModelTests {

    private val testNetworkSchedulers = TestMySchedulers()
    private val estateDaoMock = Mockito.mock(EstateDao::class.java)
    private val viewModel = AddEstateViewModel(estateDaoMock, testNetworkSchedulers)

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(estateDaoMock)
    }

    @Test
    fun addEstateSuccess() {

         val expectedEstate = TestEstateList().getList()[0]


    }


}