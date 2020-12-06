package ru.innohelpers.innohelp.component_model.collections

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

@Suppress("UNCHECKED_CAST")
class ObservableCollection<TValue> : Iterable<TValue> {

    private val mHandler = Handler(Looper.getMainLooper())
    private val mObserversMap = HashMap<Lifecycle, CollectionListenersConfiguration<TValue>>()
    private var mSize = 0
    private val defaultCapacityResize = 5
    private var data: Array<Any?> = emptyArray()
    private var capacity = 0

    var size: Int
        get() {return mSize; }
        private set(value) {mSize = value;}

    private fun observe(
        lifecycleOwner: LifecycleOwner,
        configuration: CollectionListenersConfiguration<TValue>
    ) {
        val observer = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun removeFromMap() {
                mObserversMap.remove(lifecycleOwner.lifecycle)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        mObserversMap[lifecycleOwner.lifecycle] = configuration

        mHandler.post { configuration.itemsInsertedListener?.invoke(this,  ItemsInsertedArgs(0, size)) }
    }

    fun add(item: TValue) {
        addSilently(item)
        mHandler.post{ notifyAllOnInsert(size - 1, 1) }
    }

    fun addSilently(item: TValue) {
        if (size == capacity)
            extend()
        data[size] = item
        size++
    }

    fun addAll(items: Iterable<TValue>) {
        addAllSilently(items)
        mHandler.post { notifyAllOnInsert(size - items.count(), items.count()) }
    }

    fun addAllSilently(items: Iterable<TValue>) {
        for (item in items)
            addSilently(item)
    }

    fun insert(item: TValue, position: Int) {
        insertSilently(item, position)
        mHandler.post{ notifyAllOnInsert(position, 1) }
    }

    fun insertSilently(item: TValue, position: Int) {
        if (position < 0 || position > size)
            throw IndexOutOfBoundsException()

        if (position == size) {
            addSilently(item)
            return
        }

        if (size == capacity)
            extend()
        data.copyInto(data, position + 1, position, size)
        data[position] = item
        size++
    }

    fun insertAllSilently(startPosition: Int, items: Iterable<TValue>) {
        val itemsCount = items.count()
        if (size == capacity)
            extend(itemsCount)

        var currentPosition = startPosition
        data.copyInto(data, startPosition + itemsCount, startPosition, size + itemsCount - 1)
        for (item in items) {
            data[currentPosition] = item
            currentPosition++
        }
        size += itemsCount
    }

    fun insertAll(startPosition: Int, items: Iterable<TValue>) {
        insertAllSilently(startPosition, items)
        mHandler.post { notifyAllOnInsert(startPosition, items.count()) }
    }

    fun clearSilently() {
        removeRangeSilently(0, size)
    }

    fun clear() {
        val list = toCollection()
        clearSilently()

        mHandler.post { notifyAllOnRemove(0, size, list) }
    }

    operator fun get(position: Int): TValue {
        if (position < 0 || position >= size)
            throw IndexOutOfBoundsException()

        return data[position] as TValue
    }

    fun getRange(startPosition: Int, count: Int): Collection<TValue> {
        val list = arrayListOf<TValue>()
        for (position in startPosition until startPosition+count) {
            list.add(data[position] as TValue)
        }
        return list
    }

    operator fun set(position: Int, item: TValue) {
        setSilently(position, item)
        mHandler.post { notifyAllOnChange(position, 1, arrayListOf(item)) }
    }

    fun setRange(startPosition: Int, items: Iterable<TValue>) {
        setRangeSilently(startPosition, items)
        mHandler.post { notifyAllOnChange(startPosition, items.count(), items.toList()) }
    }

    fun setRangeSilently(startPosition: Int, items: Iterable<TValue>) {
        var currentPosition = startPosition
        for (item in items) {
            setSilently(currentPosition, item)
            currentPosition++
        }
    }

    fun setSilently(position: Int, item: TValue) {
        if (position < 0 || position >= size)
            throw IndexOutOfBoundsException()

        data[position] = item
    }

    fun indexOf(item: TValue): Int {
        return indexOf(item, 0)
    }

    fun indexOf(item: TValue, offset: Int): Int {
        for (position in offset until size) {
            if (data[position] == item) return position
        }
        return -1
    }

    fun removeAtSilently(position: Int) {
        if (position < 0 || position >= size)
            throw IndexOutOfBoundsException()

        data.copyInto(data, position, position + 1)
        data[size] = null
        size--
    }

    fun removeAt(position: Int) {
        if (position < 0 || position >= size)
            throw IndexOutOfBoundsException()

        val item = data[position] as TValue
        removeAtSilently(position)
        mHandler.post { notifyAllOnRemove(position, 1, arrayListOf(item)) }
    }

    fun removeSilently(value: TValue) {
        val itemPosition = indexOf(value)
        if (itemPosition == -1) return

        removeAtSilently(itemPosition)
    }

    fun remove(value: TValue) {
        val position = indexOf(value)
        if (position == -1) return

        val item = data[position] as TValue
        removeAtSilently(position)
        mHandler.post { notifyAllOnRemove(position, 1, listOf(item)) }
    }

    fun removeRangeSilently(startPosition: Int, count: Int) {
        if (count == 0) return

        if (startPosition < 0 || startPosition >= size)
            throw IndexOutOfBoundsException()

        if (startPosition+count >= size) {
            data.fill(null, startPosition)
            size = startPosition
            return
        }

        data.copyInto(data, startPosition, startPosition+count)
        data.fill(null, size-count)
        size -= count
    }

    fun removeRange(startPosition: Int, count: Int) {
        if (count == 0) return

        if (startPosition < 0 || startPosition >= size)
            throw IndexOutOfBoundsException()


        val list = ArrayList<TValue>()
        for (position in startPosition until size) {
            if (list.size == count) break
            list.add(data[position] as TValue)
        }

        removeRangeSilently(startPosition, count)
        mHandler.post { notifyAllOnRemove(startPosition, list.size, list) }
    }

    fun swapSilently(position1: Int, position2: Int) {
        if (position1 < 0 || position2 < 0 || position1 >= size || position2 >= size)
            throw IndexOutOfBoundsException()

        val tmp = data[position1]
        data[position1] = data[position2]
        data[position2] = tmp
    }

    fun swap(position1: Int, position2: Int) {
        swapSilently(position1, position2)
        mHandler.post { notifyAllOnSwap(position1, position2) }
    }

    fun moveSilently(oldPosition: Int, newPosition: Int) {
        if (oldPosition < 0 || newPosition < 0 || oldPosition >= size || newPosition >= size)
            throw IndexOutOfBoundsException()

        if (oldPosition == newPosition) return

        val value = data[oldPosition]
        if (newPosition < oldPosition) {
            data.copyInto(data, newPosition + 1, newPosition, oldPosition - 1)
        }
        if (newPosition > oldPosition) {
            data.copyInto(data, oldPosition, oldPosition + 1, newPosition)
        }
        data[newPosition] = value
    }

    fun move(oldPosition: Int, newPosition: Int) {
        moveSilently(oldPosition, newPosition)
        if (oldPosition == newPosition) return

        mHandler.post { notifyAllOnMove(oldPosition, newPosition) }
    }

    fun toCollection(): Collection<TValue> {
        val list = ArrayList<TValue>()
        for (position in 0 until size)
            list.add(data[position] as TValue)

        return list
    }

    @MainThread
    private fun notifyAllOnInsert(startPosition: Int, count: Int) {
        for (keyValuePair in mObserversMap) {
            keyValuePair.value.itemsInsertedListener?.invoke(this, ItemsInsertedArgs(startPosition, count))
        }
    }

    @MainThread
    private fun notifyAllOnRemove(startPosition: Int, count: Int, items: Collection<TValue>) {
        for (keyValuePair in mObserversMap) {
            keyValuePair.value.itemsRemovedListener?.invoke(this, ItemsRemovedArgs(startPosition, count, items))
        }
    }

    @MainThread
    private fun notifyAllOnChange(startPosition: Int, count: Int, items: Collection<TValue>) {
        for (keyValuePair in mObserversMap) {
            keyValuePair.value.itemsChangedListener?.invoke(this, ItemsChangedArgs(startPosition, count, items))
        }
    }

    @MainThread
    private fun notifyAllOnSwap(position1: Int, position2: Int) {
        for (keyValuePair in mObserversMap) {
            keyValuePair.value.itemsSwappedListener?.invoke(this, ItemsSwappedArgs(position1, position2))
        }
    }

    @MainThread
    private fun notifyAllOnMove(oldPosition: Int, newPosition: Int) {
        for (keyValuePair in mObserversMap) {
            keyValuePair.value.itemsMovedListener?.invoke(this, ItemMovedArgs(oldPosition, newPosition))
        }
    }

    fun observe(
        lifecycleOwner: LifecycleOwner,
        itemsChangedListener: ItemsChangedListener<TValue>? = null,
        itemsInsertedListener: ItemsInsertedListener<TValue>? = null,
        itemsRemovedListener: ItemsRemovedListener<TValue>? = null,
        itemsSwappedListener: ItemsSwappedListener<TValue>? = null,
        itemsMovedListener: ItemMovedListener<TValue>? = null
    ) {
        val configuration = CollectionListenersConfiguration(
            itemsChangedListener,
            itemsInsertedListener,
            itemsRemovedListener,
            itemsSwappedListener,
            itemsMovedListener
        )
        observe(lifecycleOwner, configuration)
    }

    private fun extend(extendSize: Int) {
        val newArray = arrayOfNulls<Any>(capacity + extendSize)
        data.copyInto(newArray)

        data = newArray
        capacity += extendSize
    }

    private fun extend() {
        extend(defaultCapacityResize)
    }

    override fun iterator(): Iterator<TValue> {
        return ObservableCollectionIterator(data, size)
    }

    class ObservableCollectionIterator<TValue>(private val data: Array<Any?>, private val size: Int) : Iterator<TValue> {
        private var currentIndex = 0

        override fun hasNext(): Boolean {
            return size > currentIndex
        }

        override fun next(): TValue {
            return data[currentIndex++] as TValue
        }
    }
}