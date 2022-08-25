package com.oak.simpleweather.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.oak.simpleweather.R
import com.oak.simpleweather.model.Weather
import com.oak.simpleweather.model.WeatherItem
import com.oak.simpleweather.utils.formatDate
import com.oak.simpleweather.utils.formatDateTime
import com.oak.simpleweather.utils.formatDecimals


@Composable
fun CurrentWeather(weather: WeatherItem) {
    val imageUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}.png"

    // Date
    Text(
        text = formatDate(weather.dt),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSecondary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(6.dp)
    )

    Surface(
        modifier = Modifier
            .padding(4.dp)
            .size(200.dp),
        shape = CircleShape,
        color = Color(0xFFFFC400)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // API weather icon
            WeatherStateImage(imageUrl = imageUrl, imageSize = 80.dp)

            // Temperature
            Text(
                text = formatDecimals(weather.temp.day) + "º",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )

            // Weather
            Text(
                text = weather.weather[0].main,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
fun WeatherDetailRow(weather: WeatherItem) {
    val imageUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}.png"

    Surface(modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth(),
        shape = CircleShape,
        color = Color.White
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Day of the week
            Text(
                text = formatDate(weather.dt).split(",")[0],
                modifier = Modifier.padding(start = 5.dp)
            )

            WeatherStateImage(imageUrl = imageUrl, imageSize = 40.dp)
            // Highlighted weather description
            Surface(
                modifier = Modifier.padding(0.dp),
                shape = CircleShape,
                color = Color(0xFFFFC400)
            ) {
                Text(
                    text = weather.weather[0].description,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.caption
                )
            }
            // Max and min temperatures
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color.Blue.copy(alpha = 0.6f),
                    fontWeight = FontWeight.SemiBold
                )
                ) {
                    append(formatDecimals(weather.temp.max) + "º ")
                }
                withStyle(style = SpanStyle(
                    color = Color.Gray
                )
                ) {
                    append(formatDecimals(weather.temp.min) + "º")
                }
            })

        }

    }

}

@Composable
fun SunsetSunriseRow(weather: WeatherItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sunrise time
        Row {
            Image(
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "sunrise icon",
                modifier = Modifier.size(25.dp)
            )

            Text(
                text = formatDateTime(weather.sunrise),
                style = MaterialTheme.typography.caption
            )
        }
        // Sunset time
        Row {
            Text(
                text = formatDateTime(weather.sunset),
                style = MaterialTheme.typography.caption
            )

            Image(
                painter = painterResource(id = R.drawable.sunset),
                contentDescription = "sunset icon",
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
fun HumidityWindPressureRow(weather: WeatherItem, isMetric: Boolean) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Humidity
        Row(
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.humidity),
                contentDescription = "humidity icon",
                modifier = Modifier.size(20.dp)
            )
            Text(text = "${weather.humidity}%", style = MaterialTheme.typography.caption)
        }
        // Pressure
        Row(
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.pressure),
                contentDescription = "pressure icon",
                modifier = Modifier.size(20.dp)
            )
            Text(text = "${weather.pressure} psi", style = MaterialTheme.typography.caption)
        }
        // Wind
        Row(
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wind),
                contentDescription = "wind icon",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "${formatDecimals(weather.speed)} " + if (isMetric) "m/s" else "mph",
                style = MaterialTheme.typography.caption
            )
        }

    }
}

@Composable
fun WeekWeatherSummary(data: Weather) {
    Text(
        text = "This Week",
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.Bold
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = Color(0xFFEEF1EF),
        shape = RoundedCornerShape(size = 14.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(2.dp),
            contentPadding = PaddingValues(1.dp)
        ) {
            items(items = data.list) { item: WeatherItem ->
                WeatherDetailRow(weather = item)
            }
        }
    }
}


@Composable
fun WeatherStateImage(imageUrl: String, imageSize: Dp) {
    Image(
        painter = rememberAsyncImagePainter(model = imageUrl),
        contentDescription = "icon image",
        modifier = Modifier.size(imageSize)
    )
}