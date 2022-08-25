package com.oak.simpleweather.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.oak.simpleweather.data.DataOrException
import com.oak.simpleweather.model.Weather
import com.oak.simpleweather.navigation.WeatherScreens
import com.oak.simpleweather.screens.settings.SettingsViewModel
import com.oak.simpleweather.widgets.*

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    city: String?
) {

    val curCity: String = if (city!!.isBlank()) "Seattle" else city
    val unitFromDb = settingsViewModel.unitsList.collectAsState().value
    var unit by remember {
        mutableStateOf("metric")
    }

    var isMetric by remember {
        mutableStateOf(false)
    }

    if (!unitFromDb.isEmpty()){
        unit = unitFromDb[0].unit.split(" ")[0].lowercase()
        isMetric = unit == "metric"
        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)) {
            value = viewModel.getWeatherData(city = curCity, units = unit)
        }.value

        if (weatherData.loading == true){
            CircularProgressIndicator()
        } else if (weatherData.data != null) {
            MainScaffold(weather = weatherData.data!!, navController, isMetric = isMetric)
        }
    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScaffold(weather: Weather, navController: NavController, isMetric: Boolean) {
    
    Scaffold(topBar = {
        WeatherAppBar(
            title = weather.city.name + ", ${weather.city.country}",
            navController = navController,
            onAddActionClicked = {
                                 navController.navigate(WeatherScreens.SearchScreen.name)
            },
            elevation = 5.dp
        ) {

        }
    }) {
        MainContent(data = weather, isMetric = isMetric)
    }
}

@Composable
fun MainContent(data: Weather, isMetric: Boolean) {
    val weatherItem = data.list[0]

    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CurrentWeather(weather = weatherItem)

        HumidityWindPressureRow(weather = weatherItem, isMetric = isMetric)

        Divider()

        SunsetSunriseRow(weather = weatherItem)

        WeekWeatherSummary(data = data)
    }
}