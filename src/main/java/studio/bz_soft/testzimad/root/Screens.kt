package studio.bz_soft.testzimad.root

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.ui.cats.CatsFragment
import studio.bz_soft.testzimad.ui.detailed.DetailedFragment
import studio.bz_soft.testzimad.ui.dogs.DogsFragment
import studio.bz_soft.testzimad.ui.main.MainFragment
import java.io.Serializable

sealed class Screens: SupportAppScreen(), Serializable {

    object MainScreen: Screens() {
        override fun getFragment(): Fragment = MainFragment.instance()
    }

    data class DetailedAnimalScreen(val animal: DataList, val screen: Screens) : Screens() {
        override fun getFragment(): Fragment = DetailedFragment.instance(animal, screen)
    }

    object CatsScreen: Screens() {
        override fun getFragment(): Fragment = CatsFragment.instance()
    }

    object DogsScreen: Screens() {
        override fun getFragment(): Fragment = DogsFragment.instance()
    }
}