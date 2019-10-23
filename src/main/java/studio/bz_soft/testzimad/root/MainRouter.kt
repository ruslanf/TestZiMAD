package studio.bz_soft.testzimad.root

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen

class MainRouter(router: Router, channel: Channel<(Router) -> Unit>) : RouterChannel<Screens>(router, channel)

abstract class RouterChannel<S>(private val router: Router,
                                private val channel: Channel<(Router) -> Unit>) where S : Screen {

    private val scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null

    fun start() {
        job = scope.launch { for (action in channel) action(router) }
    }

    fun stop() {
        job?.cancel()
    }

    fun backTo(screen: S) {
        channel.offer { it.backTo(screen) }
    }

    fun exit() {
        channel.offer { it.exit() }
    }

    fun navigateTo(screen: S) {
        channel.offer { it.navigateTo(screen) }
    }

    fun newRootScreen(screen: S) {
        channel.offer { it.newRootScreen(screen) }
    }
}
