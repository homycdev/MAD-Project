package ru.innohelpers.innohelp.component_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class ObservableObject<TValue> {

    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val mObserversMap = HashMap<Lifecycle, ArrayList<ValueChangedConfiguration<TValue>>>()
    private var _value: TValue? = null

    var value: TValue?
        get() {return _value }
        set(value) {
            if (value == _value) return
            val oldValue = _value
            _value = value
            mHandler.post { notifyAllListeners(oldValue, value) }
        }


    private fun notifyAllListeners(valueFrom: TValue?, valueTo: TValue?) {

        for (keyValuePair in mObserversMap) {
            val bestMatch = ArrayList<ValueChangedListener<TValue>>()
            val moderateMatch = ArrayList<ValueChangedListener<TValue>>()
            val worthMatch = ArrayList<ValueChangedListener<TValue>>()
            for(config in keyValuePair.value) {
                if (config.valueFromSet && config.valueToSet && config.valueFrom == valueFrom && config.valueTo == valueTo)
                    bestMatch.add(config.changedListener!!)
                else if (config.valueToSet && config.valueTo == valueTo || config.valueFromSet && config.valueFrom == valueTo)
                    moderateMatch.add(config.changedListener!!)
                else if (!config.valueToSet && !config.valueFromSet)
                    worthMatch.add(config.changedListener!!)
            }
            invokeListenerArray(bestMatch, valueFrom, valueTo)
            invokeListenerArray(moderateMatch, valueFrom, valueTo)
            invokeListenerArray(worthMatch, valueFrom, valueTo)
        }
    }

    private fun invokeListenerArray(listeners: ArrayList<ValueChangedListener<TValue>>, valueFrom: TValue?, valueTo: TValue?) {
        for (listener in listeners)
            listener.invoke(valueFrom, valueTo)
    }

    private fun observe(lifecycleOwner: LifecycleOwner, listenConfiguration: ValueChangedConfiguration<TValue>) {

        val lifecycleObserver = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun removeFromMap() {
                mObserversMap.remove(lifecycleOwner.lifecycle)
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        if (!mObserversMap.containsKey(lifecycleOwner.lifecycle)) mObserversMap[lifecycleOwner.lifecycle] = ArrayList()
        mObserversMap[lifecycleOwner.lifecycle]!!.add(listenConfiguration)

        if (listenConfiguration.valueToSet && listenConfiguration.valueTo == value || !listenConfiguration.valueToSet)
            mHandler.post { listenConfiguration.changedListener!!.invoke(null, value) }
    }

    fun observe(lifecycleOwner: LifecycleOwner, valueTo: TValue?, observer: ValueChangedListener<TValue>) {
        val configuration = ValueChangedConfiguration<TValue>()
        configuration.changedListener = observer
        configuration.valueTo = valueTo
        observe(lifecycleOwner, configuration)
    }

    fun observe(lifecycleOwner: LifecycleOwner, observer: ValueChangedListener<TValue>) {
        val configuration = ValueChangedConfiguration<TValue>()
        configuration.changedListener = observer
        observe(lifecycleOwner, configuration)
    }

    fun observe(lifecycleOwner: LifecycleOwner, valueFrom: TValue?, valueTo:TValue?, observer: ValueChangedListener<TValue>) {
        val configuration = ValueChangedConfiguration<TValue>()
        configuration.changedListener = observer
        configuration.valueFrom = valueFrom
        configuration.valueTo = valueTo

        observe(lifecycleOwner, configuration)
    }

    fun removeObserver(lifecycleOwner: LifecycleOwner) {
        mObserversMap.remove(lifecycleOwner.lifecycle)
    }

}