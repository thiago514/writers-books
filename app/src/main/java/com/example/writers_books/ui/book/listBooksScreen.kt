package com.example.writers_books.ui.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.writers_books.R
import com.example.writers_books.WritersBooksTopAppBar
import com.example.writers_books.ui.navigation.NavigationDestination
import com.example.writers_books.ui.theme.componentes.WriterBookCard
import com.example.writers_books.ui.theme.componentes.WriterBookType

object ListBooksDestination: NavigationDestination {
    override val route: String = "Livros"
    override val titleRes: Int = R.string.books_title
}


@Composable
fun ListBooksScreen(
    navigateBack: () -> Unit,
    navigateToInsertBook: () -> Unit,
    navigateToDetailBook: (Int) -> Unit,
    modifier: Modifier = Modifier,
    listBookViewModel: ListBookViewModel = viewModel(factory = ListBookViewModel.Factory)
) {
    val uiState by listBookViewModel.uiState.collectAsState()


    Scaffold(
        modifier = modifier,
        topBar = {
            WritersBooksTopAppBar(
                title = "Livros",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToInsertBook() }) {
                Text(text = "+", style = MaterialTheme.typography.titleMedium, fontSize = 24.sp)

            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
        ) {
            items(items = uiState.booksList, key = { it.id }) { book ->
                WriterBookCard(
                    type = WriterBookType.BOOK,
                    obj = book,
                    onClick = { navigateToDetailBook(book.id) }
                )
            }

        }


    }
}



