package com.example.writers_books.ui.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.writers_books.R
import com.example.writers_books.WritersBooksTopAppBar
import com.example.writers_books.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            .fillMaxSize()) {
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
//            OutlinedTextField(
//                value = bookDetail.relaseDate.toString(),
//                onValueChange = {
//                    viewModel.updateUiState(bookDetail.copy(relaseDate = it))
//                },
//                label = { Text("Data de Lançamento") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.)
//            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(onChange: (Long) -> Unit, label: String, modifier: Modifier = Modifier) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        onChange(it)
        convertMillisToDate(it)
    } ?: ""

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,

                        showModeToggle = true
                    )
                }
            }
        }
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )


    }
}



fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

