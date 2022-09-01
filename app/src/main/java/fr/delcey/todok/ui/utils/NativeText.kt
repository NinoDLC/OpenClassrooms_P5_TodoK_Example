@file:Suppress("unused")
package fr.delcey.todok.ui.utils

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class NativeText {

    abstract fun toCharSequence(context: Context): CharSequence

    data class Simple(val text: CharSequence) : NativeText() {
        override fun toCharSequence(context: Context): CharSequence = text
    }

    data class Resource(@StringRes val id: Int) : NativeText() {
        override fun toCharSequence(context: Context): CharSequence = context.getString(id)
    }

    data class Plural(@PluralsRes val id: Int, val number: Int, val args: List<Any>) : NativeText() {
        override fun toCharSequence(context: Context): CharSequence = context.resources.getQuantityString(id, number, *args.toTypedArray())
    }

    data class Arguments(@StringRes val id: Int, val args: List<Any>) : NativeText() {
        override fun toCharSequence(context: Context): CharSequence = context.getString(id, *args.toTypedArray())
    }
}