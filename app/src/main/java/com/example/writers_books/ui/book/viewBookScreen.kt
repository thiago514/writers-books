package com.example.writers_books.ui.book

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.writers_books.R
import com.example.writers_books.WritersBooksTopAppBar
import com.example.writers_books.ui.navigation.NavigationDestination
import com.example.writers_books.ui.theme.componentes.convertMillisToDate
import kotlinx.coroutines.launch

object ViewBookDestination: NavigationDestination {
    override val route: String = "detailLivro"
    override val titleRes: Int = R.string.book
    const val bookIdArg = "bookId"
    val routeWithArgs = "$route/{$bookIdArg}"
}


@Composable
fun ViewBookScreen(
    navigateBack: () -> Unit,
    navigateToEditBook: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewBookViewModel = viewModel(factory = ViewBookViewModel.Factory)
) {
    val bookDetail = viewModel.viewBookUiState.collectAsState().value.bookDetail
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        topBar = {
            WritersBooksTopAppBar(
                title = "Livro",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()) {

            Text(text = "TITLE", style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            Text(text = bookDetail.book.title, style = MaterialTheme.typography.titleMedium, modifier = modifier.padding(8.dp))
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text(text = "ISBN", style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            Text(text = bookDetail.book.isbn, style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text(text = "DATA DE LANÇAMENTO", style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            Text(text = convertMillisToDate(bookDetail.book.relaseDate.time), style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text(text = "NÚMERO DE PÁGINAS", style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            Text(text = bookDetail.book.numPages.toString(), style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))

            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text(text = "ESCRITORES", style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                items(bookDetail.writers, key = { it.id }) { writer ->
                    Text(
                        text = writer.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier.padding(8.dp)
                    )
                }
            }
            Spacer(modifier = modifier.height(16.dp))
            Button(onClick = {
                navigateToEditBook(bookDetail.book.id)
            },
                shape = MaterialTheme.shapes.medium,
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()) {
                Text("Editar")
            }
            Spacer(modifier = modifier.height(16.dp))
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.deleteBook(navigateBack)
                }
            },
                shape = MaterialTheme.shapes.medium,
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()) {
                Text("Deletar")
            }

        }
    }
}

