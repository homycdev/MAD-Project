package ru.innohelpers.innohelp.component_model

import org.junit.Test
import org.junit.Assert.*
import ru.innohelpers.innohelp.component_model.collections.ObservableCollection

class ObservableCollectionTest {

    @Test
    fun testAddition() {
        val collection = ObservableCollection<Int>()
        collection.add(1)

        assertEquals(1, collection[0])
        assertEquals(1, collection.size)
    }

    @Test
    fun complexAdditionText() {
        val collection = ObservableCollection<Int>()

        collection.addAll(listOf(1, 2, 3, 4, 5))
        assertEquals(1, collection[0])
        assertEquals(2, collection[1])
        assertEquals(3, collection[2])
        assertEquals(4, collection[3])
        assertEquals(5, collection[4])
        assertEquals(5, collection.size)
    }

    @Test
    fun removeTest() {
        val collection = ObservableCollection<Int>()
        collection.add(1)
        collection.add(2)
        assertEquals(2, collection.size)
        assertEquals(1, collection[0])
        assertEquals(2, collection[1])

        collection.removeAt(0)
        assertEquals(2, collection[0])
        assertEquals(1, collection.size)
    }

    @Test
    fun complexRemoveTest() {
        val collection = ObservableCollection<Int>()
        collection.add(1)
        collection.add(2)
        collection.add(3)
        collection.add(4)
        collection.add(5)
        collection.add(6)
        collection.add(7)

        assertEquals(7, collection.size)
        assertEquals(1, collection[0])
        assertEquals(2, collection[1])
        assertEquals(3, collection[2])
        assertEquals(4, collection[3])
        assertEquals(5, collection[4])
        assertEquals(6, collection[5])
        assertEquals(7, collection[6])

        collection.removeRange(0, 5)
        assertEquals(2, collection.size)
        assertEquals(6, collection[0])
        assertEquals(7, collection[1])

        collection.insert(8, 2)
        assertEquals(8, collection[2])
        assertEquals(3, collection.size)
    }

    @Test
    fun findTest() {
        val collection = ObservableCollection<Int>()
        collection.insert(0, 0)
        collection.insert(1, 1)
        collection.insert(2, 2)
        assertEquals(0, collection[0])
        assertEquals(1, collection[1])
        assertEquals(2, collection[2])
        assertEquals(3, collection.size)

        val index = collection.indexOf(0)
        assertEquals(0, index)
    }

    @Test
    fun clearTest() {
        val collection = ObservableCollection<Int>()
        collection.add(1)
        collection.add(2)
        collection.add(3)
        collection.add(4)

        assertEquals(1, collection[0])
        assertEquals(2, collection[1])
        assertEquals(3, collection[2])
        assertEquals(4, collection[3])
        assertEquals(4, collection.size)

        collection.clear()
        assertEquals(0, collection.size)
    }
}