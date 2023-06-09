package com.mashup.twotoo.presenter.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.twotoo.presenter.R
import com.mashup.twotoo.presenter.designsystem.component.TwoTooImageView
import com.mashup.twotoo.presenter.designsystem.theme.TwoTooTheme
import com.mashup.twotoo.presenter.home.model.HomeGoalCountData

@Composable
fun HomeGoalCount(
    homeGoalCountData: HomeGoalCountData,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides TwoTooTheme.typography.bodyNormal14,
            LocalContentColor provides TwoTooTheme.color.mainPink,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                Text(homeGoalCountData.partnerName)
                TwoTooImageView(
                    modifier = Modifier.width(10.5.dp).height(9.16.dp),
                    model = R.drawable.ic_heart,
                    previewPlaceholder = R.drawable.ic_heart,
                )
                Text(homeGoalCountData.myName)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "${homeGoalCountData.count}번째 꽃 피우는 중")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGoalCount() {
    TwoTooTheme {
        HomeGoalCount(
            homeGoalCountData = HomeGoalCountData(
                partnerName = "공주",
                myName = "나",
                count = 1,
            ),
        )
    }
}