package com.example.kotlinfundamental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinfundamental.databinding.ActivityMainBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            btnObserve.setOnClickListener {
                /**
                 * "fromArray" converts an array (vararg in kotlin) into an ObservableSource that emits the items in the array (vararg in kotlin).
                 * It takes a vararg as its parameter
                 * */
                val observable = Observable.fromArray(1, 2,3, 4, 5, 6, 7, 8, 9, 10)
                val observer = object: Observer<Int> {
                    // it's called when observer is subscribe observable
                    override fun onSubscribe(d: Disposable?) {
                        Log.d("Reactive Programming", "OnSubscribe!")
                    }
                    // it's called when each items is emitted by observable
                    override fun onNext(t: Int?) {
                        Log.d("Reactive Programming", "onNext!")
                    }
                    // it's called when error is occurred
                    override fun onError(e: Throwable?) {
                        Log.d("Reactive Programming", "onError!")
                    }
                    // it's called when observable finishes emitting all items
                    override fun onComplete() {
                        Log.d("Reactive Programming", "onComplete!")
                    }
                }
                // observable is subscribed by observer
                observable.subscribe(observer)
            }
        }

        setContentView(binding.root)
    }

}