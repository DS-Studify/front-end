package com.ds.studify.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.feature.calendar.navigation.RouteStats
import com.ds.studify.feature.calendar.navigation.navigateToStats
import com.ds.studify.feature.home.navigation.RouteHome
import com.ds.studify.feature.home.navigation.navigateToHome

@Composable
fun MainBottomNavigationBar(
    navHostController: NavHostController
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val navOptionFactory: () -> NavOptions = {
        navOptions {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val isHomeSelected = currentDestination?.hierarchy?.any {
        it.hasRoute(RouteHome::class)
    } ?: false
    val isStatsSelected = currentDestination?.hierarchy?.any {
        it.hasRoute(RouteStats::class)
    } ?: false

    InnerMainBottomNavigationBar(
        isHomeSelected = isHomeSelected,
        isStatsSelected = isStatsSelected,
        onHomeClicked = {
            navHostController.navigateToHome(navOptionFactory())
        },
        onStatsClicked = {
            navHostController.navigateToStats(navOptionFactory())
        }
    )
}

@Composable
fun InnerMainBottomNavigationBar(
    isHomeSelected: Boolean,
    isStatsSelected: Boolean,
    onHomeClicked: () -> Unit,
    onStatsClicked: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .defaultMinSize(minHeight = 72.dp)
        ) {
            NavigationBarItem(
                iconId = StudifyDrawable.ic_pen,
                labelId = StudifyString.navigation_study,
                selected = isHomeSelected,
                onClick = onHomeClicked
            )

            NavigationBarItem(
                iconId = StudifyDrawable.ic_calendar,
                labelId = StudifyString.navigation_calendar,
                selected = isStatsSelected,
                onClick = onStatsClicked
            )
        }
    }
}

@Composable
private fun NavigationBarItem(
    @DrawableRes iconId: Int,
    @StringRes labelId: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = StudifyColors.TRANSPARENT,
        onClick = onClick,
        modifier = Modifier
            .height(59.dp)
            .width(78.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(iconId),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if (selected) StudifyColors.PK03 else Color(
                        0xFF6F6F6F
                    )
                ),
                modifier = Modifier.size(31.dp)
            )
            Text(
                text = stringResource(labelId),
                color = if (selected) {
                    StudifyColors.PK03
                } else {
                    Color(0xFF6F6F6F)
                },
                style = Typography.titleSmall,
                fontSize = 15.sp
            )
        }
    }
}

@Preview(widthDp = 393, heightDp = 72)
@Composable
private fun BottomNavigationBarPreview() {
    InnerMainBottomNavigationBar(
        isHomeSelected = true,
        isStatsSelected = false,
        onHomeClicked = {},
        onStatsClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun NavigationBarItemPreview() {
    NavigationBarItem(
        iconId = StudifyDrawable.ic_pen,
        labelId = StudifyString.navigation_study,
        selected = true,
        onClick = {}
    )
}