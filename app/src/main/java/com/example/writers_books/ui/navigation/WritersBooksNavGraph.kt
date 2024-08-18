package com.example.writers_books.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.writers_books.ui.book.EditBooksDestination
import com.example.writers_books.ui.book.EditBooksScreen
import com.example.writers_books.ui.book.InsertBooksDestination
import com.example.writers_books.ui.book.InsertBooksScreen
import com.example.writers_books.ui.book.InsertWriterDestination
import com.example.writers_books.ui.book.InsertWriterScreen
import com.example.writers_books.ui.book.ListBooksDestination
import com.example.writers_books.ui.book.ListBooksScreen
import com.example.writers_books.ui.book.ListWritersDestination
import com.example.writers_books.ui.book.ListWritersScreen
import com.example.writers_books.ui.book.ViewBookDestination
import com.example.writers_books.ui.book.ViewBookScreen
import com.example.writers_books.ui.home.HomeDestination
import com.example.writers_books.ui.home.HomeScreen

@Composable
fun WritersBooksNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier,
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(navigateToItemBooksMenu = { navController.navigate(ListBooksDestination.route) }, navigateToItemWrittersMenu = { navController.navigate(ListWritersDestination.route) })
        }
        composable(route = ListBooksDestination.route) {
            ListBooksScreen(navigateBack = { navController.popBackStack() },
                navigateToInsertBook = { navController.navigate(InsertBooksDestination.route) },
                navigateToDetailBook = { navController.navigate("${ViewBookDestination.route}/${it}") })
            // Add itemBooksMenu here
        }
        composable(route = InsertBooksDestination.route) {
            InsertBooksScreen(navigateBack = { navController.popBackStack()})
            // Add itemBooksMenu here
        }
        composable(route = ListWritersDestination.route) {
            ListWritersScreen(navigateBack = { navController.popBackStack() },
                navigateToInsertWriter = { navController.navigate(InsertWriterDestination.route) })
        }
        composable(route = InsertWriterDestination.route) {
            InsertWriterScreen(navigateBack = { navController.popBackStack()})
            // Add itemBooksMenu here
        }
        composable(route = ViewBookDestination.routeWithArgs,
            arguments = listOf(navArgument(ViewBookDestination.bookIdArg) {
                type = NavType.IntType
            })) {
            ViewBookScreen(navigateBack = { navController.popBackStack() },
                navigateToEditBook = { navController.navigate("${EditBooksDestination.route}/${it}") })
        }

        composable(route = EditBooksDestination.routeWithArgs,
            arguments = listOf(navArgument(EditBooksDestination.bookIdArgEdit) {
                type = NavType.IntType
            })) {
            EditBooksScreen(navigateBack = { navController.popBackStack() })
        }

        // Add NavGraphs here
    }
}