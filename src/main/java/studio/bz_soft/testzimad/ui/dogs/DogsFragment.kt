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
import kotlinx.android.synthetic.main.fragment_dogs.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import studio.bz_soft.testzimad.R
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.BackPressedInterface
import studio.bz_soft.testzimad.root.Constants
import studio.bz_soft.testzimad.root.delegated.DelegateAdapter
import studio.bz_soft.testzimad.ui.common.DogsElements
import studio.bz_soft.testzimad.ui.common.DogsItemDelegate
import kotlin.coroutines.CoroutineContext

class DogsFragment : Fragment(), BackPressedInterface, CoroutineScope {

    private val presenter: DogsPresenter by inject()

    private lateinit var bundleRecyclerView: Bundle

    private val dAdapter = DelegateAdapter(DogsItemDelegate { dogs ->
        presenter.showDogDetailed(dogs)
    })
    private var dogs: List<DataList> = emptyList()
    private var job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

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

    override fun onPause() {
        super.onPause()
        bundleRecyclerView = Bundle()
        val recyclerViewState = recyclerViewDogs.layoutManager?.onSaveInstanceState()
        bundleRecyclerView.putParcelable(Constants.KEY_RECYCLER_STATE, recyclerViewState)
    }

    override fun onResume() {
        super.onResume()
        if (!bundleRecyclerView.isEmpty) {
            val recyclerViewState = bundleRecyclerView.getParcelable(Constants.KEY_RECYCLER_STATE) as Parcelable
            recyclerViewDogs.layoutManager?.onRestoreInstanceState(recyclerViewState)
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
                dogs = presenter.getListOfDogs()
            }
            request.await()
            renderDogs(dogs)
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