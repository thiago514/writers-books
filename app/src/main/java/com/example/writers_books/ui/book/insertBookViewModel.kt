package com.example.writers_books.ui.book

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.Date

data class InsertBookUiState(
    val bookDetail: BookDetail = BookDetail(),
    val writerList: List<WriterSelect> = listOf(),
    val isEnteryValid: Boolean = false
)

data class ListWriters (
    val writer: List<WriterSelect> = listOf()
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

data class WriterSelect(
    val writer: Writer,
    var isSelected: Boolean
)

@Suppress("UNCHECKED_CAST")
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
        println("selectWriter")
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

    fun toggleWriter(writer: WriterSelect) {
        insertBookUiState = insertBookUiState.copy(writerList = insertBookUiState.writerList.map { writerSelect ->
            if (writerSelect == writer) {
                writerSelect.copy(isSelected = !writerSelect.isSelected)
            } else {
                writerSelect
            }
        })
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
                val insertBookViewModel = InsertBookViewModel(
                    (application as WritersBooksApplication).conteiner.bookDao,
                    (application as WritersBooksApplication).conteiner.writerDao,
                    (application as WritersBooksApplication).conteiner.writerBookDao
                ) as T
                return insertBookViewModel;
            }
        }
    }


}