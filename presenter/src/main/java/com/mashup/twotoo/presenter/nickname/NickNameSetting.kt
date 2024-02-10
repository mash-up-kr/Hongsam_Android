package com.mashup.twotoo.presenter.nickname

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.twotoo.presenter.R
import com.mashup.twotoo.presenter.constant.TAG
import com.mashup.twotoo.presenter.designsystem.component.TwoTooImageView
import com.mashup.twotoo.presenter.designsystem.component.button.TwoTooTextButton
import com.mashup.twotoo.presenter.designsystem.component.textfield.TwoTooTextField
import com.mashup.twotoo.presenter.designsystem.component.toast.SnackBarHost
import com.mashup.twotoo.presenter.designsystem.component.toolbar.TwoTooToolbar
import com.mashup.twotoo.presenter.designsystem.theme.MainYellow
import com.mashup.twotoo.presenter.designsystem.theme.TwoTooTheme
import com.mashup.twotoo.presenter.designsystem.theme.TwotooPink
import com.mashup.twotoo.presenter.navigation.NavigationRoute
import com.mashup.twotoo.presenter.util.checkInviteLink
import com.mashup.twotoo.presenter.util.findActivity
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun NickNameSettingRoute(
    nickNameViewModel: NickNameViewModel,
    startRoute: String,
    onSettingSuccess: (String) -> Unit,
) {
    val state by nickNameViewModel.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        checkInviteLink(
            true,
            context.findActivity().intent,
            partnerInfo = { nickname, partnerNo ->
                Log.d(TAG, "checkInviteLink: $nickname")
                Log.d(TAG, "checkInviteLink: $partnerNo")
                nickNameViewModel.setPartnerInfo(nickname, partnerNo)
            },
            error = { isFail ->
                isFail?.let { error ->
                    if (error) {
                        nickNameViewModel.toastMessage(context.getString(R.string.toast_message_deeplink_error))
                    }
                }
            },
        )
    }

    NickNameSetting(
        state,
        snackState,
        startRoute,
        onNextButtonClick = { nickName ->
            if (startRoute.isNotEmpty() && startRoute == "mypage") {
                nickNameViewModel.changeNickname(nickName)
            } else {
                nickNameViewModel.setUserNickName(nickName)
            }
        },
        onClickBackButton = { onSettingSuccess("mypage") },
    )

    nickNameViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is NickNameSideEffect.NavigateToHome -> {
                onSettingSuccess(NavigationRoute.HomeGraph.HomeScreen.route)
            }
            is NickNameSideEffect.NavigateToSendInvitation -> {
                onSettingSuccess(NavigationRoute.InvitationGraph.SendInvitationScreen.route)
            }
            is NickNameSideEffect.NavigateToMyPage -> {
                onSettingSuccess(NavigationRoute.UserGraph.UserScreen.route)
            }
            is NickNameSideEffect.ToastMessage -> {
                coroutineScope.launch {
                    snackState.showSnackbar(sideEffect.msg)
                }
            }
        }
    }
}

@Composable
fun NickNameSetting(
    state: NickNameState,
    snackState: SnackbarHostState,
    startRoute: String,
    onNextButtonClick: (String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    val isChangeMode = startRoute.isNotEmpty() && startRoute == "mypage"
    val buttonText =
        if (isChangeMode) {
            R.string.nickname_change_button
        } else {
            R.string.button_confirm
        }
    val desc =
        if (isChangeMode) {
            R.string.nickname_change
        } else {
            R.string.nickname_setting
        }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize().navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            if (isChangeMode) {
                TwoTooToolbar.BackToolbar(
                    modifier = Modifier.fillMaxWidth(),
                    onClickBackIcon = { onClickBackButton() },
                    onClickActionButton = {},
                    actionIcons = null,
                )
            } else {
                TwoTooToolbar.TitleToolbar(modifier = Modifier.fillMaxWidth())
            }
            if (state.partnerNickName.isNotEmpty() && !isChangeMode) {
                TwoTooImageView(
                    modifier = Modifier.size(97.dp, 85.dp),
                    previewPlaceholder = R.drawable.img_nickname_mate,
                    model = R.drawable.img_nickname_mate,
                    contentScale = ContentScale.Crop,
                )
                InviteGuide(state.partnerNickName)
            } else {
                TwoTooImageView(
                    modifier = Modifier.size(149.dp, 129.dp),
                    previewPlaceholder = R.drawable.img_nicknam_my,
                    model = R.drawable.img_nicknam_my,
                )
            }
            Text(
                modifier = Modifier.padding(top = 78.dp),
                text = stringResource(id = desc),
                textAlign = TextAlign.Center,
                style = TwoTooTheme.typography.headLineNormal28,
                color = TwoTooTheme.color.mainBrown,
            )
            var nickName by remember { mutableStateOf("") }
            InputUserNickName(nickName, onTextValueChanged = { nickName = it })
            Spacer(modifier = Modifier.weight(1f))
            TwoTooTextButton(
                text = stringResource(id = buttonText),
                enabled = nickName.isNotEmpty(),
            ) {
                onNextButtonClick(nickName)
            }
            Spacer(modifier = Modifier.height(54.dp))
        }
        SnackBarHost(
            Modifier.align(Alignment.BottomCenter).padding(bottom = 54.dp),
            snackState,
        )
    }
}

@Composable
fun InputUserNickName(
    nickName: String,
    onTextValueChanged: (String) -> Unit,
) {
    val nickNameMaxLength = 4

    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp),
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(id = R.string.nickname),
            style = TwoTooTheme.typography.bodyNormal16,
            color = TwoTooTheme.color.mainBrown,
        )

        TwoTooTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            text = nickName,
            textHint = stringResource(id = R.string.nickname_setting_hint),
            updateText = { text ->
                if (text.length <= nickNameMaxLength) onTextValueChanged(text)
            },
        )
    }
}

@Composable
fun InviteGuide(partnerNickName: String) {
    Text(
        modifier = Modifier
            .padding(top = 27.dp)
            .drawBehind {
                drawRoundRect(
                    color = MainYellow,
                    cornerRadius = CornerRadius(10.dp.toPx()),
                )
            }
            .padding(10.dp),
        textAlign = TextAlign.Center,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = TwoTooTheme.typography.bodyNormal16.fontFamily,
                    color = TwotooPink,
                ),
            ) {
                append(stringResource(id = R.string.other, partnerNickName))
            }
            withStyle(
                style = SpanStyle(
                    fontFamily = TwoTooTheme.typography.bodyNormal16.fontFamily,
                    color = TwoTooTheme.color.mainBrown,
                ),
            ) {
                append(stringResource(id = R.string.invite_someone))
            }
        },
    )
}

@Preview
@Composable
private fun InviteGuidePreview() {
    InviteGuide("공주")
}

@Preview
@Composable
private fun NickNameSettingPreview() {
    NickNameSetting(NickNameState(), SnackbarHostState(), "", {}, {})
}

@Preview
@Composable
private fun NickNameSettingIsChangePreview() {
    NickNameSetting(
        NickNameState(),
        SnackbarHostState(),
        "mypage",
        {},
        {},
    )
}
