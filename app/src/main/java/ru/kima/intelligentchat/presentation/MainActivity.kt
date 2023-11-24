package ru.kima.intelligentchat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.kima.intelligentchat.presentation.cardDetails.CardDetailsScreen
import ru.kima.intelligentchat.presentation.charactersList.CharactersListScreen
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntelligentChatTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "cards") {
                    composable("cards") { CharactersListScreen(navController) }
                    composable(
                        route = "cards/{cardId}",
                        arguments = listOf(
                            navArgument(name = "cardId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) { backStackEntry ->
                        val cardId = backStackEntry.arguments?.getInt("cardId") ?: -1
                        CardDetailsScreen(cardId = cardId, navController = navController)
                    }
                }
            }
        }
    }
}
