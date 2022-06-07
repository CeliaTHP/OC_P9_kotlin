package com.example.oc_p9_kotlin

import com.example.oc_p9_kotlin.utils.MySchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers


class TestMySchedulers : MySchedulers() {
    override val io: Scheduler
        get() = Schedulers.trampoline()
    override val main: Scheduler
        get() = Schedulers.trampoline()
}