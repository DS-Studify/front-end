package com.ds.studify.core.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import kotlinx.coroutines.launch

private val ACTIVE_TAB_BACKGROUND_COLOR = StudifyColors.G02
private val ACTIVE_TAB_TEXT_COLOR = StudifyColors.BLACK
private val INACTIVE_TAB_BACKGROUND_COLOR = Color(0xFFF6F6F6)
private val INACTIVE_TAB_TEXT_COLOR = StudifyColors.G03
private const val TRANSITION_TIME = 300
private val TAB_SPACE = 4.dp

@Composable
fun StudifyTabBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    tabTitles: List<String>
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = INACTIVE_TAB_BACKGROUND_COLOR,
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
    ) {
        val indicatorFraction by animateFloatAsState(
            targetValue = selectedTabIndex.toFloat(),
            animationSpec = tween(durationMillis = TRANSITION_TIME),
            label = "Tab indicator",
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val indicatorWidth = (size.width - TAB_SPACE.toPx() *
                            (tabTitles.size + 1)) / tabTitles.size
                    val indicatorHeight = size.height - 8.dp.toPx()
                    val startCenter = 4.dp.toPx() + indicatorWidth * 0.5f
                    val endCenter = size.width - startCenter
                    val indicatorCenterOffset = startCenter +
                            (endCenter - startCenter) * indicatorFraction / tabTitles.size.minus(1)
                    drawRoundRect(
                        color = ACTIVE_TAB_BACKGROUND_COLOR,
                        topLeft = Offset(
                            x = indicatorCenterOffset - indicatorWidth * 0.5f,
                            y = 4.dp.toPx()
                        ),
                        size = Size(indicatorWidth, indicatorHeight),
                        cornerRadius = CornerRadius(26.dp.toPx()),
                    )
                }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TAB_SPACE),
            modifier = Modifier
                .padding(4.dp)
        ) {
            repeat(tabTitles.size) { pageIndex ->
                val animatedOffset by animateFloatAsState(
                    targetValue = if (pageIndex == selectedTabIndex) 1f else 0f,
                    animationSpec = tween(durationMillis = TRANSITION_TIME),
                    label = "Tab animation"
                )
                val textColor = lerp(
                    INACTIVE_TAB_TEXT_COLOR,
                    ACTIVE_TAB_TEXT_COLOR,
                    animatedOffset
                )
                Button(
                    contentPadding = PaddingValues(0.dp),
                    onClick = { onTabSelected(pageIndex) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StudifyColors.TRANSPARENT,
                        contentColor = StudifyColors.BLACK
                    )
                ) {
                    Text(
                        modifier = Modifier,
                        text = tabTitles[pageIndex],
                        style = Typography.titleSmall,
                        textAlign = TextAlign.Center,
                        color = textColor,
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 360, showBackground = true)
@Composable
private fun StudifyTabBarPreview() {
    var currentTabIndex by remember { mutableIntStateOf(0) }
    val tabList = listOf("공부시간", "집중도", "자세")
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState {
        tabList.size
    }
    Column {
        StudifyTabBar(
            selectedTabIndex = currentTabIndex,
            onTabSelected = { pageIndex ->
                currentTabIndex = pageIndex
                scope.launch {
                    pagerState.animateScrollToPage(
                        pageIndex,
                        animationSpec = spring()
                    )
                }
            },
            tabTitles = tabList
        )
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            userScrollEnabled = false
        ) {
            Text(
                text = tabList[it],
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}