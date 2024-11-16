
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alicankorkmaz.quizzi.domain.model.GameRoom
import com.alicankorkmaz.quizzi.domain.model.RoomState
import com.alicankorkmaz.quizzi.ui.screen.rooms.RoomsEvent
import com.alicankorkmaz.quizzi.ui.screen.rooms.RoomsViewModel
import com.alicankorkmaz.quizzi.ui.util.observeWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsScreen(
    viewModel: RoomsViewModel = hiltViewModel(),
    onNavigateToRoom: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    // Collect events
    viewModel.eventChannel.observeWithLifecycle { event ->
        when (event) {
            is RoomsEvent.NavigateToRoom -> onNavigateToRoom()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Available Rooms") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh rooms"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            RoomsScreenContent(
                modifier = Modifier,
                rooms = uiState.rooms,
                isConnected = uiState.isConnected,
                error = uiState.error,
                onCreateRoom = { viewModel.createRoom() },
                onJoinRoom = { roomId ->
                    viewModel.joinRoom(roomId)
                    onNavigateToRoom()
                }
            )
        }
    }
}

@Composable
private fun RoomsScreenContent(
    modifier: Modifier = Modifier,
    rooms: List<GameRoom>,
    isConnected: Boolean,
    error: String?,
    onCreateRoom: () -> Unit,
    onJoinRoom: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Available Rooms",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (!isConnected) {
            Text(
                text = "Connecting...",
                color = MaterialTheme.colorScheme.secondary
            )
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = onCreateRoom,
            modifier = Modifier.fillMaxWidth(),
            enabled = isConnected
        ) {
            Text("Create New Room")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(rooms) { room ->
                RoomItem(
                    room = room,
                    onJoinRoom = { onJoinRoom(room.id) }
                )
            }
        }
    }
}


@Composable
private fun RoomItem(
    room: GameRoom,
    onJoinRoom: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.7f)
            ) {
                Text(
                    text = "Room #${room.id}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = "${room.playerCount}/4 Players",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Text(
                    text = "Status: ${room.roomState.name}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }

            Button(
                onClick = onJoinRoom,
                enabled = room.roomState == RoomState.WAITING && room.playerCount < 4
            ) {
                Text("Join")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun RoomsScreenContentPreview() {
    MaterialTheme {
        RoomsScreenContent(
            rooms = listOf(
                GameRoom(
                    id = "Room1",
                    playerCount = 2,
                    roomState = RoomState.WAITING,
                    players = listOf("Player1", "Player2")
                ),
                GameRoom(
                    id = "Room2",
                    playerCount = 3,
                    roomState = RoomState.PLAYING,
                    players = listOf("Player3", "Player4", "Player5")
                )
            ),
            isConnected = true,
            error = null,
            onCreateRoom = {},
            onJoinRoom = {}
        )
    }
}
