package com.example.writers_books.ui.writer

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

object ViewWriterDestination: NavigationDestination {
    override val route: String = "detailWriter"
    override val titleRes: Int = R.string.writer
    const val writerIdArg = "writerId"
    val routeWithArgs = "$route/{$writerIdArg}"
}


@Composable
fun ViewWriterScreen(
    navigateBack: () -> Unit,
    navigateToEditWriter: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewWriterViewModel = viewModel(factory = ViewWriterViewModel.Factory)
) {
    val writerDetail = viewModel.viewWriterUiState.collectAsState().value.writerDetail
    val coroutineScope = rememberCoroutineScope()
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
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()) {

            Text(text = "NOME", style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            Text(text = writerDetail.writer.name, style = MaterialTheme.typography.titleMedium, modifier = modifier.padding(8.dp))
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text(text = "GENERO", style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            Text(text = writerDetail.writer.gender, style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text(text = "DATA DE NASCIMENTO", style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            Text(text = convertMillisToDate(writerDetail.writer.bornDate.time), style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text(text = "LIVROS", style = MaterialTheme.typography.bodyMedium, modifier = modifier.padding(8.dp))
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                items(writerDetail.books, key = { it.id }) { book ->
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier.padding(8.dp)
                    )
                }
            }
            Spacer(modifier = modifier.height(16.dp))
            Button(onClick = {
                navigateToEditWriter(writerDetail.writer.id)
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
                    viewModel.deleteWriter(navigateBack)
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

