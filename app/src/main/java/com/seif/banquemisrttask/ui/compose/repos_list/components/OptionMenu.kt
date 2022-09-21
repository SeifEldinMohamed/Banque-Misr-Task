package com.seif.banquemisrttask.ui.compose.repos_list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.seif.banquemisrttask.ui.compose.HomeViewModel

@Composable
fun OptionMenu(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                "Trending",
                textAlign = TextAlign.Center,
                color = Color.Black,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.fillMaxWidth()
            )
        },
        backgroundColor = Color.White,
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.MoreVert, "menu")
            }

            DropdownMenu(
                expanded = showMenu, // Whether the menu is currently open and visible to the user
                onDismissRequest = { showMenu = false } // Called when the user requests to dismiss the menu, such as by tapping outside the menu's bounds
            ) {
                DropdownMenuItem(onClick = {
                    if (homeViewModel.state.value.error.isBlank()) // when we are not in error state
                        homeViewModel.sortReposByName()
                    showMenu = false
                }
                ) {
                    Text(text = "Sort By Name", Modifier.padding(horizontal = 10.dp))
                }
                DropdownMenuItem(onClick = {
                    if (homeViewModel.state.value.error.isBlank()) // when we are not in error state
                    homeViewModel.sortReposBStars()
                    showMenu = false
                }) {
                    Text(text = "Sort By Stars", Modifier.padding(horizontal = 10.dp))
                }
            }
        }
    )
}
