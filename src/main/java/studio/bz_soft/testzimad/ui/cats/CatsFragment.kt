package studio.bz_soft.testzimad.ui.cats

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_animals.*
import kotlinx.android.synthetic.main.fragment_animals.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import studio.bz_soft.testzimad.R
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.BackPressedInterface
import studio.bz_soft.testzimad.root.Constants.KEY_RECYCLER_STATE
import studio.bz_soft.testzimad.root.delegated.DelegateAdapter
import studio.bz_soft.testzimad.ui.common.AnimalElements
import studio.bz_soft.testzimad.ui.common.AnimalsItemDelegate
import kotlin.coroutines.CoroutineContext

class CatsFragment : Fragment(), BackPressedInterface, CoroutineScope {

    private val presenter: CatsPresenter by inject()

    private lateinit var bundleRecyclerView: Bundle

    private val dAdapter = DelegateAdapter(AnimalsItemDelegate { cats ->
        presenter.showCatDetailed(cats)
    })
    private var cats: List<DataList> = emptyList()
    private var job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_animals, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshAdapter()
        view.apply {
            swipeRefreshCats.setOnRefreshListener { refreshAdapter() }

            recyclerViewAnimals.apply {
                adapter = dAdapter
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        bundleRecyclerView = Bundle()
        val recyclerViewState = recyclerViewAnimals.layoutManager?.onSaveInstanceState()
        bundleRecyclerView.putParcelable(KEY_RECYCLER_STATE, recyclerViewState)
    }

    override fun onResume() {
        super.onResume()
        if (!bundleRecyclerView.isEmpty) {
            val recyclerViewState = bundleRecyclerView.getParcelable(KEY_RECYCLER_STATE) as Parcelable
            recyclerViewAnimals.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed()
        return true
    }

    private fun refreshAdapter() {
        launch {
            val request = async(Dispatchers.IO) {
                cats = presenter.getListOfCats()
            }
            request.await()
            renderCats(cats)
        }
    }

    private fun renderCats(cats: List<DataList>) {
        dAdapter.apply {
            items.clear()
            items.addAll(cats.map { AnimalElements.AnimalItem(it) })
            notifyDataSetChanged()
        }
    }

    companion object {
        fun instance(): CatsFragment = CatsFragment()
    }
}