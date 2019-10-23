package studio.bz_soft.testzimad.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_cell.view.*
import studio.bz_soft.testzimad.R
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.delegated.AdapterDelegateInterface
import studio.bz_soft.testzimad.root.delegated.BaseHolder

sealed class CatsElements {
    data class CatsItem(val animal: DataList): CatsElements()
}

class CatsItemHolder(v: View, val onClick: (DataList) -> Unit): BaseHolder<CatsElements>(v) {

    override fun bindModel(item: CatsElements) {
        super.bindModel(item)
        when (item) {
            is CatsElements.CatsItem -> itemView.apply {
                Picasso.get()
                    .load(item.animal.url)
                    .into(ivAnimal)
                tvAnimalTitle.text = item.animal.title
                setOnClickListener { onClick(item.animal) }
            }
        }
    }
}

class CatsItemDelegate(private val onClick: (DataList) -> Unit): AdapterDelegateInterface<CatsElements> {

    override fun isForViewType(items: List<CatsElements>, position: Int): Boolean {
        return items[position] is CatsElements.CatsItem
    }

    override fun createViewHolder(parent: ViewGroup): BaseHolder<CatsElements> {
        return CatsItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_cell, parent, false),
            onClick
        )
    }

    override fun bindViewHolder(items: List<CatsElements>, position: Int, holder: BaseHolder<CatsElements>) {
        holder.bindModel(items[position])
    }
}