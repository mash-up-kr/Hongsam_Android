package com.mashup.twotoo.presenter.designsystem.component.toolbar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashup.twotoo.presenter.R
import com.mashup.twotoo.presenter.designsystem.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainToolbar(
    text: String = stringResource(id = R.string.app_name),
    onClickHelpIcon: () -> Unit,
) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                Text(
                    text = text,
                    color = TwotooPink,
                    fontFamily = Font.Omyuda,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                )
            }
        },
        actions = {
            Row(
                Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { onClickHelpIcon() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.help),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
            }
        },
        modifier = Modifier.height(56.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TwotooBackground,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainToolbar() {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = TwotooPink,
                    fontFamily = Font.Omyuda,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                )
            }
        },
        modifier = Modifier.height(56.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TwotooBackground,
        ),
    )
}

@Composable
@Preview
fun mainToolbarWithNavAndActionsPreview() {
    MainToolbar("공주", {})
}

@Composable
@Preview
fun mainToolbar() {
    MainToolbar()
}
