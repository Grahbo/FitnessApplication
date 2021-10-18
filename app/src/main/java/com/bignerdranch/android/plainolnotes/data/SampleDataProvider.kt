package com.bignerdranch.android.plainolnotes.data

import java.util.*

class SampleDataProvider {

    companion object{
        private val sampleText1 = "A Simple Note"
        private val sampleText2 = "A note a\nline feed"
        private val sampleText3 = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sit amet euismod eros, eget fringilla urna. Donec bibendum, risus ut ornare pulvinar, odio massa commodo turpis, in sagittis purus dolor vel magna. Etiam commodo, diam vitae pharetra luctus, magna justo tincidunt augue, non faucibus ante nisi non orci. Donec auctor in lectus ut interdum. Duis mattis odio non sem placerat viverra. Aenean et purus sagittis, accumsan enim id, rutrum massa. Pellentesque quam elit, volutpat ac aliquam non, scelerisque et orci. Suspendisse ultricies malesuada mi. Aenean eget ipsum risus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.

            Phasellus porttitor porttitor massa eu auctor. Donec vel justo risus. Fusce vel tellus in velit mollis venenatis. Phasellus a nisl sapien. Sed efficitur eu quam a bibendum. Vivamus sodales sed est sit amet commodo. Suspendisse id nunc sed ante pellentesque tincidunt. Phasellus quis nisl dignissim, placerat nunc ac, auctor ipsum. Proin mattis aliquet turpis, et congue odio. Quisque non pellentesque libero, ut posuere elit. Ut a eleifend ligula. Praesent eget lacinia nunc, commodo egestas nulla. In nec egestas urna. Cras ligula mi, malesuada eget consequat nec, maximus et orci. Curabitur consequat, nisi sit amet ullamcorper faucibus, arcu mauris eleifend lectus, et mattis magna nisi sit amet orci.
        """.trimIndent()

        private fun getDate(diff: Long): Date {
            return Date( Date().time + diff)
        }

        fun getNotes() = arrayListOf (
            NoteEntity(getDate(0), sampleText1),
            NoteEntity(getDate(1), sampleText2),
            NoteEntity(getDate(2), sampleText3)
                )

    }
}