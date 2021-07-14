package com.example.kotlinfundamental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinfundamental.databinding.ActivityMainBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observables.ConnectableObservable
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            btnObserve.setOnClickListener {
                /** --------------------------------- Start | Cold Observable --------------------------------- **/
                /**
                 * "Observable" starts its emission right after observer subscribe on it
                 * Cold Observable creates a data producer for each subscriber. In the sense Cold Observable only emits an item/event when it has a observer
                 * That's why the value received by each observer is different because Cold Observable will always generate a random value for every observer
                 * */
                val cold : Observable<Long> = Observable.create<Long> {
                    it.onNext(Random.nextLong(100))
                    it.onComplete()
                }

                cold.subscribe {
                    Log.d("Reactive Programming", "Cold Observer#1 - onNext!: ${it}")
                }

                cold.subscribe {
                    Log.d("Reactive Programming", "Cold Observer#2 - onNext!: ${it}")
                }
                /** --------------------------------- End | Cold Observable --------------------------------- **/


                /** --------------------------------- Start | Hot Observable --------------------------------- **/
                /**
                 * ConnectableObservable start its emission the moment wa call "connect()"
                 * Hot Observable creates a data producer first, then each subscriber gets the data from one producer. It produces data no matter if an observer listens or not
                 * That's why the value received by Observer#1 and Observer#2 are same but we don't see any output from Observer#3 because Observer#3 was listening after "hot.connect()"
                 * Hot Observable doesn't care about its observer. When it doesn't have observer then the data has been emitted will be lost or an observer is late to listens then that observer will never get the data
                 * Like a radio station, if we're late listening to an event then we'll miss it
                 * */
                val hot : ConnectableObservable<Long> = Observable.create<Long> {
                    it.onNext(Random.nextLong(100))
                    it.onComplete()
                }.publish()

                hot.subscribe {
                    Log.d("Reactive Programming", "Hot Observer#1 - onNext!: ${it}")
                }

                hot.subscribe {
                    Log.d("Reactive Programming", "Hot Observer#2 - onNext!: ${it}")
                }

                hot.connect()

                hot.subscribe {
                    Log.d("Reactive Programming", "Hot Observer#3 - onNext!: ${it}")
                }

                /** --------------------------------- End | Hot Observable --------------------------------- **/
            }
        }

        setContentView(binding.root)
    }

}