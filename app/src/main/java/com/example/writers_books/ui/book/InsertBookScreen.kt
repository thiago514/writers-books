package com.example.writers_books.ui.book

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.writers_books.R
import com.example.writers_books.WritersBooksTopAppBar
import com.example.writers_books.ui.navigation.NavigationDestination
import com.example.writers_books.ui.theme.componentes.DateInput
import com.example.writers_books.ui.theme.componentes.WriterBookSelectCard
import com.example.writers_books.ui.theme.componentes.WriterBookType
import kotlinx.coroutines.launch
import java.util.Date

object InsertBooksDestination: NavigationDestination {
    override val route: String = "Inserir Livros"
    override val titleRes: Int = R.string.insert_book
}


@Composable
fun InsertBooksScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertBookViewModel = viewModel(factory = InsertBookViewModel.Factory)
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            WritersBooksTopAppBar(
                title = "Livros",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()) {
            val coroutineScope = rememberCoroutineScope()
            val bookDetail = viewModel.insertBookUiState.bookDetail
            OutlinedTextField(
                value = bookDetail.title,
                onValueChange = {
                    viewModel.updateUiState(bookDetail.copy(title = it))
                },
                label = { Text("Título") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = bookDetail.isbn,
                onValueChange = {
                    viewModel.updateUiState(bookDetail.copy(isbn = it))
                },
                label = { Text("ISBN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            DateInput(onChange = { viewModel.updateUiState((bookDetail.copy(relaseDate = Date(it+1000000))))}, label = "Data de Lançamento", modifier = modifier.padding(8.dp))
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = bookDetail.numPages.toString(),
                onValueChange = {
                    if(it.isNotEmpty()){
                        viewModel.updateUiState(bookDetail.copy(numPages = it.toInt()))
                    }
                },
                label = { Text("Número de Páginas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            if(!viewModel.insertBookUiState.isEnteryValid) {
                Text("Por favor, preencha todos os campos", color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
            }
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .height(500.dp),
            ) {
                items(viewModel.insertBookUiState.writerList) {
                    WriterBookSelectCard(
                        type = WriterBookType.WRITER,
                        obj = it,
                        onSelect = {
                            viewModel.selectWriter(it)
                        },
                    )
                }
            }
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.saveBook()
                    navigateBack()
                }
            },
                shape = MaterialTheme.shapes.medium,
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()) {
                Text("Salvar")
            }

        }
    }
}
