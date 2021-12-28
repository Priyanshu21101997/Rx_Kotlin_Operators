package com.example.rx_kotlin_operators

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.fromArray
import io.reactivex.rxjava3.core.Observable.just
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.functions.Predicate
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    private lateinit var mObservable: Observable<Student>
    private lateinit var mDisposableObserver: DisposableObserver<Student>
    private lateinit var textView:TextView

    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private  val TAG = "MainActivity"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById<TextView>(R.id.textView)

        // 1. Just Operator

        val greeting:String = "Hello from greeting"
//        mObservable = Observable.just(greeting)

        // 2. fromArray Operator

//        val greetings = arrayOf("Hello World","Bye World")
//        mObservable = Observable.fromArray(greetings)

       // 3. Range  -> To get a range

//        val greetings = arrayOf(1,2,3,4,5,6,7,8)
//        mObservable = Observable.range(1,20)

        // 4. Create operator -> Create observable from scratch. WIth create method we can have a function
        // body so we have control over our data. We can decide what data to emit
        // I have created student class for this method


        mObservable = Observable.create { emitter ->

            val studentList:ArrayList<Student> = getStudents()
            for(student in studentList){
                emitter.onNext(student)
            }

            emitter.onComplete()
        }

        //5. Map operator

        getObserver()

//        mCompositeDisposable.add(
//            mObservable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map {
//                   it.name =  it.name.uppercase()
//                    return@map it
//                }
//                .subscribeWith(mDisposableObserver)
//        )

        //6. Flatmap operator -> Gives a observable

//        mCompositeDisposable.add(
//            mObservable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap {
//
//                    val student1 = it
//                    student1.name = it.name
//
//                    val student2 = it
//                    student2.name = it.name + "New Name"
//
//                    return@flatMap Observable.just(it,student1,student2)
//                }
//                .subscribeWith(mDisposableObserver)
//        )

        // 7. Concat map same as flatmap but it preserves the order of the emission. It waits for
        //previous emissions to finish so as to maintain the order hence it is not the most
        //efficient solution

//        Order is important use -> ConcatMap
//        Speed is important use -> Flatmap

        // 8. Buffer operator ->periodically gathers elements emitted by an observable into bundle
        // and emit these bundles rather than emitting items one at a time .


//        mCompositeDisposable.add(
//            Observable.range(1,20)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .buffer(4)
//                .subscribeWith(object: DisposableObserver<List<Int>>() {
//                    override fun onNext(t: List<Int>) {
//                        Log.d(TAG, "onNext: Came to on next")
//                        for (value in t) {
//                            Log.d(TAG, "int value is $value")
//                        }
//                    }
//
//                    override fun onError(e: Throwable) {
//                    }
//
//                    override fun onComplete() {
//                        Log.d(TAG, "onComplete: Came to oncomplete ")
//                    }
//                }))

        // 9. Filter operator

        Observable.range(1,20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            .filter(Predicate {
                it -> it%2 == 0
            })
            .subscribe(object : Observer<Int> {
                override fun onSubscribe(d: Disposable) {
                }
                override fun onNext(t: Int) {
                    Log.d(TAG, "onNext: $t")
                }
                override fun onError(e: Throwable) {
                }
                override fun onComplete() {
                }
            })
    }

    private fun getObserver():DisposableObserver<Student>{
        mDisposableObserver = object : DisposableObserver<Student>() {
            override fun onNext(t: Student) {
                textView.text = t.toString()
                Log.d("TAG", "onNext: ${t.name}")
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }

        }

        return mDisposableObserver
    }

    private fun getStudents():ArrayList<Student>{
        val list = ArrayList<Student>()
        val s1 = Student("Student1","student1@gmail.com",21,"20")
        val s2 = Student("Student2","student2@gmail.com",22,"22")
        val s3 = Student("Student3","student3@gmail.com",23,"23")
        val s4 = Student("Student4","student4@gmail.com",24,"24")
        list.add(s1)
        list.add(s2)
        list.add(s3)
        list.add(s4)

        return list

    }
}