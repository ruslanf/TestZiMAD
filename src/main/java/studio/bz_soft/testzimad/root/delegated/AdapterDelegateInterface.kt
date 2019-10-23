package studio.bz_soft.testzimad.root.delegated

import android.view.ViewGroup

interface AdapterDelegateInterface<T> {
    fun isForViewType(items: List<T>, position: Int): Boolean
    fun createViewHolder(parent: ViewGroup): BaseHolder<T>
    fun bindViewHolder(items: List<T>, position: Int, holder: BaseHolder<T>)

    fun onViewAttachedToWindow(holder: BaseHolder<T>) {}
    fun onViewDetachedFromWindow(holder: BaseHolder<T>) {}
}