package com.example.writers_books.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.writers_books.R
import com.example.writers_books.WritersBooksTopAppBar
import com.example.writers_books.ui.navigation.NavigationDestination

object HomeDestination: NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.app_name
}


@Composable
fun HomeScreen(
    navigateToItemBooksMenu: () -> Unit,
    navigateToItemWrittersMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            WritersBooksTopAppBar(
                title = "Escolha uma opção",
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            CardButtonOptions(text = "Livros", onClick = { navigateToItemBooksMenu() }, modifier = modifier.padding(innerPadding))
            CardButtonOptions(text = "Autores", onClick = { navigateToItemWrittersMenu() }, modifier = modifier.padding(innerPadding))
        }


    }
}

@Composable
fun CardButtonOptions(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().height(300.dp).padding(16.dp),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)
                .wrapContentSize(Alignment.Center),
            fontSize = 24.sp
        )
    }
}
