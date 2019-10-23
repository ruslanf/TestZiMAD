package studio.bz_soft.testzimad.di

import kotlinx.coroutines.channels.Channel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import studio.bz_soft.testzimad.data.Repository
import studio.bz_soft.testzimad.data.http.ApiClient
import studio.bz_soft.testzimad.data.http.ApiClientInterface
import studio.bz_soft.testzimad.root.App
import studio.bz_soft.testzimad.root.Constants
import studio.bz_soft.testzimad.root.MainRouter
import studio.bz_soft.testzimad.ui.cats.CatsPresenter
import studio.bz_soft.testzimad.ui.detailed.DetailedPresenter
import studio.bz_soft.testzimad.ui.dogs.DogsPresenter
import studio.bz_soft.testzimad.ui.main.MainPresenter

val applicationModule = module {
    single { androidApplication() as App }
}

val networkModule = module {
    single { ApiClient(Constants.API_MAIN_URL, androidContext()) as ApiClientInterface }
    single { Repository(get()) }
}

val presenterModule = module {
    single { MainPresenter(get()) }
    single { CatsPresenter(get(), get()) }
    single { DogsPresenter(get(), get()) }
}

val navigationModule = module {
    single { Cicerone.create() }
    single { get<Cicerone<Router>>().router as Router }
    single { get<Cicerone<Router>>().navigatorHolder as NavigatorHolder }
    single { MainRouter(get(), Channel(Channel.UNLIMITED)).apply { start() } }
}