package com.woonggon.rv_ex

import org.junit.Test
import org.junit.Assert.*

class RVAdapterTest {

    @Test
    fun getItemCount_returnsCorrectSize() {
        val items = mutableListOf("a", "b", "c")
        val adapter = RVAdapter(items)
        assertEquals(3, adapter.itemCount)
    }

    @Test
    fun getItemCount_emptyList_returnsZero() {
        val items = mutableListOf<String>()
        val adapter = RVAdapter(items)
        assertEquals(0, adapter.itemCount)
    }

    @Test
    fun getItem_returnsCorrectItem() {
        val items = mutableListOf("첫번째", "두번째", "세번째")
        val adapter = RVAdapter(items)
        assertEquals("첫번째", items[0])
        assertEquals("세번째", items[2])
    }

    @Test
    fun itemClick_isNullByDefault() {
        val adapter = RVAdapter(mutableListOf("test"))
        assertNull(adapter.itemClick)
    }

    @Test
    fun itemClick_canBeSet() {
        val adapter = RVAdapter(mutableListOf("test"))
        var clicked = false
        adapter.itemClick = object : ItemClick {
            override fun onClick(view: android.view.View, position: Int) {
                clicked = true
            }
        }
        assertNotNull(adapter.itemClick)
    }

    @Test
    fun items_addAndRemove() {
        val items = mutableListOf("a", "b")
        val adapter = RVAdapter(items)
        assertEquals(2, adapter.itemCount)

        items.add("c")
        assertEquals(3, adapter.itemCount)

        items.removeAt(0)
        assertEquals(2, adapter.itemCount)
        assertEquals("b", items[0])
    }
}
