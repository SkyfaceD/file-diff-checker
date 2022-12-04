package ui.dropTarget

import java.io.File

sealed class DropTargetState {
    data class Success(val files: List<File>) : DropTargetState()

    data class Failure(val message: String? = null, val cause: Throwable? = null) : DropTargetState()

    object DragEnter : DropTargetState()

    object DragExit : DropTargetState()

    override fun toString(): String {
        return when (this) {
            DragEnter -> "DragEnter"
            DragExit -> "DragExit"
            is Failure -> "Failure"
            is Success -> "Success"
        }
    }
}