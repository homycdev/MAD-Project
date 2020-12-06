package ru.innohelpers.innohelp.component_model.collections

data class ItemsInsertedArgs(
    var startPosition: Int,
    var count: Int
)

data class ItemsChangedArgs<TValue>(
    var startPosition: Int,
    var count: Int,
    var items: Collection<TValue>
)

data class ItemsRemovedArgs<TValue>(
    var position: Int,
    var count: Int,
    var items: Collection<TValue>
)

data class ItemsSwappedArgs(
    var position1: Int,
    var position2: Int
)

data class ItemMovedArgs(
    var oldPosition: Int,
    var newPosition: Int
)

typealias ItemsInsertedListener<TValue> = (ObservableCollection<TValue>, ItemsInsertedArgs) -> Unit
typealias ItemsRemovedListener<TValue> = (ObservableCollection<TValue>, ItemsRemovedArgs<TValue>) -> Unit
typealias ItemsSwappedListener<TValue> = (ObservableCollection<TValue>, ItemsSwappedArgs) -> Unit
typealias ItemMovedListener<TValue> = (ObservableCollection<TValue>, ItemMovedArgs) -> Unit
typealias ItemsChangedListener<TValue> = (ObservableCollection<TValue>, ItemsChangedArgs<TValue>) -> Unit