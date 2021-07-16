package com.example.kotlinfundamental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinfundamental.databinding.ActivityMainBinding
import io.reactivex.rxjava3.core.*
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
                /** --------------------------------- Start | Single --------------------------------- **/
                /**
                 * Single is an Observable which only emits one item or throws an error.
                 * Single isn't like an Observable we wrote before. The only noticeable difference is the observer definition where "onNext()" and "onComplete()" of Observable has been combined to "onSuccess()", as the stream has only one single item to emit.
                 * Single is like promise in Javascript. A promise is an object that may produce a item or throw an error.
                 * Typically example is a network call, using retrofit you return an Observable or Flowable. You usually care for the response once you can replace this with Single<T>
                 **/
                val single = Single.just(1)
                val singleObserver = object: SingleObserver<Int> {
                    override fun onSubscribe(d: Disposable?) {
                        Log.d("Reactive Programming", "Single onSubscribe!")
                    }

                    override fun onSuccess(t: Int?) {
                        Log.d("Reactive Programming", "Single onSuccess!: ${t.toString()}")
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("Reactive Programming", "Single onError!: ${e?.message}")
                    }
                }

                single.subscribe(singleObserver)
                /** --------------------------------- End | Single --------------------------------- **/

                /** --------------------------------- Start | Maybe --------------------------------- **/
                /**
                 * Maybe is similar to Single but it allows your observable the ability to not emit any item at all and it has additional method called "onComplete()"
                 * "onSuccess()" will be called when Maybe emits an item
                 * "onComplete()" will be called when Maybe doesn't emit any item at all
                 **/
                val maybe = Maybe.just(2)
                // val maybe = Maybe.empty<Int>()
                val maybeObserver = object: MaybeObserver<Int> {
                    override fun onSubscribe(d: Disposable?) {
                        Log.d("Reactive Programming", "Maybe onSubscribe!")
                    }

                    override fun onSuccess(t: Int?) {
                        Log.d("Reactive Programming", "Maybe onSuccess! ${t.toString()}")
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("Reactive Programming", "Maybe onError! ${e?.message}")
                    }

                    override fun onComplete() {
                        Log.d("Reactive Programming", "Maybe onComplete!")
                    }
                }

                maybe.subscribe(maybeObserver)
                /** --------------------------------- End | Maybe --------------------------------- **/

                /** --------------------------------- Start | Completable --------------------------------- **/
                /**
                 * Completable is an Observable that only concerned about two things, if some action is executed ("onComplete()") or an error is encountered ("onError()")
                 * Completable is useful for cases wherein you are only interested if something has executed properly, ignoring output or whatever.
                 * Completable can be converted to an observable using toObservable. However, the reverse is also not straightforward. You can convert to a Single first
                 * For example: There are certain scenario where only concern in completion or error. Suppose we update any User model in the app and want to just notify the server about it. We don’t care about the response because app already has the latest object
                 **/
                val completable = Completable.fromSingle(Single.just(1))
                val completableObserver = object: CompletableObserver {
                    override fun onSubscribe(d: Disposable?) {
                        Log.d("Reactive Programming", "Completable onSubscribe!")
                    }

                    override fun onComplete() {
                        Log.d("Reactive Programming", "Completable onComplete!")
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("Reactive Programming", "Completable onError! ${e?.message}")
                    }

                }

                completable.subscribe(completableObserver)
                /** --------------------------------- End | Completable --------------------------------- **/
            }
        }

        setContentView(binding.root)
    }

}