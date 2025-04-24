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
import com.hm.viewdemo.activity.compose.scrolltest.ScrollPractice


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

    const val sliderPractice = "Slider Practice"

    const val switchPractice = "Switch Practice"

    const val checkboxPractice = "Checkbox Practice"

    const val radioGroupPractice = "Radio Group Practice"

    const val BadgesPractice = "Badges Practice"

    const val bottomSheetPractice = "Bottom Sheet Practice"

    const val LazyColumnPractice = "Lazy Column Practice"

    const val HorizontalPagerPractice = "HorizontalPager Practice"

    const val PaddingPractice = "Padding Practice"

    /**
     * 滚动相关学习
     */
    const val ScrollPractice = "Scroll Practice"


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

            Item(
                text = WidgetsNavRoute.sliderPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.sliderPractice)
                }
            ),

            Item(
                text = WidgetsNavRoute.switchPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.switchPractice)
                }
            ),

            Item(
                text = WidgetsNavRoute.checkboxPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.checkboxPractice)
                }
            ),

            Item(
                text = WidgetsNavRoute.BadgesPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.BadgesPractice)
                }
            ),

            Item(
                text = WidgetsNavRoute.bottomSheetPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.bottomSheetPractice)
                }
            ),
            Item(
                text = WidgetsNavRoute.LazyColumnPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.LazyColumnPractice)
                }
            ),

            Item(
                text = WidgetsNavRoute.HorizontalPagerPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.HorizontalPagerPractice)
                }
            ),
            Item(
                text = WidgetsNavRoute.PaddingPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.PaddingPractice)
                }
            ),
            Item(
                text = WidgetsNavRoute.ScrollPractice,
                onclick = {
                    navController.navigateToSingleAccount(WidgetsNavRoute.ScrollPractice)
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
                //解决快速点击返回，会出现一个白屏界面的问题
                navController.popBackStack(WidgetsNavRoute.widgetsEnter, inclusive = false)
                //navController.popBackStack()
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

        WidgetsNavRoute.sliderPractice -> SliderPractice(modifier, onBackClick)

        WidgetsNavRoute.switchPractice -> SwitchPractice(modifier, onBackClick)

        WidgetsNavRoute.checkboxPractice -> CheckboxPractice(modifier, onBackClick)

        WidgetsNavRoute.BadgesPractice -> BadgesPractice(modifier, onBackClick)

        WidgetsNavRoute.bottomSheetPractice -> BottomSheetPractice(modifier, onBackClick)

        WidgetsNavRoute.LazyColumnPractice -> LazyColumnPractice2(modifier, onBackClick)

        WidgetsNavRoute.HorizontalPagerPractice -> HorizontalPagerPractice(modifier, onBackClick)
        WidgetsNavRoute.PaddingPractice -> PaddingPractice(modifier, onBackClick)
        WidgetsNavRoute.ScrollPractice -> ScrollPractice(modifier, onBackClick)

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