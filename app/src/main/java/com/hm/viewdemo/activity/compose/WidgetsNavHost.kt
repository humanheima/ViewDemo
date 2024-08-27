package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


object WidgetsNavRoute {

    const val widgetsEnter = "Widgets Enter"
    const val buttonPractice = "Button Practice"
    const val floatButtonPractice = "Float Button Practice"

    const val route = "Widgets"
    const val accountTypeArg = "account_type"
    const val routeWithArgs = "${route}/{${accountTypeArg}}"
    val arguments = listOf(
        navArgument(accountTypeArg) { type = NavType.StringType }
    )
}

@Composable
fun WidgetsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = WidgetsNavRoute.widgetsEnter,
        modifier = modifier
    ) {

        composable(route = WidgetsNavRoute.widgetsEnter) {
            WidgetsEnter(
                modifier = modifier,
                onBackClick = onBackClick,
                onAccountClick = { accountType ->
                    navController.navigateToSingleAccount(accountType)
                }
            )
        }
        composable(
            route = WidgetsNavRoute.routeWithArgs,
            arguments = WidgetsNavRoute.arguments
        ) {
            val accountType = it.arguments?.getString(WidgetsNavRoute.accountTypeArg)
                ?: WidgetsNavRoute.widgetsEnter
            WidgetsPractice(accountType, modifier, onBackClick = {
                navController.popBackStack()
            })
        }
    }
}

@Composable
private fun WidgetsEnter(
    modifier: Modifier,
    onBackClick: () -> Unit,
    onAccountClick: (String) -> Unit = {}
) {

    Scaffold(
        topBar = { SimpleTopAppBarExample("Widgets 入口", onBackClick) }
    ) { padding ->

        Column(
            modifier = modifier.padding(padding)
        ) {

            Button(
                onClick = {
                    onAccountClick(WidgetsNavRoute.buttonPractice)
                },
                modifier = modifier.fillMaxWidth()
            ) {
                Text(text = "Button Practice")
            }
            Button(
                onClick = {
                    onAccountClick(WidgetsNavRoute.floatButtonPractice)
                },
                modifier = modifier.fillMaxWidth()
            ) {
                Text(text = WidgetsNavRoute.floatButtonPractice)
            }
        }

    }

}

@Composable
private fun WidgetsPractice(
    route: String,
    modifier: Modifier,
    onBackClick: () -> Unit
) {
    when (route) {
        WidgetsNavRoute.buttonPractice -> ButtonPractice(modifier, onBackClick)
        WidgetsNavRoute.floatButtonPractice -> FloatButtonPractice(modifier, onBackClick)

        else -> Text(text = "未知界面")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopAppBarExample(
    title: String,
    navigateBack: () -> Unit
) {

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),

        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

private fun NavHostController.navigateToSingleAccount(accountType: String) {
    this.navigateSingleTopTo("${WidgetsNavRoute.route}/$accountType")
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }