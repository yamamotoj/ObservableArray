package com.github.yamamotoj.observablearray

import rx.subjects.PublishSubject
import java.util.*
import com.github.yamamotoj.observablearray.ObservableArray.Event

class ObservableArrayList<T>(
        private val arrayList: ArrayList<T>
) : ObservableArray<T> {

    private val updateSubject
            = PublishSubject.create<Event>()

    override val size: Int get() = arrayList.size
    override fun get(index: Int): T = arrayList[index]
    override fun updates() = updateSubject

    fun add(elem: T): Boolean {
        val index = size
        val ret = arrayList.add(elem)
        updateSubject.onNext(Event.ItemInserted(index))
        return ret
    }

    fun add(index: Int, elem: T) {
        arrayList.add(index, elem)
        updateSubject.onNext(Event.ItemInserted(index))
    }

    fun set(index: Int, elem: T): T {
        val ret = arrayList.set(index, elem)
        updateSubject.onNext(Event.ItemChanged(index))
        return ret
    }

    fun removeAt(index: Int): T {
        val ret = arrayList.removeAt(index)
        updateSubject.onNext(Event.ItemRemoved(index))
        return ret
    }

    fun remove(elem: T): Boolean {
        val index = arrayList.indexOf(elem)
        val ret = arrayList.remove(elem)
        updateSubject.onNext(Event.ItemRemoved(index))
        return ret
    }
}
