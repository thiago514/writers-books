package com.example.writers_books.ui.book

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.writers_books.ui.theme.componentes.DropMenuGender
import com.example.writers_books.ui.theme.componentes.WriterBookSelectCard
import com.example.writers_books.ui.theme.componentes.WriterBookType
import kotlinx.coroutines.launch
import java.util.Date

object InsertWriterDestination: NavigationDestination {
    override val route: String = "Inserir Autor"
    override val titleRes: Int = R.string.insert_writer
}


@Composable
fun InsertWriterScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertWriterViewModel = viewModel(factory = InsertWriterViewModel.Factory)
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            WritersBooksTopAppBar(
                title = "Autor",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            val coroutineScope = rememberCoroutineScope()
            val writerDetail = viewModel.insertWriterUiState.writerDetail
            OutlinedTextField(
                value = writerDetail.name,
                onValueChange = {
                    viewModel.updateUiState(writerDetail.copy(name = it))
                },
                label = { Text("Nome") },
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
            DropMenuGender(
                value = writerDetail.gender,
                onValueChange = {
                    viewModel.updateUiState(writerDetail.copy(gender = it))
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            DateInput(onChange = { viewModel.updateUiState((writerDetail.copy(bornDate = Date(it))))}, label = "Data de Nascimento", modifier = modifier.padding(8.dp))
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            if(!viewModel.insertWriterUiState.isEnteryValid) {
                Text("Por favor, preencha todos os campos", color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth())
            }
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .height(500.dp),
            ) {
                items(viewModel.insertWriterUiState.bookList, key = { it.book.id }) {
                    WriterBookSelectCard(
                        type = WriterBookType.BOOK,
                        obj = it,
                        onSelect = {
                            viewModel.selectBook(it)
                        },
                    )
                }
            }
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.saveWriter()
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


