package com.seif.banquemisrttask.ui.compose.repos_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.seif.banquemisrttask.R
import com.seif.banquemisrttask.domain.model.TrendingRepository

@OptIn(ExperimentalCoilApi::class)
@Composable
fun RepoItemExpandable(
    repositories: TrendingRepository,
    itemPosition:Int,
    onItemClick:(Int)-> Unit
) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onItemClick(itemPosition)
        }
        .padding(top = 12.dp)
    ) {
        Image(
            painter = rememberImagePainter(
                data =  repositories.avatar,
                builder = {
                  //  error(R.drawable.image_placeholder)
                    crossfade(1000)
                }
            ),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .padding(top = 8.dp, start = 8.dp)
                .clip(RoundedCornerShape(80.dp))
        )
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = repositories.author)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = repositories.name)
            Spacer(modifier = Modifier.height(10.dp))

            if (repositories.isExpanded.value) {
                Text(text = repositories.description)
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Box( // circle
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(12.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(android.graphics.Color.parseColor(repositories.languageColor)))

                    )

                    Text(text = repositories.language, Modifier.padding(end = 5.dp))

                    Image( // star
                        painter = painterResource(id = R.drawable.ic_star_yellow_16),
                        contentDescription = "start icon",
                        Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )

                    Text(text = repositories.stars.toString(), Modifier.padding(end = 5.dp))

                    Image( // fork
                        painter = painterResource(id = R.drawable.ic_fork_black_16),
                        contentDescription = "fork icon",
                        Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )

                    Text(text = repositories.forks.toString(), Modifier.padding(end = 5.dp))
                }
            }
        }
    }
    Divider()
}