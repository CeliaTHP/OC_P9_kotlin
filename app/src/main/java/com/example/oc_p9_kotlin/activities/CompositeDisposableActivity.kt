package com.example.oc_p9_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class CompositeDisposableActivity : AppCompatActivity() {
    val bag = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}