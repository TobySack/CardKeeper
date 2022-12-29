package com.example.rewardcards.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.composable
import androidx.compose.material.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.rewardcards.android.cardAdd.CardAddScreen
import com.example.rewardcards.android.cardList.CardListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "cardList") {
                    composable(route = "cardList") {
                        CardListScreen(navController = navController)
                    }
                    composable(route = "cardAdd") {
                        CardAddScreen(navController = navController)
                    }
                }
            }
        }
    }
}
