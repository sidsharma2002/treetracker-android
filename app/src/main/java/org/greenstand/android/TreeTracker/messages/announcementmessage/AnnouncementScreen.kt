package org.greenstand.android.TreeTracker.messages.announcementmessage


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.greenstand.android.TreeTracker.R
import org.greenstand.android.TreeTracker.messages.OtherChatIcon
import org.greenstand.android.TreeTracker.models.messages.AnnouncementMessage
import org.greenstand.android.TreeTracker.root.LocalNavHostController
import org.greenstand.android.TreeTracker.theme.CustomTheme
import org.greenstand.android.TreeTracker.view.ActionBar
import org.greenstand.android.TreeTracker.view.AppButtonColors
import org.greenstand.android.TreeTracker.view.AppColors
import org.greenstand.android.TreeTracker.view.ArrowButton
import org.greenstand.android.TreeTracker.view.CustomDialog
import org.greenstand.android.TreeTracker.view.LocalImage
import org.greenstand.android.TreeTracker.view.RoundedLocalImageContainer


@Composable
fun AnnouncementScreen(
    messageId: String,
    viewModel: AnnouncementViewModel = viewModel(
        factory = AnnouncementViewModelFactory(messageId)
    )
) {
    val state by viewModel.state.collectAsState(AnnouncementState())
    val navController = LocalNavHostController.current

    Scaffold(
        topBar = {
            ActionBar(
                centerAction = {
                    OtherChatIcon()
                },
            )
        },
        bottomBar = {
            ActionBar(
                leftAction = {
                    ArrowButton(
                        isLeft = true,
                        colors = AppButtonColors.MessagePurple,
                        onClick = {
                            navController.popBackStack()
                        }
                    )
                },
            )
        },

        ) {
        Column(
            Modifier
                .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 80.dp)
                .fillMaxSize()
        ) {
            Announcement(state = state)
        }
    }
}

@Composable
private fun Announcement(
    state: AnnouncementState,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .background(
                color = AppColors.MessageReceivedBackground,
                shape = RoundedCornerShape(6.dp)
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        state.currentTitle?.let { title ->
            Text(
                text = title,
                color = CustomTheme.textColors.lightText,
                fontWeight = FontWeight.Bold,
                style = CustomTheme.typography.large,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        state.currentBody?.let { body ->
            Text(
                text = body,
                color = CustomTheme.textColors.lightText,
                fontWeight = FontWeight.Bold,
                style = CustomTheme.typography.regular,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        state.currentUrl?.let { url ->
            Text(
                text = url,
                Modifier
                    .padding(top = 10.dp)
                    .clickable(onClick = {
                        openUrlLink(context = context, url = url)
                    }),
                color = AppColors.Green,
                style = TextStyle(textDecoration = TextDecoration.Underline),
            )
        }
    }
}

private fun openUrlLink(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    ContextCompat.startActivity(context, intent, null)
}