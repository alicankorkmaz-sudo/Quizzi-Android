package studio.astroturf.quizzi.ui.screen.game

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import showToast
import studio.astroturf.quizzi.domain.exceptionhandling.UiNotification
import studio.astroturf.quizzi.domain.model.GameFeedback
import studio.astroturf.quizzi.ui.screen.game.composables.gameover.GameOverContent
import studio.astroturf.quizzi.ui.screen.game.composables.lobby.LobbyContent
import studio.astroturf.quizzi.ui.screen.game.composables.paused.PausedContent
import studio.astroturf.quizzi.ui.screen.game.composables.round.GameRoundContent
import timber.log.Timber

private const val TAG = "GameScreen"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GameScreen(
    onNavigateToRooms: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val uiNotification by viewModel.notification.collectAsState()

    // Log state changes using a side effect
    LaunchedEffect(uiState) {
        Timber.tag(TAG).d("State is being changed to: $uiState")
    }

    LaunchedEffect(uiNotification) {
        when (uiNotification) {
            is UiNotification.Toast -> context.showToast((uiNotification as UiNotification.Toast).message)
            else -> {} // Handle other types of notifications if needed
        }
    }

    GameScreenContent(
        state = uiState,
        onNavigateToRooms = onNavigateToRooms,
        onSubmitAnswer = viewModel::submitAnswer,
        onSubmitFeedback = viewModel::submitFeedback,
        imageLoader = viewModel.imageLoader,
        modifier = modifier,
    )
}

@Composable
private fun GameScreenContent(
    state: GameUiState,
    onNavigateToRooms: () -> Unit,
    onSubmitAnswer: (Int) -> Unit,
    onSubmitFeedback: (GameFeedback) -> Unit,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        GameStateContent(
            currentState = state,
            onNavigateToRooms = onNavigateToRooms,
            onSubmitAnswer = onSubmitAnswer,
            onSubmitFeedback = onSubmitFeedback,
            imageLoader = imageLoader,
        )
    }
}

@Composable
private fun GameStateContent(
    currentState: GameUiState,
    onNavigateToRooms: () -> Unit,
    onSubmitAnswer: (Int) -> Unit,
    onSubmitFeedback: (GameFeedback) -> Unit,
    imageLoader: ImageLoader,
) {
    when {
        currentState is GameUiState.Lobby -> {
            LobbyContent(
                roomName = currentState.roomName,
                creator = currentState.creator,
                challenger = currentState.challenger,
                countdown = currentState.countdown?.timeRemainingInSeconds,
            )
        }

        currentState is GameUiState.RoundOn -> {
            GameRoundContent(
                state = currentState,
                onSubmitAnswer = onSubmitAnswer,
                imageLoader = imageLoader,
            )
        }

        currentState is GameUiState.GameOver -> {
            GameOverContent(
                state = currentState,
                onNavigateBack = onNavigateToRooms,
                onSubmitFeedback = onSubmitFeedback,
            )
        }

        currentState is GameUiState.Paused -> {
            PausedContent(
                reason = currentState.reason,
                onlinePlayers = currentState.onlinePlayers,
                onRetry = {},
            )
        }

//        currentState is GameUiState.RoundEnd -> {
//            RoundResultOverlay(
//                correctAnswerText = currentState.correctAnswerValue,
//                roundWinner = currentState.roundWinner,
//            )
//        }

        else -> LoadingIndicator()
    }
}

private enum class GameStateAnimationKey {
    IDLE,
    LOBBY,
    ROUND,
    GAME_OVER,
    PAUSED,
    ROUND_END,
}

private fun GameUiState.toAnimationKey(): GameStateAnimationKey =
    when (this) {
        is GameUiState.Idle -> GameStateAnimationKey.IDLE
        is GameUiState.Lobby -> GameStateAnimationKey.LOBBY
        is GameUiState.RoundOn -> GameStateAnimationKey.ROUND
        is GameUiState.GameOver -> GameStateAnimationKey.GAME_OVER
        is GameUiState.Paused -> GameStateAnimationKey.PAUSED
//        is GameUiState.RoundEnd -> GameStateAnimationKey.ROUND_END
    }

private fun getTransitionSpec(targetState: GameStateAnimationKey): ContentTransform =
    when (targetState) {
        GameStateAnimationKey.GAME_OVER ->
            (slideInVertically { height -> height } + fadeIn())
                .togetherWith(slideOutVertically { height -> -height } + fadeOut())

        else -> fadeIn() togetherWith fadeOut()
    }
