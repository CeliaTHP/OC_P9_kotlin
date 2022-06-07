package com.example.oc_p9_kotlin

import com.example.oc_p9_kotlin.daos.EstateDao
import com.example.oc_p9_kotlin.view_models.EditEstateViewModel
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

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
    fun addEstateSuccess() {

         val expectedEstate = TestEstateList().getList()[0]


    }


}