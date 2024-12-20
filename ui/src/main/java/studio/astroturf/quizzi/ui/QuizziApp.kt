package studio.astroturf.quizzi.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import studio.astroturf.quizzi.domain.exceptionhandling.UiNotification
import studio.astroturf.quizzi.ui.exceptionhandling.NotificationHandler
import studio.astroturf.quizzi.ui.navigation.QuizziNavGraph
import studio.astroturf.quizzi.ui.theme.QuizziTheme

@Composable
fun QuizziApp(
    modifier: Modifier = Modifier,
) {
    QuizziTheme {
        val navController = rememberNavController()
        var currentNotification by remember { mutableStateOf<UiNotification?>(null) }

        Box(modifier = modifier.fillMaxSize()) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                QuizziNavGraph(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                )
            }

            NotificationHandler(
                notification = currentNotification,
                onDismiss = { currentNotification = null },
            )
        }
    }
} 
