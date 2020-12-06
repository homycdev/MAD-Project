package ru.innohelpers.innohelp.component_model.collections

data class CollectionListenersConfiguration<TValue> (
    var itemsChangedListener: ItemsChangedListener<TValue>?,
    var itemsInsertedListener: ItemsInsertedListener<TValue>?,
    var itemsRemovedListener: ItemsRemovedListener<TValue>?,
    var itemsSwappedListener: ItemsSwappedListener<TValue>?,
    var itemsMovedListener: ItemMovedListener<TValue>?
)
