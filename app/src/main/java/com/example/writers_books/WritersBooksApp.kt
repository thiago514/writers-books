package com.example.writers_books

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.writers_books.ui.navigation.WritersBooksNavHost


@Composable
fun WritersBooksApp(navController: NavHostController = rememberNavController()) {
    WritersBooksNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritersBooksTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            if(canNavigateBack) {
                 IconButton(onClick = navigateUp) {
                     Icon(
                         imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                         contentDescription = stringResource(R.string.back_button)
                     )
                 }
            }
        }
    )
}