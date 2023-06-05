package com.mashup.twotoo.presenter.designsystem.component.toolbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.twotoo.presenter.R
import com.mashup.twotoo.presenter.designsystem.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoTooBackToolbar(
    onClickBackIcon: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    @DrawableRes backIconId: Int = R.drawable.back_arrow,
    actionIconButton: @Composable () -> Unit = {},
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title,
                    color = TwoTooTheme.color.mainBrown,
                    style = TwoTooTheme.typography.headLineNormal20,
                    textAlign = TextAlign.Center,
                )
            }
        },
        navigationIcon = {
            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.CenterStart) {
                IconButton(onClick = {
                    onClickBackIcon()
                }) {
                    Icon(
                        painter = painterResource(id = backIconId),
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            Row(
                Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                actionIconButton()
            }
        },
        modifier = modifier.then(Modifier.height(56.dp)),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TwoTooTheme.color.backgroundYellow,
        ),
    )
}

@Composable
@Preview
fun BackToolbarWithNavTitleAndActionPreview() {
    val moreIconButton: @Composable () -> Unit =
        {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.more),
                    contentDescription = null,
                )
            }
        }
    TwoTooBackToolbar(title = "title", onClickBackIcon = {}, actionIconButton = moreIconButton)
}

@Composable
@Preview
fun BackToolbarWithNavAndTitlePreview() {
    val moreIconButton: @Composable () -> Unit =
        {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.more),
                    contentDescription = null,
                )
            }
        }
    TwoTooBackToolbar(onClickBackIcon = {}, actionIconButton = moreIconButton)
}

@Composable
@Preview
fun TwoTooBackToolbar() {
    TwoTooBackToolbar(onClickBackIcon = {})
}
