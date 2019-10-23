package studio.bz_soft.testzimad.ui.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detailed_animal.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get
import studio.bz_soft.testzimad.R
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.BackPressedInterface
import studio.bz_soft.testzimad.root.Constants.KEY_ANIMALS
import studio.bz_soft.testzimad.root.Constants.KEY_SCREEN
import studio.bz_soft.testzimad.root.Screens
import kotlin.coroutines.CoroutineContext

class DetailedFragment: Fragment(), BackPressedInterface, CoroutineScope {

    private var job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
    private val animals = CompletableDeferred<DataList>()

    private lateinit var screen: Screens

    private val presenter: DetailedPresenter = DetailedPresenter(get(), animals)

    private var info = DataList("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animals.complete(arguments?.getParcelable(KEY_ANIMALS) ?: DataList())
        screen = arguments?.getSerializable(KEY_SCREEN) as Screens
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  = inflater.inflate(R.layout.fragment_detailed_animal, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch {
            val request = async(Dispatchers.IO) {
                info = presenter.getDetailedInfo()
            }
            request.await()
            renderAnimal(view, info)
        }
        view.apply {
            ivBackButton.setOnClickListener { presenter.onBackPressed(screen) }
        }
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed(screen)
        return true
    }

    private fun renderAnimal(v: View, animal: DataList) {
        v.apply {
            Picasso.get()
                .load(animal.url)
                .into(ivAnimal)
            tvAnimalTitle.text = animal.title
        }
    }

    companion object {
        fun instance(animals: DataList, screen: Screens): DetailedFragment = DetailedFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_ANIMALS, animals)
                putSerializable(KEY_SCREEN, screen)
            }
        }
    }
}