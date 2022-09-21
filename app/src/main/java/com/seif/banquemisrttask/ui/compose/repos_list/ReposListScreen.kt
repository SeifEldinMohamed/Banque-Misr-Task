package com.seif.banquemisrttask.ui.compose.repos_list

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import com.seif.banquemisrttask.ui.compose.repos_list.components.*

@Composable
fun ReposListScreen() {
    Column {
        OptionMenu()
        SwipeRefreshCompose()
    }
}







