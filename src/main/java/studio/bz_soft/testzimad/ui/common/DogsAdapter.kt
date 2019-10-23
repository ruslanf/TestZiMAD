package studio.bz_soft.testzimad.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_cell_dogs.view.*
import studio.bz_soft.testzimad.R
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.delegated.AdapterDelegateInterface
import studio.bz_soft.testzimad.root.delegated.BaseHolder

sealed class DogsElements {
    data class DogsItem(val animal: DataList): DogsElements()
}

class DogsItemHolder(v: View, val onClick: (DataList) -> Unit): BaseHolder<DogsElements>(v) {

    override fun bindModel(item: DogsElements) {
        super.bindModel(item)
        when (item) {
            is DogsElements.DogsItem -> itemView.apply {
                Picasso.get()
                    .load(item.animal.url)
                    .into(ivDog)
                tvDogTitle.text = item.animal.title
                setOnClickListener { onClick(item.animal) }
            }
        }
    }
}

class DogsItemDelegate(private val onClick: (DataList) -> Unit): AdapterDelegateInterface<DogsElements> {

    override fun isForViewType(items: List<DogsElements>, position: Int): Boolean {
        return items[position] is DogsElements.DogsItem
    }

    override fun createViewHolder(parent: ViewGroup): BaseHolder<DogsElements> {
        return DogsItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_cell_dogs, parent, false),
            onClick
        )
    }

    override fun bindViewHolder(items: List<DogsElements>, position: Int, holder: BaseHolder<DogsElements>) {
        holder.bindModel(items[position])
    }
}