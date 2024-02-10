package com.mashup.twotoo.presenter.history

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.mashup.twotoo.presenter.R
import com.mashup.twotoo.presenter.constant.TAG
import com.mashup.twotoo.presenter.designsystem.component.bottomsheet.BottomSheetData
import com.mashup.twotoo.presenter.designsystem.component.bottomsheet.BottomSheetType
import com.mashup.twotoo.presenter.designsystem.component.bottomsheet.TwoTooBottomSheet
import com.mashup.twotoo.presenter.designsystem.component.dialog.DialogContent
import com.mashup.twotoo.presenter.designsystem.component.dialog.TwoTooDialog
import com.mashup.twotoo.presenter.designsystem.component.dialog.selection.SelectionDialogButtonContent
import com.mashup.twotoo.presenter.designsystem.component.loading.FlowerLoadingIndicator
import com.mashup.twotoo.presenter.designsystem.component.toolbar.TwoTooToolbar
import com.mashup.twotoo.presenter.designsystem.theme.TwoTooTheme
import com.mashup.twotoo.presenter.designsystem.theme.TwotooPink
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun HistoryRoute(
    from: String,
    challengeNo: Int,
    historyViewModel: HistoryViewModel,
    onClickBackButton: () -> Unit,
    navigateToHistoryDetail: (Int) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalContext.current as Activity
    val commitNo = activity.intent.getIntExtra("commitNo", 0)
    Log.d(TAG, "HistoryRoute: commitNo= $commitNo, from= $from viewModel= $historyViewModel")
    val state by historyViewModel.collectAsState()
    LaunchedEffect(Unit) {
        launch {
            lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                historyViewModel.getChallengeByUser(challengeNo).join()
                launch {
                    Log.i(
                        TAG,
                        "HistoryScreen: navigateToDetail: navigateChallengeDetail = ${state.navigateToChallengeDetail}",
                    )
                    if (from == "notification" && state.navigateToChallengeDetail) {
                        if (commitNo != 0) {
                            navigateToHistoryDetail(commitNo)
                        }
                        historyViewModel.setNavigateToChallengeDetail(false)
                    }
                }
            }
        }
    }

    HistoryScreen(
        onClickBackButton = onClickBackButton,
        navigateToHistoryDetail = navigateToHistoryDetail,
        quiteChallenge = { historyViewModel.quiteChallenge(challengeNo) },
        onClickBottomSheetDataButton = { bottomSheetData ->
            val bt = (bottomSheetData as BottomSheetData.AuthenticateData)
            historyViewModel.onClickBottomSheetDataButton(bottomSheetData)
        },
        state = state,
    )
}

@Composable
fun HistoryScreen(
    onClickBackButton: () -> Unit,
    navigateToHistoryDetail: (Int) -> Unit,
    quiteChallenge: () -> Unit,
    onClickBottomSheetDataButton: (BottomSheetData) -> Unit,
    state: HistoryState,
) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var bottomSheetType by remember { mutableStateOf<BottomSheetType>(BottomSheetType.Authenticate()) }
    val challengeDropSelectionDialogTitleId = if (state.challengeInfoUiModel.isFinished) {
        R.string.challenge_delete
    } else {
        R.string.challenge_done
    }

    fun onDismiss() {
        isBottomSheetVisible = false
    }

    fun showBottomSheet() {
        isBottomSheetVisible = true
    }

    if (isBottomSheetVisible) {
        TwoTooBottomSheet(
            type = bottomSheetType,
            onDismiss = ::onDismiss,
            onClickButton = {
                onClickBottomSheetDataButton(it)
                isBottomSheetVisible = false
            },

        )
    }
    var showSelectListDialog by remember { mutableStateOf(false) }
    var showChallengeDropDialog by remember { mutableStateOf(false) }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),

        ) {
            Spacer(modifier = Modifier.padding(top = 5.dp))
            TwoTooToolbar.HistoryToolbar(
                modifier = Modifier.fillMaxWidth(),
                onClickBackIcon = onClickBackButton,
                onClickActionButton = {
                    showSelectListDialog = true
                },
            )
            Spacer(modifier = Modifier.height(9.dp))
            if (state.loadingIndicatorState) {
                Box(modifier = Modifier.fillMaxSize()) {
                    FlowerLoadingIndicator(
                        modifier = Modifier.width(128.dp).height(144.dp).align(Alignment.Center),
                    )
                }
            } else {
                ChallengeInfo(
                    state.challengeInfoUiModel,
                )
            }
            OwnerNickNames(state.ownerNickNamesUiModel)
            Spacer(modifier = Modifier.height(12.dp))
            Divider(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
                    .padding(horizontal = 24.dp),
            )
            Box {
                DottedLine()
                HistoryItems(
                    state.historyItemUiModel,
                    navigateToHistoryDetail,
                    ::showBottomSheet,
                )
            }
        }
        if (showSelectListDialog) {
            TwoTooSelectionDialog(
                selectionDialogButtonContents = listOf(
                    SelectionDialogButtonContent(
                        titleId = challengeDropSelectionDialogTitleId,
                        buttonAction = {
                            showSelectListDialog = false
                            showChallengeDropDialog = true
                        },
                        color = TwotooPink,
                    ),
                    SelectionDialogButtonContent(
                        titleId = R.string.cancel,
                        buttonAction = { showSelectListDialog = false },
                        color = Color.Black,
                    ),
                ),
            )
        }
        if (showChallengeDropDialog) {
            TwoTooDialog(
                content = DialogContent.createHistoryLeaveChallengeDialogContent(
                    negativeAction = {
                        showChallengeDropDialog = false
                    },
                    positiveAction = {
                        quiteChallenge()
                        showChallengeDropDialog = false
                        onClickBackButton()
                    },
                    state.challengeInfoUiModel.isFinished,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewHistoryScreen() {
    TwoTooTheme {
        HistoryScreen(
            onClickBackButton = {},
            state = HistoryState.default,
            navigateToHistoryDetail = {},
            onClickBottomSheetDataButton = {},
            quiteChallenge = {},
        )
    }
}

@Preview(name = "비어있을때")
@Composable
private fun PreviewHistoryScreenEmpty() {
    TwoTooTheme {
        HistoryScreen(
            onClickBackButton = {},
            state = HistoryState.default,
            navigateToHistoryDetail = {},
            onClickBottomSheetDataButton = {},
            quiteChallenge = {},
        )
    }
}

@Preview(name = "프로그래스바가 보이는 화면")
@Composable
private fun PreviewHistoryScreenWithProgressBar() {
    TwoTooTheme {
        HistoryScreen(
            onClickBackButton = {},
            state = HistoryState.default,
            navigateToHistoryDetail = {},
            onClickBottomSheetDataButton = {},
            quiteChallenge = {},
        )
    }
}
