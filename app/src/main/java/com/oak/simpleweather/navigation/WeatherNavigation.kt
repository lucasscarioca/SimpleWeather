package com.oak.simpleweather.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.oak.simpleweather.screens.about.AboutScreen
import com.oak.simpleweather.screens.favorites.FavoritesScreen
import com.oak.simpleweather.screens.main.MainScreen
import com.oak.simpleweather.screens.main.MainViewModel
import com.oak.simpleweather.screens.search.SearchScreen
import com.oak.simpleweather.screens.settings.SettingsScreen
import com.oak.simpleweather.screens.splash.WeatherSplashScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name) {
        composable(WeatherScreens.SplashScreen.name){
            WeatherSplashScreen(navController = navController)
        }

        val route = WeatherScreens.MainScreen.name

        composable(
            "$route/{city}",
            arguments = listOf(
                navArgument(name = "city") { type = NavType.StringType }
            )
        ){ navBack ->
            navBack.arguments?.getString("city").let { city ->
                val mainViewModel = hiltViewModel<MainViewModel>()
                MainScreen(navController = navController, viewModel = mainViewModel, city = city)
            }
        }

        composable(WeatherScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }

        composable(WeatherScreens.AboutScreen.name){
            AboutScreen(navController = navController)
        }

        composable(WeatherScreens.SettingsScreen.name){
            SettingsScreen(navController = navController)
        }

        composable(WeatherScreens.FavoritesScreen.name){
            FavoritesScreen(navController = navController)
        }
    }
}