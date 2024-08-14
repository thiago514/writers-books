package com.example.writers_books.ui.book

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.writers_books.WritersBooksApplication
import com.example.writers_books.data.Book
import com.example.writers_books.data.BookDao
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


@Suppress("UNCHECKED_CAST")
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



    fun updateBook(book: Book) {
        insertBookUiState = book.toBookUiState()
    }

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ) :T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return InsertBookViewModel(
                    (application as WritersBooksApplication).conteiner.bookDao,
                ) as T
            }
        }
    }


}