package dev.carlosmuvi.kmptemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.carlosmuvi.common.model.Event
import dev.carlosmuvi.common.model.ReminderTime
import dev.carlosmuvi.common.model.UiState
import dev.carlosmuvi.common.viewmodel.EventCreatorViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventCreatorScreen(
    viewModel: EventCreatorViewModel = koinViewModel()
) {
    val eventState by viewModel.eventState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Text(
            text = "Event Creator",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Enter event details and AI will extract the information",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Input Text Field
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Event Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    viewModel.updateInputText(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = { Text("e.g., Team meeting tomorrow at 2pm in the conference room") },
                maxLines = 6
            )
        }

        // Parse Button
        Button(
            onClick = { viewModel.parseEvent() },
            enabled = inputText.isNotBlank() && eventState !is UiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (eventState is UiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Parsing...")
            } else {
                Text("Parse Event")
            }
        }

        // Event State Display
        when (val state = eventState) {
            is UiState.Success -> {
                EventCard(event = state.data)

                Button(
                    onClick = { viewModel.confirmEvent() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Add to Calendar")
                }
            }

            is UiState.Error -> {
                ErrorCard(
                    message = state.message,
                    onRetry = { viewModel.resetState() }
                )
            }

            else -> {}
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Parsed Event",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            HorizontalDivider()

            // Event Details
            EventDetailRow(
                icon = Icons.Default.Title,
                label = "Title",
                value = event.title
            )

            event.description?.let {
                EventDetailRow(
                    icon = Icons.Default.Description,
                    label = "Description",
                    value = it
                )
            }

            event.location?.let {
                EventDetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "Location",
                    value = it
                )
            }

            EventDetailRow(
                icon = Icons.Default.CalendarToday,
                label = "Start",
                value = formatDateTime(event.startDateTime)
            )

            EventDetailRow(
                icon = Icons.Default.CalendarToday,
                label = "End",
                value = formatDateTime(event.endDateTime)
            )

            if (event.allDay) {
                EventDetailRow(
                    icon = Icons.Default.WbSunny,
                    label = "All Day",
                    value = "Yes"
                )
            }
        }
    }
}

@Composable
fun EventDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ErrorCard(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(40.dp)
            )

            Text(
                text = "Error",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Try Again")
            }
        }
    }
}

private fun formatDateTime(dateTime: kotlinx.datetime.LocalDateTime): String {
    return "${dateTime.year}-${dateTime.monthNumber.toString().padStart(2, '0')}-${
        dateTime.dayOfMonth.toString().padStart(2, '0')
    } at ${dateTime.hour.toString().padStart(2, '0')}:${
        dateTime.minute.toString().padStart(2, '0')
    }"
}

private fun formatReminder(reminder: ReminderTime): String {
    return when (reminder) {
        ReminderTime.NONE -> "None"
        ReminderTime.AT_TIME -> "At event time"
        ReminderTime.FIVE_MINUTES -> "5 minutes before"
        ReminderTime.FIFTEEN_MINUTES -> "15 minutes before"
        ReminderTime.THIRTY_MINUTES -> "30 minutes before"
        ReminderTime.ONE_HOUR -> "1 hour before"
        ReminderTime.ONE_DAY -> "1 day before"
    }
}
