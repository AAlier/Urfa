package com.urfa.util

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import android.content.Context
import android.view.MotionEvent
import androidx.annotation.DimenRes
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

inline fun <reified T : View.OnClickListener> View.setOnDelayedClickListener(listener: T) {
    isClickable = true
    isFocusableInTouchMode = false
    val safeClickListener = SafeClickListener {
        listener.onClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun View.setOnDelayedClickListener(listener: (view: View) -> Unit) {
    isClickable = true
    isFocusableInTouchMode = false
    val safeClickListener = SafeClickListener {
        listener(it)
    }
    setOnClickListener(safeClickListener)
}

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (data: T) -> Unit) {
    this.observe(owner, Observer {
        if (it != null) {
            observer.invoke(it)
        }
    })
}

/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Px internal fun Context.dimen(@DimenRes res: Int): Int {
    return resources.getDimensionPixelSize(res)
}

internal typealias ListAdapter<T> = RecyclerView.Adapter<T>

internal fun ListAdapter<*>.isEmpty(): Boolean {
    return itemCount == 0
}

internal fun RecyclerView.getItemPosition(e: MotionEvent): Int {
    val v = findChildViewUnder(e.x, e.y) ?: return RecyclerView.NO_POSITION
    return getChildAdapterPosition(v)
}