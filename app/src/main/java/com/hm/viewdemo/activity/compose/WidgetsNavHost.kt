package com.hm.viewdemo.activity.compose

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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


/**
 * 所有的导航路径
 */
object WidgetsNavRoute {

    const val widgetsEnter = "Widgets Enter"
    const val buttonPractice = "Button Practice"
    const val floatButtonPractice = "Float Button Practice"
    const val cardPractice = "Card Practice"

    const val chipPractice = "Chip Practice"

    const val dialogPractice = "Dialog Practice"

    const val progressIndicatorPractice = "Progress Indicator Practice"


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
        val itemList = listOf<Item>(
            Item(
                text = WidgetsNavRoute.buttonPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.buttonPractice)
                }
            ),
            Item(
                text = WidgetsNavRoute.buttonPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.floatButtonPractice)
                }
            ),
            Item(
                text = WidgetsNavRoute.cardPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.cardPractice)
                }
            ),

            Item(
                text = WidgetsNavRoute.chipPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.chipPractice)
                }
            ),

            Item(
                text = WidgetsNavRoute.dialogPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.dialogPractice)
                }
            ),

            Item(
                text = WidgetsNavRoute.progressIndicatorPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.progressIndicatorPractice)
                }
            ),

            )
        composable(route = WidgetsNavRoute.widgetsEnter) {
            WidgetsEnter(
                modifier = modifier,
                itemList,
                onBackClick = onBackClick
            )
        }

        //这个路由固定就是：Widgets/{accountType}

        composable(
            route = WidgetsNavRoute.routeWithArgs,
            arguments = WidgetsNavRoute.arguments
        ) {
            Log.d("WidgetsNavHost", "WidgetsNavHost: route = ${it.destination.route}")
            val accountType = it.arguments?.getString(WidgetsNavRoute.accountTypeArg)
                ?: WidgetsNavRoute.widgetsEnter
            WidgetsPractice(accountType, modifier, onBackClick = {
                navController.popBackStack()
            })
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
        WidgetsNavRoute.cardPractice -> CardPractice(modifier, onBackClick)
        WidgetsNavRoute.chipPractice -> ChipPractice(modifier, onBackClick)
        WidgetsNavRoute.dialogPractice -> DialogPractice(modifier, onBackClick)
        WidgetsNavRoute.progressIndicatorPractice -> ProgressIndicatorPractice(
            modifier,
            onBackClick
        )

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