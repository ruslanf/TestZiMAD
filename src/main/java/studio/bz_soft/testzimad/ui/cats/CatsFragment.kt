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

    private lateinit var bundleRecyclerViewState: Bundle

    private val dAdapter = DelegateAdapter(CatsItemDelegate { cats ->
        presenter.showCatDetailed(cats)
    })
    private var job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_cats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshAdapter()
        view.apply {
            recyclerViewCats.apply {
                adapter = dAdapter
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            }
        }
        saveState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val recyclerViewState = recyclerViewCats.layoutManager?.onSaveInstanceState()
        val position: Int = (recyclerViewCats.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        outState.apply {
            putParcelable(KEY_RECYCLER_STATE, recyclerViewState)
            putInt(KEY_RECYCLER_POSITION, position)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState.apply {
            val recyclerViewState: Parcelable? = savedInstanceState?.getParcelable(KEY_RECYCLER_STATE)
            val position: Int? = savedInstanceState?.getInt(KEY_RECYCLER_POSITION)
            if (recyclerViewState != null && position != null) {
                recyclerViewCats.layoutManager?.onRestoreInstanceState(recyclerViewState)
                recyclerViewCats.scrollToPosition(position)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("Cats", "onPause()...")
        saveState()
    }

    override fun onResume() {
        super.onResume()
        Log.d("Cats", "onResume()...")
        bundleRecyclerViewState.apply {
            Handler().postDelayed(Runnable {
                val recyclerViewState: Parcelable? = getParcelable(KEY_RECYCLER_STATE)
                val position: Int = getInt(KEY_RECYCLER_POSITION)
                if (recyclerViewState != null) {
                    recyclerViewCats.layoutManager?.onRestoreInstanceState(recyclerViewState)
                    recyclerViewCats.scrollToPosition(position)
                }
            }, 50)
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

    private fun saveState() {
        val recyclerViewState = recyclerViewCats.layoutManager?.onSaveInstanceState()
        val position: Int = (recyclerViewCats.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        bundleRecyclerViewState = Bundle().apply {
            putParcelable(KEY_RECYCLER_STATE, recyclerViewState)
            putInt(KEY_RECYCLER_POSITION, position)
        }
    }

    private fun refreshAdapter() {
        progressBar.visibility = View.VISIBLE
        launch {
            var cats: List<DataList> = emptyList()
            val request = async(Dispatchers.IO) {
                cats = presenter.getListOfCats()
            }
            request.await()
            renderCats(cats)
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