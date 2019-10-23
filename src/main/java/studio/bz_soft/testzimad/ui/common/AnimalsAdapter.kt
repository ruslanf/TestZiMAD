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

sealed class AnimalElements {
    data class AnimalItem(val animal: DataList): AnimalElements()
}

class AnimalsItemHolder(v: View, val onClick: (DataList) -> Unit): BaseHolder<AnimalElements>(v) {

    override fun bindModel(item: AnimalElements) {
        super.bindModel(item)
        when (item) {
            is AnimalElements.AnimalItem -> itemView.apply {
                Picasso.get()
                    .load(item.animal.url)
                    .into(ivAnimal)
                tvAnimalTitle.text = item.animal.title
                setOnClickListener { onClick(item.animal) }
            }
        }
    }
}

class AnimalsItemDelegate(private val onClick: (DataList) -> Unit): AdapterDelegateInterface<AnimalElements> {

    override fun isForViewType(items: List<AnimalElements>, position: Int): Boolean {
        return items[position] is AnimalElements.AnimalItem
    }

    override fun createViewHolder(parent: ViewGroup): BaseHolder<AnimalElements> {
        return AnimalsItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_cell, parent, false),
            onClick
        )
    }

    override fun bindViewHolder(items: List<AnimalElements>, position: Int, holder: BaseHolder<AnimalElements>) {
        holder.bindModel(items[position])
    }
}