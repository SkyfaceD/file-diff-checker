package ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import ui.dropTarget.DropTargetState
import ui.dropTarget.DropTargetWrapper
import viewModel.ViewModel
import java.io.File

class AppViewModel(
    private val dropTargetWrapper: DropTargetWrapper
) : ViewModel() {
    var state by mutableStateOf(AppUiState())
        private set

    init {
        viewModelScope.launch { subscribeOnDropTarget() }
    }

    fun changeInPath(path: String) {
        state = state.copy(inPath = path)
    }

    fun changeOutPath(path: String) {
        state = state.copy(outPath = path)
    }

    fun difference() {

    }

    fun move() {

    }

    private suspend fun subscribeOnDropTarget() {
        dropTargetWrapper.state.collect {
            if (it == null) return@collect

            println("DropTargetState: $it")

            when (it) {
                is DropTargetState.Failure -> {
                    changeDrop(false)
                }
                is DropTargetState.Success -> {
                    val paths = it.files.map(File::toString)
                    changeInPath(paths.getOrElse(0) { "" })
                    changeOutPath(paths.getOrElse(1) { "" })
                    changeDrop(false)
                }
                DropTargetState.DragEnter -> changeDrop(true)
                DropTargetState.DragExit -> changeDrop(false)
            }
        }
    }

    private fun changeDrop(isDrop: Boolean) {
        state = state.copy(isDrop = isDrop)
    }
}

data class AppUiState(
    val isLoading: Boolean = false,
    val inPath: String = "",
    val outPath: String = "",
    val isDifferent: Boolean = false,
    val isDrop: Boolean = false,
)