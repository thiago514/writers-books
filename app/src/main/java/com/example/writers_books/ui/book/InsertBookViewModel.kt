package com.example.writers_books.ui.book

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writers_books.data.Book
import com.example.writers_books.data.BookDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

data class InsertBookUiState(
    val bookDetail: BookDetail = BookDetail(),
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

fun Book.toBookUiState(isEnteryValid: Boolean = false): InsertBookUiState = InsertBookUiState(
    bookDetail = this.toBookDetail(),
    isEnteryValid = isEnteryValid
)


class InsertBookViewModel(private val bookDao: BookDao) : ViewModel() {

    var insertBookUiState by mutableStateOf(InsertBookUiState())
        private set

    fun updateUiState(bookDetail: BookDetail) {
        insertBookUiState = InsertBookUiState(bookDetail, validateInput(bookDetail))
    }

    suspend fun saveBook() {
        if (validateInput()) {
            bookDao.insertBook(insertBookUiState.bookDetail.toBook())
        }
    }

    private fun validateInput(uiState: BookDetail = insertBookUiState.bookDetail): Boolean {
        return with(uiState) {
            title.isNotBlank() && isbn.isNotBlank() && numPages > 0
        }
    }

    fun resetUiState() {
        insertBookUiState = InsertBookUiState()
    }

    fun updateBook(book: Book) {
        insertBookUiState = book.toBookUiState()
    }


}