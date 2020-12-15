package ru.innohelpers.innohelp.realm.storage

import android.os.Handler
import io.realm.Realm
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class RealmStorage(private val handler: Handler, protected val realm: Realm) {

    private class NotifyRunnable(private val runnable: Runnable) : Runnable {

        var finished: Boolean = false

        override fun run() {
            lock.withLock {
                println("invoked: " + Thread.currentThread().id)
                runnable.run()
                finished = true
                condition.signalAll()
            }
        }
    }

    protected fun invoke(runnable: Runnable) {
        lock.withLock {
            val nRunnable = NotifyRunnable(runnable)
            handler.post(nRunnable)
            while (!nRunnable.finished) {
                try {
                    condition.await()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private companion object {
        val lock = ReentrantLock()
        val condition: Condition = lock.newCondition()
    }

}