package com.example.writers_books.ui.theme.componentes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.writers_books.data.Book
import com.example.writers_books.data.Writer

@Composable
fun WriterBookCard(
    modifier: Modifier = Modifier,
    type: WriterBookType,
    obj: Any,
    onClick: () -> Unit
) {
    val name: String
    if(type == WriterBookType.WRITER) {
        val writer = obj as Writer
        name = writer.name
    } else {
        val book = obj as Book
        name = book.title
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = onClick
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            fontSize = 24.sp
        )
    }
}