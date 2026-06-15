package com.gmail.uli153.ulisestv.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gmail.uli153.ulisestv.ui.categories.CategoriesScreen
import com.gmail.uli153.ulisestv.ui.detail.DetailScreen
import com.gmail.uli153.ulisestv.ui.home.HomeScreen
import com.gmail.uli153.ulisestv.ui.login.LoginScreen
import com.gmail.uli153.ulisestv.ui.player.PlayerScreen
import com.gmail.uli153.ulisestv.ui.search.SearchScreen
import com.gmail.uli153.ulisestv.ui.videos.VideosScreen

@Composable
fun UlisesTvNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN,
    ) {
        composable(Destinations.LOGIN) {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                },
            )
        }

        composable(Destinations.HOME) {
            HomeScreen(
                onFeaturedClick = { video ->
                    navController.navigate(Destinations.detail(video.id))
                },
                onCategoryClick = { category ->
                    navController.navigate(Destinations.videos(category.id))
                },
                onAllCategoriesClick = {
                    navController.navigate(Destinations.CATEGORIES)
                },
            )
        }

        composable(Destinations.CATEGORIES) {
            CategoriesScreen(
                onCategoryClick = { category ->
                    navController.navigate(Destinations.videos(category.id))
                },
            )
        }

        composable(
            route = "${Destinations.DETAIL}/{${Destinations.ARG_VIDEO_ID}}",
            arguments = listOf(
                navArgument(Destinations.ARG_VIDEO_ID) { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getInt(Destinations.ARG_VIDEO_ID)
                ?: return@composable
            DetailScreen(
                videoId = videoId,
                onPlay = { video ->
                    navController.navigate(Destinations.player(Uri.encode(video.url)))
                },
            )
        }

        composable(
            route = "${Destinations.VIDEOS}?${Destinations.ARG_CATEGORY_ID}={${Destinations.ARG_CATEGORY_ID}}",
            arguments = listOf(
                navArgument(Destinations.ARG_CATEGORY_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                },
            ),
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt(Destinations.ARG_CATEGORY_ID) ?: -1
            VideosScreen(
                categoryId = categoryId,
                onVideoClick = { video ->
                    navController.navigate(Destinations.player(Uri.encode(video.url)))
                },
                onSearchClick = {
                    navController.navigate(Destinations.search(categoryId))
                },
            )
        }

        composable(
            route = "${Destinations.SEARCH}?${Destinations.ARG_CATEGORY_ID}={${Destinations.ARG_CATEGORY_ID}}",
            arguments = listOf(
                navArgument(Destinations.ARG_CATEGORY_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                },
            ),
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt(Destinations.ARG_CATEGORY_ID) ?: -1
            SearchScreen(
                categoryId = categoryId,
                onVideoClick = { video ->
                    navController.navigate(Destinations.player(Uri.encode(video.url)))
                },
            )
        }

        composable(
            route = "${Destinations.PLAYER}?${Destinations.ARG_VIDEO_URL}={${Destinations.ARG_VIDEO_URL}}",
            arguments = listOf(
                navArgument(Destinations.ARG_VIDEO_URL) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
        ) { backStackEntry ->
            // navigation-compose already URL-decodes query args once, so pass it through as-is.
            val videoUrl = backStackEntry.arguments?.getString(Destinations.ARG_VIDEO_URL).orEmpty()
            PlayerScreen(videoUrl = videoUrl)
        }
    }
}
