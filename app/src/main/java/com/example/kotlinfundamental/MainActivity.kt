package com.example.kotlinfundamental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinfundamental.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*------------------------------ Start | Init data ------------------------------*/
        val tasks = mutableListOf<Task?>()
        tasks.add(Task("Take out the trash", true,3))
        tasks.add(Task("Walk the dog", false, 2 ))
        tasks.add(Task("Make my bed", true, 1 ))
        tasks.add(Task("Unload the dishwasher", false, 4 ))
        tasks.add(Task("Make dinner", true, 5 ))
        /*------------------------------ End | Init data ------------------------------*/

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            btnObserve.setOnClickListener {
                // Create an observable of Task
                Observable
                // "create" is executed on IO Thread because of "subscribeOn(Schedulers.io())"
                .create<Task> {
                    Log.d("Reactive Programming", "create Thread: ${Thread.currentThread().name}")
                    tasks.forEach { task ->
                        if (task === null) {
                            // Throw an exception when task is null
                            it.onError(Exception("Something when wrong"))
                            return@forEach
                        }
                        // Convert Task into an observable
                        it.onNext(task)
                    }
                    // Notify that the observable has finished sending items
                    it.onComplete()
                }
                /**
                 * - "subscribeOn" specifies the thread on which the observable will be subscribed
                 * - It will stay on it downstream (The thread will be changed when we change thread by using "observeOn")
                 * - If there are multiple instances of subscribeOn in the stream, only the first one has a practical effect
                 * */
                .subscribeOn(Schedulers.io())
                // "map" is executed on IO Thread because of "subscribeOn(Schedulers.io())"
                .map {
                    Log.d("Reactive Programming", "map #1 Thread: ${Thread.currentThread().name}")
                    // "Map" is an operator used to modify each item emitted by an Observable and it returns modified item as an anything (object, value, etc.)
                    it.isComplete = false
                    it
                }
                /**
                 * "observeOn" specifies the thread on which the next operators in the chain (Downstream) will be executed
                 * */
                .observeOn(AndroidSchedulers.mainThread())
                // "map" is executed on Main Thread because of observeOn(AndroidSchedulers.mainThread())
                .map {
                    Log.d("Reactive Programming", "map #2 Thread: ${Thread.currentThread().name}")
                    // "Map" is an operator used to modify each item emitted by an Observable and it returns modified item as an anything (object, value, etc.)
                    it.isComplete = true
                    it
                }
                // "flatMap" is executed on Main Thread because of observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    Log.d("Reactive Programming", "flatMap Thread: ${Thread.currentThread().name}")
                    // "Map" is an operator used to modify each item emitted by an Observable and it returns modified item as an anything (object, value, etc.)
                    it.isComplete = true
                    // Let's assume we want to make another network request and we don't want to make it on Main Thread (Avoid UI Freeze) then we do it on IO thread inside "getLocationObservable"
                    getLocationObservable(it)
                }
                // Change the thread (Back to Main Thread)
                .observeOn(AndroidSchedulers.mainThread())
                // "subscribe" is executed on Main Thread because of observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("Reactive Programming", "subscribe Thread: ${Thread.currentThread().name}")
                    // Execute until all task is emmited or error is occured
                    Log.d("Reactive Programming","onNext: \n Task: \n\t- description: ${it.description} \n\t- isCompleted: ${it.isComplete} \n\t- priority: ${it.priority} \n\t- location: ${it.location?.country}")
                }, {
                    // Execute when error is occured
                    Log.d("Reactive Programming", "onError: ${it.message}")
                }, {
                    // Execute when all task is emmited (It will never executed when when error is occured)
                    Log.d("Reactive Programming","onComplete: All is done!")
                })
            }
        }

        setContentView(binding.root)
    }

    private fun getLocationObservable(task: Task): Observable<Task> {
        return Observable
        // "create" is executed on IO Thread because of "subscribeOn(Schedulers.io())" below
        .create<Task> { emitter ->
            Log.d("Reactive Programming", "create location Thread: ${Thread.currentThread().name}")
            if (!emitter.isDisposed) {
                val location = Location("Home", "Jakarta", "Indonesia")
                task.location = location

                val delay = Random.nextLong(1000) + 500
                Thread.sleep(delay)
                emitter.onNext(task)
                emitter.onComplete()
            }
        }.subscribeOn(Schedulers.io())
    }
}