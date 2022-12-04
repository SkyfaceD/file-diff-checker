import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import ui.App
import ui.dropTarget.DropTargetWrapper
import ui.dropTarget.LocalDropTargetWrapper
import ui.icon.Icons
import ui.theme.AppTheme

fun main() = application {

    Window(
        state = rememberWindowState(position = WindowPosition(Alignment.Center), width = 400.dp, height = 400.dp),
        title = "File Diff Checker",
        undecorated = true,
        resizable = false,
        icon = painterResource(Icons.AppLogo),
        onCloseRequest = ::exitApplication,
    ) {
        val dropTargetWrapper = DropTargetWrapper()
        window.contentPane.dropTarget = dropTargetWrapper.target

        CompositionLocalProvider(
            LocalDropTargetWrapper provides dropTargetWrapper
        ) {
            AppTheme(true) {
                Scaffold(
                    topBar = {
                        AppTopAppBar(onCloseClick = this@application::exitApplication)
                    }
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
private fun FrameWindowScope.AppTopAppBar(
    onCloseClick: () -> Unit,
) {
    var pX by remember { mutableStateOf(0) }
    var pY by remember { mutableStateOf(0) }

    Surface(
        color = MaterialTheme.colors.primarySurface,
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.height(30.dp).pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        pX = it.x.toInt()
                        pY = it.y.toInt()
                    },
                    onDrag = { change, _ ->
                        window.setLocation(
                            (window.location.x + change.position.x).toInt() - pX,
                            (window.location.y + change.position.y).toInt() - pY
                        )
                    }
                )
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(8.dp))

            Text(
                text = "File Diff Checker",
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

            MenuButton(
                modifier = Modifier.fillMaxHeight(),
                icon = painterResource(Icons.Minus),
                onClick = { window.isMinimized = true }
            )

            MenuButton(
                modifier = Modifier.fillMaxHeight(),
                icon = painterResource(Icons.Close),
                onClick = onCloseClick,
                tint = MaterialTheme.colors.error
            )
        }
    }
}

@Composable
private fun MenuButton(
    icon: Painter,
    modifier: Modifier = Modifier,
    tint: Color = MenuButtonIconTint,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier.defaultMinSize(1.dp, 1.dp).padding(horizontal = 1.dp),
        onClick = onClick,
        shape = RectangleShape,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.textButtonColors(contentColor = tint)
    ) {
        Icon(icon, contentDescription)
    }
}

private val MenuButtonIconTint
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black
