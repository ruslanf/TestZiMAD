package studio.bz_soft.testzimad.ui.dogs

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_dogs.*
import kotlinx.android.synthetic.main.fragment_dogs.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import studio.bz_soft.testzimad.R
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.BackPressedInterface
import studio.bz_soft.testzimad.root.Constants.KEY_RECYCLER_POSITION
import studio.bz_soft.testzimad.root.Constants.KEY_RECYCLER_STATE
import studio.bz_soft.testzimad.root.delegated.DelegateAdapter
import studio.bz_soft.testzimad.ui.common.DogsElements
import studio.bz_soft.testzimad.ui.common.DogsItemDelegate
import kotlin.coroutines.CoroutineContext

class DogsFragment : Fragment(), BackPressedInterface, CoroutineScope {

    private val presenter: DogsPresenter by inject()

    private lateinit var bundleRecyclerViewState: Bundle

    private val dAdapter = DelegateAdapter(DogsItemDelegate { dogs ->
        presenter.showDogDetailed(dogs)
    })
    private var job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            val recyclerViewState: Parcelable? = savedInstanceState.getParcelable(KEY_RECYCLER_STATE)
            val position: Int? = savedInstanceState.getInt(KEY_RECYCLER_POSITION)
            if (recyclerViewState != null && position != null) {
                recyclerViewDogs.layoutManager?.onRestoreInstanceState(recyclerViewState)
                recyclerViewDogs.scrollToPosition(position)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dogs, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshAdapter()
        view.apply {
            recyclerViewDogs.apply {
                adapter = dAdapter
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            }
        }
        saveState()
    }

    override fun onPause() {
        super.onPause()
        saveState()
    }

    override fun onResume() {
        super.onResume()
        bundleRecyclerViewState.apply {
            Handler().postDelayed(Runnable {
                val recyclerViewState: Parcelable? = getParcelable(KEY_RECYCLER_STATE)
                if (recyclerViewState != null) {
                    recyclerViewDogs.layoutManager?.onRestoreInstanceState(recyclerViewState)
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
        val recyclerViewState = recyclerViewDogs.layoutManager?.onSaveInstanceState()
        bundleRecyclerViewState = Bundle().apply {
            putParcelable(KEY_RECYCLER_STATE, recyclerViewState)
        }
    }

    private fun refreshAdapter() {
        progressBar.visibility = View.VISIBLE
        launch {
            var dogs: List<DataList> = emptyList()
            val request = async(Dispatchers.IO) {
                dogs = presenter.getListOfDogs()
            }
            request.await()
            renderDogs(dogs)
            progressBar.visibility = if (request.isCompleted) View.GONE else View.VISIBLE
        }
    }

    private fun renderDogs(dogs: List<DataList>) {
        dAdapter.apply {
            items.clear()
            items.addAll(dogs.map { DogsElements.DogsItem(it) })
            notifyDataSetChanged()
        }
    }

    companion object {
        fun instance(): DogsFragment = DogsFragment()
    }
}