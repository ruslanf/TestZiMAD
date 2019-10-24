package studio.bz_soft.testzimad.ui.cats

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_cats.*
import kotlinx.android.synthetic.main.fragment_cats.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import studio.bz_soft.testzimad.R
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.BackPressedInterface
import studio.bz_soft.testzimad.root.Constants.KEY_RECYCLER_POSITION
import studio.bz_soft.testzimad.root.Constants.KEY_RECYCLER_STATE
import studio.bz_soft.testzimad.root.delegated.DelegateAdapter
import studio.bz_soft.testzimad.ui.common.CatsElements
import studio.bz_soft.testzimad.ui.common.CatsItemDelegate
import kotlin.coroutines.CoroutineContext

class CatsFragment : Fragment(), BackPressedInterface, CoroutineScope {

    private val presenter: CatsPresenter by inject()

    private var cats: List<DataList> = emptyList()

    private val dAdapter = DelegateAdapter(CatsItemDelegate { cats ->
        presenter.showCatDetailed(cats)
    })
    private var job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
    private var recyclerViewState: Parcelable? = null
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.apply {
            recyclerViewState = getParcelable(KEY_RECYCLER_STATE)
            position = getInt(KEY_RECYCLER_POSITION)
            Log.d("Cats", "onCreate() position is -> $position")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_cats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Cats", "onViewCreated() position is -> $position")

        view.apply {
            recyclerViewCats.apply {
                adapter = dAdapter
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
                refreshAdapter()
//                if (cats.isNotEmpty()) renderCats(cats)
                recyclerViewState?.apply {
                    layoutManager?.onRestoreInstanceState(recyclerViewState)
                    scrollToPosition(position)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            val recyclerViewState = recyclerViewCats.layoutManager?.onSaveInstanceState()
            val position: Int =
                (recyclerViewCats.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            Log.d("Cats", "onSaveInstanceState() position is -> $position")
            putParcelable(KEY_RECYCLER_STATE, recyclerViewState)
            putInt(KEY_RECYCLER_POSITION, position)
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
        Log.d("Cats", "refreshAdapter()...")
        progressBar.visibility = View.VISIBLE
        launch {

            val request = async(Dispatchers.IO) {
                cats = presenter.getListOfCats()
            }
            request.await()
            if (request.isCompleted) renderCats(cats)
            progressBar.visibility = if (request.isCompleted) View.GONE else View.VISIBLE
        }
    }

    private fun renderCats(cats: List<DataList>) {
        dAdapter.apply {
            items.clear()
            items.addAll(cats.map { CatsElements.CatsItem(it) })
            notifyDataSetChanged()
        }
    }

    companion object {
        fun instance(): CatsFragment = CatsFragment()
    }
}