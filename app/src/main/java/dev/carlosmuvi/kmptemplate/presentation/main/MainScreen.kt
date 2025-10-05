package dev.carlosmuvi.kmptemplate.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.carlosmuvi.common.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val counter by viewModel.counter.collectAsState()
    val message by viewModel.message.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "Counter: $counter",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = { viewModel.decrementCounter() }) {
                    Text("-")
                }

                Button(onClick = { viewModel.incrementCounter() }) {
                    Text("+")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "This is a Kotlin Multiplatform template with:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            listOf(
                "✓ Koin for dependency injection",
                "✓ ViewModel architecture",
                "✓ Jetpack Compose (Android)",
                "✓ SwiftUI (iOS)"
            ).forEach { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
