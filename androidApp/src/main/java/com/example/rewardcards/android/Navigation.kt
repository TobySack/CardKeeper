package com.example.rewardcards.android

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rewardcards.android.cardAdd.CardAddScreen
import com.example.rewardcards.android.cardDetail.CardDetailScreen
import com.example.rewardcards.android.cardEdit.CardEditScreen
import com.example.rewardcards.android.cardList.CardListScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.CardList.route) {
        composable(route = Screen.CardList.route) {
            CardListScreen(navController = navController)
        }
        composable(route = Screen.CardAdd.route) {
            CardAddScreen(navController = navController)
        }
        composable(
            route = Screen.CardEdit.route + "/{cardId}",
            arguments = listOf(
                navArgument(name = "cardId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getLong("cardId") ?: -1L
            CardEditScreen(cardId = cardId, navController = navController)
        }
        composable(
            route = Screen.CardDetail.route + "/{cardId}",
            arguments = listOf(
                navArgument(name = "cardId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getLong("cardId") ?: -1L
            CardDetailScreen(cardId = cardId, navController = navController)
        }
    }
}