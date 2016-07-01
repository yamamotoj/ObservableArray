package com.github.yamamotoj.observablearray

import com.github.yamamotoj.observablearray.ObservableArray.Event
import rx.Observable

interface ObservableArray<T> {

    val size: Int
    operator fun get(index: Int): T
    fun updates(): Observable<Event>

    sealed class Event {
        class DataSetChanged : Event()
        class ItemChanged(val position: Int) : Event()
        class ItemInserted(val position: Int) : Event()
        class ItemRemoved(val position: Int) : Event()
    }
}

fun <T, R> ObservableArray<T>.map(transform: (T) -> R): ObservableArray<R> = object : ObservableArray<R> {
    override val size: Int get() = this@map.size
    override fun get(index: Int): R = transform(this@map[index])
    override fun updates(): Observable<ObservableArray.Event> = this@map.updates()
}

operator fun <T> ObservableArray<T>.plus(other: ObservableArray<T>)
        : ObservableArray<T> = object : ObservableArray<T> {

    override val size: Int get() = this@plus.size + other.size
    override fun get(index: Int): T =
            if (index < this@plus.size) {
                this@plus[index]
            } else {
                other[index - this@plus.size]
            }

    override fun updates(): Observable<Event> = Observable.merge(
            this@plus.updates(),
            other.updates().map {
                val offset = this@plus.size
                when (it) {
                    is Event.DataSetChanged -> it
                    is Event.ItemChanged -> Event.ItemChanged(it.position + offset)
                    is Event.ItemInserted -> Event.ItemInserted(it.position + offset)
                    is Event.ItemRemoved -> Event.ItemRemoved(it.position + offset)
                }
            }
    )
}
