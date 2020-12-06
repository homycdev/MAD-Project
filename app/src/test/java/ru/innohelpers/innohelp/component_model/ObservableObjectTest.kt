package ru.innohelpers.innohelp.component_model

import junit.framework.Assert.assertEquals
import org.junit.Test

class ObservableObjectTest {

    @Test
    fun simpleTest() {
        val value = ObservableObject<Int>()
        assertEquals(null, value.value)
    }

    @Test
    fun assignTest() {
        val value = ObservableObject<Int>()
        value.value = 5
        assertEquals(5, value.value)
    }
}