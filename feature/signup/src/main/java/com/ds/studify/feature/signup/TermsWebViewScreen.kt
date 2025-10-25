package com.ds.studify.feature.signup

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithTitle
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.feature.signup.navigation.RouteTerms
import com.ds.studify.feature.signup.navigation.TermsType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TermsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val termsRoute: RouteTerms = savedStateHandle.toRoute()
    private val termsType = termsRoute.type

    val titleResId: Int
        get() = when (termsType) {
            TermsType.SERVICE -> StudifyString.signup_terms_service
            TermsType.PRIVACY -> StudifyString.signup_terms_privacy
        }

    val url: String
        get() = when (termsType) {
            TermsType.SERVICE -> "https://malleable-pisces-00d.notion.site/2751852bf7c08048b62ddba00e687924"
            TermsType.PRIVACY -> "https://malleable-pisces-00d.notion.site/2751852bf7c08027a6d4f0b60e20a605"
        }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun TermsWebViewRoute(
    onBack: () -> Unit,
    viewModel: TermsViewModel = hiltViewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val webView = androidx.compose.runtime.remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(false)
            settings.loadsImagesAutomatically = true
            settings.useWideViewPort = true
            settings.allowFileAccess = false
            settings.allowContentAccess = true
        }
    }

    androidx.activity.compose.BackHandler(enabled = true) {
        if (webView.canGoBack()) webView.goBack() else onBack()
    }

    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose { webView.destroy() }
    }

    StudifyScaffoldWithTitle(
        title = stringResource(viewModel.titleResId),
        onBackButtonClick = onBack
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .background(StudifyColors.WHITE)
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { webView },
                update = { it.loadUrl(viewModel.url) }
            )
        }
    }
}
