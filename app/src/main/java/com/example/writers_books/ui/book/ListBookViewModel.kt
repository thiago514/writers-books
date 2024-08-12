package com.example.writers_books.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

}