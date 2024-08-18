package com.example.writers_books.ui.book

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.writers_books.R
import com.example.writers_books.WritersBooksTopAppBar
import com.example.writers_books.data.Writer
import com.example.writers_books.ui.navigation.NavigationDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

