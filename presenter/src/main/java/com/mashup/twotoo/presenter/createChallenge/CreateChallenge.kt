package com.mashup.twotoo.presenter.createChallenge

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.twotoo.presenter.R
import com.mashup.twotoo.presenter.createChallenge.model.ChallengeInfoModel
import com.mashup.twotoo.presenter.designsystem.component.dialog.selection.SelectionDialogButtonContent
import com.mashup.twotoo.presenter.designsystem.component.toast.SnackBarHost
import com.mashup.twotoo.presenter.designsystem.component.toolbar.TwoTooToolbar
import com.mashup.twotoo.presenter.designsystem.theme.TwoTooTheme
import com.mashup.twotoo.presenter.designsystem.theme.TwotooPink
import com.mashup.twotoo.presenter.history.TwoTooSelectionDialog
import com.mashup.twotoo.presenter.home.model.BeforeChallengeState
import com.mashup.twotoo.presenter.util.DateFormatter
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CreateChallengeRoute(
    createChallengeViewModel: CreateChallengeViewModel,
    onBackToHome: () -> Unit,
    onFinishChallengeInfo: () -> Unit,
) {
    val state by createChallengeViewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    createChallengeViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CreateChallengeSideEffect.NavigateToSuccessCreate -> {
            }
            is CreateChallengeSideEffect.NavigateToHome -> {
                onBackToHome()
            }
            is CreateChallengeSideEffect.ToastMessage -> {
                snackbarHostState.showSnackbar(message = sideEffect.message)
            }
            is CreateChallengeSideEffect.DismissDialog -> {
                createChallengeViewModel.setDialogVisibility(false)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CreateChallenge(
            state = state,
            updateOneStep = { challengeInfoModel ->
                createChallengeViewModel.setCreateChallengeInfo(challengeInfoModel)
                createChallengeViewModel.updateCurrentStep(1)
            },
            updateTwoStep = { challengeInfoModel ->
                createChallengeViewModel.setCreateChallengeInfo(challengeInfoModel)
                createChallengeViewModel.updateCurrentStep(1)
            },
            onClickTheeStep = {
                onFinishChallengeInfo()
            },
            onClickBackButton = {
                if (state.homeState == BeforeChallengeState.EMPTY.name || state.homeState == BeforeChallengeState.TERMINATION.name) {
                    if (state.currentStep > 1) {
                        createChallengeViewModel.updateCurrentStep(-1)
                    } else {
                        onBackToHome()
                    }
                } else {
                    onBackToHome()
                }
            },
            onClickDialogPositiveButton = createChallengeViewModel::deleteChallenge,
            setDialogVisibility = createChallengeViewModel::setDialogVisibility,
        )
        SnackBarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            snackState = snackbarHostState,
        )
    }
}

