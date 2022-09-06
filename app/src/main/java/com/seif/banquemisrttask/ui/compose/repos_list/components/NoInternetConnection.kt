package com.seif.banquemisrttask.ui.compose.repos_list.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.seif.banquemisrttask.R
import com.seif.banquemisrttask.ui.compose.HomeViewModel
import com.seif.banquemisrttask.ui.compose.ui.theme.LightGreen
import com.seif.banquemisrttask.ui.compose.ui.theme.LightWhite

@Composable
fun NoInternetConnectionSection(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightWhite)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.nointernet_connection),
            contentDescription = "No internet connection image",
            Modifier
                .padding(top = 120.dp, bottom = 25.dp)
                .fillMaxWidth()
                .height(250.dp)
        )
        Text(
            text = "Something Went Wrong..",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Text(
            text = "An alien is probably blocking your signal",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(80.dp))

        OutlinedButton(
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
            border = BorderStroke(2.dp, LightGreen),
            modifier = Modifier
                .fillMaxWidth(0.8f),
            onClick = {
                homeViewModel.forceFetchingData()
            }
        ) {
            Text(
                text = "Retry",
                fontSize = 18.sp,
                color = LightGreen
            )
        }
    }
}
