package com.example.writers_books.ui.book

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.writers_books.WritersBooksApplication
import com.example.writers_books.data.Book
import com.example.writers_books.data.BookDao
import com.example.writers_books.data.BookWithWriters
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

data class ViewBookUiState(
    val bookDetail: BookWithWriters = BookWithWriters(Book(0,"","",Date(),0), listOf())
)

class ViewBookViewModel(
    savedStateHandle: SavedStateHandle,
    private val bookDao: BookDao) : ViewModel() {

    private val bookId: Int = checkNotNull(savedStateHandle[ViewBookDestination.bookIdArg])

    var viewBookUiState: StateFlow<ViewBookUiState> =
        bookDao.getBookWithWriters(bookId)
            .filterNotNull()
            .map { bookWithWriters ->
                if(bookWithWriters.isEmpty()) return@map ViewBookUiState()
                ViewBookUiState(bookWithWriters.first())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = ViewBookUiState(BookWithWriters(Book(0,"","",Date(),0), listOf()))
            )

    suspend fun deleteBook(navigateBack: () -> Unit) {
            bookDao.deleteBook(viewBookUiState.value.bookDetail.book)
            navigateBack()
    }

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ) :T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()
                @Suppress("UNCHECKED_CAST")
                val insertBookViewModel = ViewBookViewModel(
                    savedStateHandle,
                    (application as WritersBooksApplication).conteiner.bookDao
                ) as T
                return insertBookViewModel;
            }
        }
    }


}