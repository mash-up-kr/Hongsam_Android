package com.mashup.twotoo.presenter.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.constraintlayout.compose.ConstraintLayout
import com.mashup.twotoo.presenter.R
import com.mashup.twotoo.presenter.designsystem.component.TwoTooImageView
import com.mashup.twotoo.presenter.designsystem.component.button.TwoTooTextButton
import com.mashup.twotoo.presenter.designsystem.theme.TwoTooTheme
import com.mashup.twotoo.presenter.home.model.BeforeChallengeUiModel
import com.mashup.twotoo.presenter.home.model.StateTitleUiModel

/**
 * @Created by 김현국 2023/06/10
 */

@Composable
fun HomeBeforeChallenge(
    beforeChallengeUiModel: BeforeChallengeUiModel,
    modifier: Modifier = Modifier,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (homeGoalCount, homeBeforeChallengeTitle, homeBeforeChallengeImage, textButton, homeBackground) = createRefs()
        HomeGoalCount(
            modifier = Modifier.constrainAs(homeGoalCount) {
                top.linkTo(parent.top, margin = 11.dp)
                end.linkTo(parent.end, margin = 22.dp)
            },
            homeGoalCountUiModel = beforeChallengeUiModel.homeGoalCountUiModel,
        )
        HomeBeforeChallengeTitle(
            modifier = Modifier.constrainAs(homeBeforeChallengeTitle) {
                top.linkTo(parent.top, margin = screenHeight * 0.33f.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            stateTitle = beforeChallengeUiModel.stateTitleUiModel,
        )
        HomeBeforeChallengeImage(
            modifier = Modifier
                .constrainAs(homeBeforeChallengeImage) {
                    top.linkTo(homeBeforeChallengeTitle.bottom, margin = 11.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.width(93.dp)
                .height(109.dp),
            stateImage = beforeChallengeUiModel.stateImage,
        )
        TwoTooImageView(
            modifier = Modifier.fillMaxWidth().height(244.dp).constrainAs(homeBackground) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            previewPlaceholder = R.drawable.image_home_background,
            model = R.drawable.image_home_background,
        )

        TwoTooTextButton(
            modifier = Modifier.width(177.dp).height(57.dp).constrainAs(textButton) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 51.dp)
            },
            text = stringResource(id = beforeChallengeUiModel.buttonText),
        )
    }
}

@Composable
fun HomeBeforeChallengeTitle(
    stateTitle: StateTitleUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        Text(
            text = stringResource(id = stateTitle.title),
            style = TwoTooTheme.typography.headLineNormal28,
            color = TwoTooTheme.color.mainBrown,
            textAlign = TextAlign.Center,
        )
        stateTitle.subTitle?.let {
            Text(
                text = stringResource(id = stateTitle.subTitle),
                style = TwoTooTheme.typography.bodyNormal14,
                color = TwoTooTheme.color.gray600,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun HomeBeforeChallengeImage(
    @DrawableRes stateImage: Int,
    modifier: Modifier = Modifier,
) {
    TwoTooImageView(
        modifier = modifier,
        model = stateImage,
        previewPlaceholder = stateImage,
    )
}

@Preview(showBackground = true, device = "id:pixel_2")
@Composable
private fun PreviewHomeBeforeEmptyChallenge() {
    TwoTooTheme {
        HomeBeforeChallenge(
            modifier = Modifier.fillMaxSize(),
            beforeChallengeUiModel = BeforeChallengeUiModel.empty,
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_2")
@Composable
private fun PreviewHomeBeforeRequestChallenge() {
    TwoTooTheme {
        HomeBeforeChallenge(
            modifier = Modifier.fillMaxSize(),
            beforeChallengeUiModel = BeforeChallengeUiModel.request,
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_2")
@Composable
private fun PreviewHomeBeforeResponseChallenge() {
    TwoTooTheme {
        HomeBeforeChallenge(
            modifier = Modifier.fillMaxSize(),
            beforeChallengeUiModel = BeforeChallengeUiModel.response,
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_2")
@Composable
private fun PreviewHomeBeforeWaitChallenge() {
    TwoTooTheme {
        HomeBeforeChallenge(
            modifier = Modifier.fillMaxSize(),
            beforeChallengeUiModel = BeforeChallengeUiModel.wait,
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_2")
@Composable
private fun PreviewHomeBeforeTerminationChallenge() {
    TwoTooTheme {
        HomeBeforeChallenge(
            modifier = Modifier.fillMaxSize(),
            beforeChallengeUiModel = BeforeChallengeUiModel.termination,
        )
    }
}
