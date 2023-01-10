@file:Suppress("unused")

package fr.delcey.todok.ui.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T1, T2, R> combine(
    liveData1: LiveData<T1>,
    liveData2: LiveData<T2>,
    transform: LiveDataValueProducer<R>.(T1?, T2?) -> Unit,
): LiveData<R> = MediatorLiveData<R>().apply {
    val producer = object : LiveDataValueProducer<R> {
        override fun emit(value: R) {
            this@apply.value = value
        }
    }

    addSource(liveData1) {
        transform(
            producer,
            liveData1.value,
            liveData2.value
        )
    }
    addSource(liveData2) {
        transform(
            producer,
            liveData1.value,
            liveData2.value
        )
    }
}

fun <T1, T2, T3, R> combine(
    liveData1: LiveData<T1>,
    liveData2: LiveData<T2>,
    liveData3: LiveData<T3>,
    transform: LiveDataValueProducer<R>.(T1?, T2?, T3?) -> Unit,
): LiveData<R> = MediatorLiveData<R>().apply {
    val producer = object : LiveDataValueProducer<R> {
        override fun emit(value: R) {
            this@apply.value = value
        }
    }

    addSource(liveData1) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
        )
    }
    addSource(liveData2) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
        )
    }
    addSource(liveData3) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
        )
    }
}

fun <T1, T2, T3, T4, R> combine(
    liveData1: LiveData<T1>,
    liveData2: LiveData<T2>,
    liveData3: LiveData<T3>,
    liveData4: LiveData<T4>,
    transform: LiveDataValueProducer<R>.(T1?, T2?, T3?, T4?) -> Unit,
): LiveData<R> = MediatorLiveData<R>().apply {
    val producer = object : LiveDataValueProducer<R> {
        override fun emit(value: R) {
            this@apply.value = value
        }
    }

    addSource(liveData1) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
            liveData4.value,
        )
    }
    addSource(liveData2) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
            liveData4.value,
        )
    }
    addSource(liveData3) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
            liveData4.value,
        )
    }
    addSource(liveData4) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
            liveData4.value,
        )
    }
}

fun <T1, T2, T3, T4, T5, R> combine(
    liveData1: LiveData<T1>,
    liveData2: LiveData<T2>,
    liveData3: LiveData<T3>,
    liveData4: LiveData<T4>,
    liveData5: LiveData<T5>,
    transform: LiveDataValueProducer<R>.(T1?, T2?, T3?, T4?, T5?) -> Unit,
): LiveData<R> = MediatorLiveData<R>().apply {
    val producer = object : LiveDataValueProducer<R> {
        override fun emit(value: R) {
            this@apply.value = value
        }
    }

    addSource(liveData1) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
            liveData4.value,
            liveData5.value,
        )
    }
    addSource(liveData2) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
            liveData4.value,
            liveData5.value,
        )
    }
    addSource(liveData3) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
            liveData4.value,
            liveData5.value,
        )
    }
    addSource(liveData4) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
            liveData4.value,
            liveData5.value,
        )
    }
    addSource(liveData5) {
        transform(
            producer,
            liveData1.value,
            liveData2.value,
            liveData3.value,
            liveData4.value,
            liveData5.value,
        )
    }
}

interface LiveDataValueProducer<T> {
    fun emit(value: T)
}