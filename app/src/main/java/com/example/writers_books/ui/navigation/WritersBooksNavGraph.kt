package com.example.writers_books.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.writers_books.ui.book.InsertBooksDestination
import com.example.writers_books.ui.book.InsertBooksScreen
import com.example.writers_books.ui.book.InsertWriterDestination
import com.example.writers_books.ui.book.InsertWriterScreen
import com.example.writers_books.ui.book.ListBooksDestination
import com.example.writers_books.ui.book.ListBooksScreen
import com.example.writers_books.ui.book.ListWritersDestination
import com.example.writers_books.ui.book.ListWritersScreen
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
                navigateToInsertBook = { navController.navigate(InsertBooksDestination.route) })
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

        // Add NavGraphs here
    }
}