package com.example.writers_books.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.writers_books.WritersBooksApplication
import com.example.writers_books.data.Book
import com.example.writers_books.data.BookDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ListBookUiState(
    val booksList: List<Book> = listOf()
)


class ListBookViewModel(bookDao: BookDao): ViewModel() {

    val uiState: StateFlow<ListBookUiState> =
        bookDao.getAllBooks().map { books ->
            ListBookUiState(books)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ListBookUiState()
        )

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ) :T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                @Suppress("UNCHECKED_CAST")
                return ListBookViewModel(
                    (application as WritersBooksApplication).conteiner.bookDao,
                ) as T
            }
        }
    }
}