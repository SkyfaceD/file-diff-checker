package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.dropTarget.LocalDropTargetWrapper

@Composable
fun App() {
    val dropTargetWrapper = LocalDropTargetWrapper.current

    val viewModel = remember { AppViewModel(dropTargetWrapper) }
    val state = viewModel.state

    val primaryColor = MaterialTheme.colors.primary

    Box(
        modifier = Modifier.fillMaxSize(1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp).then(
                if (state.isDrop) {
                    val stroke = Stroke(
                        2f,
                        Stroke.DefaultMiter,
                        StrokeCap.Round,
                        StrokeJoin.Round,
                        PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    Modifier.drawBehind {
                        drawRoundRect(
                            color = primaryColor,
                            style = stroke,
                            cornerRadius = CornerRadius(10f, 10f)
                        )
                    }
                } else Modifier
            ).padding(8.dp),
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.inPath,
                onValueChange = viewModel::changeInPath,
                label = { Text("In Path") },
                enabled = !state.isLoading
            )

            Spacer(Modifier.height(12.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.outPath,
                onValueChange = viewModel::changeOutPath,
                label = { Text("Out Path") },
                enabled = !state.isLoading
            )

            Spacer(Modifier.weight(1f))

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = viewModel::difference,
                enabled = !state.isLoading
            ) {
                Text("Proceed")
            }

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = viewModel::move,
                enabled = !state.isLoading && state.isDifferent
            ) {
                Text("Move")
            }
        }

        if (state.isDrop) {
            Text(
                text = "Drop File",
                fontWeight = FontWeight.Bold
            )
        }
    }
}