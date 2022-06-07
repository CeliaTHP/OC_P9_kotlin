package com.example.oc_p9_kotlin.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

interface MainSchedulerProvider {
    val main: Scheduler
        get() = AndroidSchedulers.mainThread()
}

interface IOSchedulerProvider {
    val io: Scheduler
        get() = Schedulers.io()
}

open class MySchedulers : MainSchedulerProvider, IOSchedulerProvider
