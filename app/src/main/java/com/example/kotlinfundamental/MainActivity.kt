package com.example.kotlinfundamental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinfundamental.databinding.ActivityMainBinding
import io.reactivex.rxjava3.core.Observable

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            btnObserve.setOnClickListener {
                // "just" is an operator that converts its parameters into an observable and emit it
                Observable.just("Hello RxJava and RxAndroid").subscribe {
                    Log.d("Reactive Programming", "Result: ${it}")
                }
            }
        }

        setContentView(binding.root)
    }
}