@Composable
fun CreateChallenge(
    state: ChallengeInfoModel,
    updateOneStep: (ChallengeInfoModel) -> Unit,
    updateTwoStep: (ChallengeInfoModel) -> Unit,
    onClickTheeStep: () -> Unit,
    onClickBackButton: () -> Unit,
    onClickDialogPositiveButton: (Int) -> Unit,
    setDialogVisibility: (Boolean) -> Unit,
) = with(state) {
    val context = LocalContext.current
    val isNextButtonVisible =
        homeState == BeforeChallengeState.RESPONSE.name ||
            homeState == BeforeChallengeState.EMPTY.name ||
            homeState == BeforeChallengeState.TERMINATION.name

    Column(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        CreateChallengeToolbar(
            challengeNo = challengeNo,
            homeState = homeState,
            deleteDialogVisibility = dialogVisibility,
            onClickBackButton = onClickBackButton,
            onClickDialogPositiveButton = onClickDialogPositiveButton,
            setDialogVisibility = setDialogVisibility,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, start = 24.dp, end = 24.dp),
        ) {
            if (homeState == BeforeChallengeState.TERMINATION.name || homeState == BeforeChallengeState.EMPTY.name) {
                Text(
                    text = stringResource(id = R.string.create_challenge_step, currentStep),
                    textAlign = TextAlign.Left,
                    style = TwoTooTheme.typography.headLineNormal28,
                    color = TwoTooTheme.color.mainBrown,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            if (homeState == BeforeChallengeState.TERMINATION.name || homeState == BeforeChallengeState.EMPTY.name ||
                homeState == BeforeChallengeState.RESPONSE.name
            ) {
                Text(
                    text = stringResource(id = getCurrentStepString("title", currentStep, context), currentStep),
                    textAlign = TextAlign.Left,
                    style = TwoTooTheme.typography.headLineNormal24,
                    color = TwoTooTheme.color.mainBrown,
                )
                Text(
                    text = stringResource(id = getCurrentStepString("desc", currentStep, context)),
                    style = TwoTooTheme.typography.bodyNormal14,
                    color = TwoTooTheme.color.gray600,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
            when (currentStep) {
                1 -> CreateChallengeOneStep(state) { name, startDate, endDate ->
                    val period = DateFormatter.formatDateRange(startDate, endDate)
                    updateOneStep(
                        ChallengeInfoModel(
                            challengeName = name,
                            startDate = startDate,
                            endDate = endDate,
                            period = period,
                        ),
                    )
                }
                2 -> CreateChallengeTwoStep(state) { challengeInfo ->
                    updateTwoStep(ChallengeInfoModel(challengeInfo = challengeInfo))
                }
                3 -> CreateChallengeCard(
                    challengeTitle = state.challengeName,
                    challengeDesc = state.challengeInfo,
                    challengeDate = state.period,
                    isNextButtonVisible = isNextButtonVisible,
                    onClickNext = onClickTheeStep,
                )
            }
        }
    }
}

@Composable
fun CreateChallengeToolbar(
    deleteDialogVisibility: Boolean,
    challengeNo: Int,
    homeState: String,
    onClickBackButton: () -> Unit,
    onClickDialogPositiveButton: (Int) -> Unit,
    setDialogVisibility: (Boolean) -> Unit,
) {
    val toolbarTitle = when (homeState) {
        BeforeChallengeState.WAIT.name, BeforeChallengeState.REQUEST.name -> stringResource(id = R.string.challenge_info)
        BeforeChallengeState.RESPONSE.name, BeforeChallengeState.EMPTY.name, BeforeChallengeState.TERMINATION.name -> ""
        else -> { "" }
    }

    TwoTooToolbar.CreateChallengeToolbar(
        modifier = Modifier.fillMaxWidth(),
        title = toolbarTitle,
        onClickActionButton = {
            if (BeforeChallengeState.isChallengeDeletionEnabled(homeState)) {
                setDialogVisibility(true)
            }
        },
        onClickBackIcon = onClickBackButton,
    )

    if (deleteDialogVisibility) {
        TwoTooSelectionDialog(
            selectionDialogButtonContents = listOf(
                SelectionDialogButtonContent(
                    titleId = R.string.challenge_done,
                    buttonAction = {
                        onClickDialogPositiveButton(challengeNo)
                    },
                    color = TwotooPink,
                ),
                SelectionDialogButtonContent(
                    titleId = R.string.cancel,
                    buttonAction = { setDialogVisibility(false) },
                    color = Color.Black,
                ),
            ),
        )
    }
}

fun getCurrentStepString(name: String, currentStep: Int, context: Context): Int {
    val titleName = "create_challenge_${name}_$currentStep"
    return context.resources.getIdentifier(titleName, "string", context.packageName)
}

@Preview
@Composable
private fun PreviewCreateChallengeOneStep() {
    CreateChallenge(
        state = ChallengeInfoModel(),
        updateOneStep = {},
        updateTwoStep = {},
        onClickBackButton = {},
        onClickTheeStep = {},
        onClickDialogPositiveButton = {},
        setDialogVisibility = {},
    )
}
