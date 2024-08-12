package com.example.writers_books.ui.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.writers_books.R
import com.example.writers_books.WritersBooksTopAppBar
import com.example.writers_books.data.Book
import com.example.writers_books.ui.AppViewModelProvider
import com.example.writers_books.ui.navigation.NavigationDestination
import com.google.type.Date
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

object InsertBooksDestination: NavigationDestination {
    override val route: String = "Inserir Livros"
    override val titleRes: Int = R.string.insert_book
}


@Composable
fun InsertBooksScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertBookViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
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
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Text(text = "+", style = MaterialTheme.typography.titleMedium, fontSize = 24.sp)

            }
        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            val coroutineScope = rememberCoroutineScope()
            val bookDetail = viewModel.insertBookUiState.bookDetail
            OutlinedTextField(
                value = bookDetail.title,
                onValueChange = {
                    viewModel.updateUiState(bookDetail.copy(title = it))
                },
                label = { Text("Título") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            OutlinedTextField(
                value = bookDetail.isbn,
                onValueChange = {
                    viewModel.updateUiState(bookDetail.copy(isbn = it))
                },
                label = { Text("ISBN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
//            OutlinedTextField(
//                value = bookDetail.relaseDate.toString(),
//                onValueChange = {
//                    viewModel.updateUiState(bookDetail.copy(relaseDate = it))
//                },
//                label = { Text("Data de Lançamento") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.)
//            )
            OutlinedTextField(
                value = bookDetail.numPages.toString(),
                onValueChange = {
                    viewModel.updateUiState(bookDetail.copy(numPages = it.toInt()))
                },
                label = { Text("Número de Páginas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            if(!viewModel.insertBookUiState.isEnteryValid) {
                Text("Por favor, preencha todos os campos", color = MaterialTheme.colorScheme.error)
            }
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.saveBook()
                    navigateBack()
                }
            }) {
                Text("Salvar")
            }

        }
    }
}


