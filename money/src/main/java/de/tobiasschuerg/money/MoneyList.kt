package de.tobiasschuerg.money

import java.math.BigDecimal

/**
 * List of money which provides additional features such as sum, average, median etc...
 *
 * Created by Tobias Schürg on 18.10.2017.
 */
class MoneyList(private val currency: Currency, val autoConvert: Boolean = false) : MutableList<Money> {

    private val list = mutableListOf<Money>()

    private var sum = Money.ZERO.copy(currency = currency)

    override val size = list.size

    override fun contains(element: Money) = list.contains(element)

    override fun containsAll(elements: Collection<Money>) = list.containsAll(elements)

    override fun get(index: Int): Money = list.get(index)

    override fun indexOf(element: Money): Int = list.indexOf(element)

    override fun isEmpty(): Boolean = list.isEmpty()

    override fun iterator() = list.iterator()

    override fun lastIndexOf(element: Money): Int = list.lastIndexOf(element)

    override fun listIterator() = list.listIterator()

    override fun listIterator(index: Int) = list.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Money> = subList(fromIndex, toIndex)

    override fun add(element: Money): Boolean {
        list.add(element)
        addToSum(element)
        return true
    }

    override fun add(index: Int, element: Money) {
        list.add(index, element)
        addToSum(element)
    }

    private fun addToSum(element: Money) {
        sum += if (autoConvert) {
            element.convertInto(currency)
        } else {
            element
        }
    }

    private fun substractFromSum(element: Money) {
        sum -= if (autoConvert) {
            element.convertInto(currency)
        } else {
            element
        }
    }

    override fun addAll(index: Int, elements: Collection<Money>): Boolean {
        list.addAll(index, elements)
        elements.forEach(this::addToSum)
        return true
    }

    override fun addAll(elements: Collection<Money>): Boolean {
        list.addAll(elements)
        elements.forEach(this::addToSum)
        return true
    }

    override fun clear() {
        list.clear()
        sum = Money.ZERO
    }

    override fun remove(element: Money): Boolean {
        list.remove(element)
        substractFromSum(element)
        return true
    }

    override fun removeAll(elements: Collection<Money>): Boolean {
        val success = list.removeAll(elements)
        elements.forEach(this::substractFromSum)
        return success
    }

    override fun removeAt(index: Int): Money {
        val element = list.removeAt(index)
        substractFromSum(element)
        return element
    }

    override fun retainAll(elements: Collection<Money>): Boolean {
        TODO("not implemented")
    }

    override fun set(index: Int, element: Money): Money {
        TODO("not implemented")
    }

    fun sum(): Money = sum

    fun min(): Money? = list.minBy(Money::amount)

    fun max(): Money? = list.maxBy(Money::amount)

    fun median(): Money? {
        if (list.isNotEmpty()) {
            val index: Int = list.size / 2
            return list.sortedBy(Money::amount)[index]
        } else {
            return null
        }
    }

    fun average(): Money? {
        return if (list.isNotEmpty()) {
            sum / BigDecimal(list.size)
        } else {
            null
        }
    }
}