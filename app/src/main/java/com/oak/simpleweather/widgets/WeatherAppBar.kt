package com.oak.simpleweather.widgets

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.oak.simpleweather.model.Favorite
import com.oak.simpleweather.navigation.WeatherScreens
import com.oak.simpleweather.screens.favorites.FavoriteViewModel

@Composable
fun WeatherAppBar(
    title: String = "City",
    icon: ImageVector? = null,
    isMainScreen: Boolean = true,
    elevation: Dp = 0.dp,
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    onAddActionClicked: () -> Unit = {},
    onButtonClicked: () -> Unit = {}
) {

    val showDialog = remember {
        mutableStateOf(false)
    }

    val showIt = remember {
        mutableStateOf(false)
    }

    val toastText = remember {
        mutableStateOf("Favorites updated")
    }

    val context = LocalContext.current

    if (showDialog.value) {
        ShowSettingDropDownMenu(showDialog = showDialog, navController = navController)
    }
    
    TopAppBar(
        title = { Text(
            text = title,
            color = MaterialTheme.colors.onSecondary,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
        ) },
        actions = {
                  if (isMainScreen){
                      IconButton(onClick = {
                          onAddActionClicked.invoke()
                      }) {
                          Icon(
                              imageVector = Icons.Default.Search,
                              contentDescription = "Search Icon"
                          )
                      }
                      IconButton(onClick = {
                          showDialog.value = true
                      }) {
                          Icon(
                              imageVector = Icons.Rounded.MoreVert,
                              contentDescription = "More Icon"
                          )
                      }
                  } else Box {}
        },
        navigationIcon = {
                 if (icon != null) {
                     Icon(
                         imageVector = icon,
                         contentDescription = null,
                         tint = MaterialTheme.colors.onSecondary,
                         modifier = Modifier.clickable {
                             onButtonClicked.invoke()
                         }
                     )
                 }
                if (isMainScreen) {
                    val isAlreadyFavList = favoriteViewModel
                        .favList.collectAsState().value?.filter { item ->
                            (item.city == title.split(",")[0])
                        }
                    // City is not favorite
                    if (isAlreadyFavList.isNullOrEmpty()) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite icon",
                            modifier = Modifier
                                .scale(0.9f)
                                .padding(start = 10.dp)
                                .clickable {
                                    val locationList = title.split(",")
                                    favoriteViewModel.insertFavorite(
                                        Favorite(
                                            city = locationList[0],
                                            country = locationList[1]
                                        )
                                    ).run {
                                        showIt.value = true
                                        toastText.value = "Added to favorites"
                                    }
                                }
                        )
                    // City is favorite
                    } else {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite icon",
                            modifier = Modifier
                                .scale(0.9f)
                                .padding(start = 10.dp)
                                .clickable {
                                    val favCity = favoriteViewModel
                                        .favList.value?.find {
                                        (it.city == title.split(",")[0])
                                    }
                                    if (favCity != null) {
                                        favoriteViewModel.deleteFavorite(favCity).run {
                                            showIt.value = true
                                            toastText.value = "Removed from favorites"
                                        }
                                    }
                                },
                            tint = Color.Red.copy(alpha = 0.6f)
                        )
                    }

                    ShowToast(context = context, showIt, toastText)


                }
        },
        backgroundColor = Color.Transparent,
        elevation = elevation
    )
    
}

@Composable
fun ShowToast(context: Context, showIt: MutableState<Boolean>, toastText: MutableState<String>) {
    if (showIt.value) {
        Toast.makeText(context,toastText.value, Toast.LENGTH_SHORT).show()
        showIt.value = false
    }
}

@Composable
fun ShowSettingDropDownMenu(showDialog: MutableState<Boolean>, navController: NavController) {

    var expanded by remember { mutableStateOf(true) }
    val items = listOf("About", "Favorites", "Settings")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .absolutePadding(top = 45.dp, right = 20.dp)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                showDialog.value = false
            },
            modifier = Modifier
                .width(140.dp)
                .background(Color.White)
        ) {
            items.forEachIndexed { index, text ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    showDialog.value = false
                }) {
                    Icon(
                        imageVector = when (text){
                            "About" -> Icons.Default.Info
                            "Favorites" -> Icons.Default.FavoriteBorder
                            "Settings" -> Icons.Default.Settings
                            else -> Icons.Default.Settings },
                        contentDescription = null,
                        tint = Color.LightGray
                    )
                    Text(
                        text = text,
                        modifier = Modifier.clickable {
                                  navController.navigate(
                                      when (text){
                                          "About" -> WeatherScreens.AboutScreen.name
                                          "Favorites" -> WeatherScreens.FavoritesScreen.name
                                          "Settings" -> WeatherScreens.SettingsScreen.name
                                          else -> WeatherScreens.AboutScreen.name
                                      })
                        },
                        fontWeight = FontWeight.W300
                    )
                }
            }
        }
    }
}
