package viewModel

import kotlinx.coroutines.Dispatchers

open class ViewModel {
    val viewModelScope = CloseableCoroutineScope(Dispatchers.IO)
}