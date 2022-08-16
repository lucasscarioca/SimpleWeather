package com.oak.simpleweather.screens.main

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.oak.simpleweather.data.DataOrException
import com.oak.simpleweather.model.Weather

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    ShowData(viewModel)
}

@Composable
fun ShowData(viewModel: MainViewModel) {

    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)) {
        value = viewModel.getWeatherData(city = "Seattle")
    }.value

    if (weatherData.loading == true){
        CircularProgressIndicator()
    } else if (weatherData.data != null) {
        Text(text = "MainScreen ${weatherData.data!!}")
    }

}