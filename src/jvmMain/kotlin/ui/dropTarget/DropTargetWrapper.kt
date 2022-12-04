package ui.dropTarget

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.*
import java.io.File

class DropTargetWrapper {
    private val _state = MutableStateFlow<DropTargetState?>(null)
    val state = _state.asStateFlow()

    val target = object : DropTarget() {
        override fun dragEnter(event: DropTargetDragEvent?) {
            if (event == null) return

            _state.update { DropTargetState.DragEnter }
        }

        override fun dragExit(event: DropTargetEvent?) {
            if (event == null) return

            _state.update { DropTargetState.DragExit }
        }

        @Suppress("UNCHECKED_CAST")
        @Synchronized
        override fun drop(event: DropTargetDropEvent?) {
            if (event == null) return _state.update { DropTargetState.Failure("Event not registered", null) }

            try {
                event.acceptDrop(DnDConstants.ACTION_REFERENCE)
                val droppedFiles = event.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                _state.update { DropTargetState.Success(droppedFiles) }
            } catch (cause: Throwable) {
                _state.update { DropTargetState.Failure("", cause) }
            }
        }
    }
}

val LocalDropTargetWrapper = compositionLocalOf<DropTargetWrapper> { error("Not provided") }