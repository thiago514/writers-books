package com.example.writers_books.ui.theme.componentes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.writers_books.ui.book.BookSelect
import com.example.writers_books.ui.book.WriterSelect

enum class WriterBookType {
    WRITER,
    BOOK
}

@Composable
fun WriterBookSelectCard(
    type: WriterBookType,
    obj: Any,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selected: Boolean
    val name: String
    if(type == WriterBookType.WRITER) {
        val writer = obj as WriterSelect
        selected = writer.isSelected
        name = writer.writer.name
    } else {
        val book = obj as BookSelect
        selected = book.isSelected
        name = book.book.title
    }

    Column(modifier = modifier) {
        Row(
            modifier = modifier
                .padding(2.dp)
                .fillMaxWidth()


        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                IconButton(
                    onClick = { onSelect() }, modifier = modifier
                        .border(2.dp, Color.Black)
                        .padding(bottom = 7.dp)
                ) {
                    if (selected) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Check",
                            modifier = modifier.size(50.dp),
                        )
                    }

                }
            }
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 25.dp, start = 8.dp)
            )
        }
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .border(3.dp, color = Color.Black)
                .height(3.dp)
        )
    }

}