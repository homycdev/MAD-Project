package ru.innohelpers.innohelp.component_model

class ValueChangedConfiguration<TValue> {

    var valueFromSet = false
        private set

    var valueToSet = false
        private set

    private var _valueFrom: TValue? = null
    private var _valueTo: TValue? = null

    var changedListener: ValueChangedListener<TValue>? = null

    var valueFrom: TValue?
        get() {if (!valueFromSet) throw IllegalAccessException("Property valueFrom not set"); return _valueFrom; }
        set(value) {_valueFrom = value; valueFromSet = true }

    var valueTo: TValue?
        get() {if (!valueToSet) throw IllegalAccessException("Property valueTo not set"); return _valueTo; }
        set(value) {_valueTo = value; valueToSet = true}
}
