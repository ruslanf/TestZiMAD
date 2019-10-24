package studio.bz_soft.testzimad.ui.dogs

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_dogs.*
import kotlinx.android.synthetic.main.fragment_dogs.progressBar
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

    private val dAdapter = DelegateAdapter(DogsItemDelegate { dogs ->
        presenter.showDogDetailed(dogs)
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            val recyclerViewState = recyclerViewDogs.layoutManager?.onSaveInstanceState()
            val position: Int =
                (recyclerViewDogs.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
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