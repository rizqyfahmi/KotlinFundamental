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

        /*------------------------------ Start | Init data ------------------------------*/
        val tasks = mutableListOf<Task?>()
        tasks.add(Task("Take out the trash", true,3))
        tasks.add(Task("Walk the dog", false, 2 ))
        tasks.add(Task("Make my bed", true, 1 ))
        tasks.add(null)
        tasks.add(Task("Unload the dishwasher", false, 4 ))
        tasks.add(Task("Make dinner", true, 5 ))
        /*------------------------------ End | Init data ------------------------------*/

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            btnObserve.setOnClickListener {
                // Create an observable of Task
                Observable.create<Task> {
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
                }.subscribe({
                    // Execute until all task is emmited or error is occured
                    Log.d("Reactive Programming","onNext: \n Task: \n\t- description: ${it.description} \n\t- isCompleted: ${it.isComplete} \n\t- priority: ${it.priority}")
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
}