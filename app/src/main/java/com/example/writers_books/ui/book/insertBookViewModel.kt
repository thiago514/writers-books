package com.example.writers_books.ui.book

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.writers_books.WritersBooksApplication
import com.example.writers_books.data.Book
import com.example.writers_books.data.BookDao
import com.example.writers_books.data.Writer
import com.example.writers_books.data.WriterBookDao
import com.example.writers_books.data.WriterDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

data class InsertBookUiState(
    val bookDetail: BookDetail = BookDetail(),
    val writerList: List<WriterSelect> = listOf(),
    val isEnteryValid: Boolean = false
)

data class BookDetail(
    val id: Int = 0,
    val title: String = "",
    val isbn: String = "",
    val relaseDate: Date = Date(),
    val numPages: Int = 0,
)

fun BookDetail.toBook(): Book {
    return Book(
        id = id,
        title = title,
        isbn = isbn,
        relaseDate = relaseDate,
        numPages = numPages
    )
}

fun Book.toBookDetail(): BookDetail {
    return BookDetail(
        id = id,
        title = title,
        isbn = isbn,
        relaseDate = relaseDate,
        numPages = numPages
    )
}

data class WriterSelect(
    val writer: Writer,
    var isSelected: Boolean
)

class InsertBookViewModel(private val bookDao: BookDao,
    private val writerDao: WriterDao,
    private val writerBookDao: WriterBookDao) : ViewModel() {

    var insertBookUiState by mutableStateOf(InsertBookUiState())
        private set

    init {
        viewModelScope.launch {
            writerDao.getAllWriters().collect {
                insertBookUiState = insertBookUiState.copy(writerList = it.map { writer ->
                    WriterSelect(writer, false)
                })
            }
        }

    }

    fun selectWriter(writer: WriterSelect) {
        insertBookUiState = insertBookUiState.copy(writerList = insertBookUiState.writerList.map { writerSelect ->
            if (writerSelect == writer) {
                writerSelect.copy(isSelected = !writerSelect.isSelected)
            } else {
                writerSelect
            }
        })
    }

    fun updateUiState(bookDetail: BookDetail) {
        insertBookUiState = insertBookUiState.copy(bookDetail = bookDetail)
    }

    suspend fun saveBook() {
        if (validateInput()) {
            bookDao.insertBook(insertBookUiState.bookDetail.toBook())
            val bookId = bookDao.getBookByTitle(insertBookUiState.bookDetail.title).first().id
            val selectedWriters = insertBookUiState.writerList.filter { it.isSelected }.map { it.writer }
            writerBookDao.insertMultipleWritersForOneBook(selectedWriters, bookId)

        }
    }

    private fun validateInput(uiState: BookDetail = insertBookUiState.bookDetail): Boolean {
        return with(uiState) {
            title.isNotBlank() && isbn.isNotBlank() && numPages > 0
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ) :T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                @Suppress("UNCHECKED_CAST")
                val insertBookViewModel = InsertBookViewModel(
                    (application as WritersBooksApplication).conteiner.bookDao,
                    application.conteiner.writerDao,
                    application.conteiner.writerBookDao
                ) as T
                return insertBookViewModel
            }
        }
    }


}