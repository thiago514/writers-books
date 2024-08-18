package com.example.writers_books.ui.writer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.writers_books.WritersBooksApplication
import com.example.writers_books.data.BookDao
import com.example.writers_books.data.WriterBookDao
import com.example.writers_books.data.WriterDao
import com.example.writers_books.ui.book.BookSelect
import com.example.writers_books.ui.book.WriterDetail
import com.example.writers_books.ui.book.toWriter
import com.example.writers_books.ui.book.toWriterDetail
import kotlinx.coroutines.launch

data class EditWriterUiState(
    val writerDetail: WriterDetail = WriterDetail(),
    val bookList: List<BookSelect> = listOf(),
    val isEnteryValid: Boolean = false
)

class EditWriterViewModel(private val bookDao: BookDao,
    private val writerDao: WriterDao,
    private val writerBookDao: WriterBookDao,
    savedStateHandle: SavedStateHandle) : ViewModel() {

    var editWriterUiState by mutableStateOf(EditWriterUiState())
        private set

    private val writerId: Int = checkNotNull(savedStateHandle[EditWriterDestination.writerIdArgEdit])

    init {
        viewModelScope.launch {
            bookDao.getAllBooks().collect { books ->
                writerDao.getWriterWithBooks(writerId).collect { writerWithBooks ->
                    val writerDetail = writerWithBooks.first().writer
                    editWriterUiState = editWriterUiState.copy(
                        writerDetail = writerDetail.toWriterDetail(),
                        bookList = books.map { book ->
                            BookSelect(book, writerWithBooks.first().books.contains(book))
                        })
                }
            }
        }

    }

    suspend fun selectBook(book: BookSelect) {
        editWriterUiState = editWriterUiState.copy(bookList = editWriterUiState.bookList.map { bookSelect ->
            if (bookSelect == book) {
                val bookAlterado = bookSelect.copy(isSelected = !bookSelect.isSelected)
                if (bookAlterado.isSelected) {
                    writerBookDao.insertWriterBook(com.example.writers_books.data.WriterBook(writerId ,bookAlterado.book.id))
                    bookAlterado
                } else {
                    writerBookDao.deleteWriterBook(writerId ,bookAlterado.book.id)
                    bookAlterado
                }
            } else {
                bookSelect
            }
        })
    }

    fun updateUiState(writerDetail: WriterDetail) {
        editWriterUiState = editWriterUiState.copy(writerDetail = writerDetail)
    }


    suspend fun saveWriter() {
        if (validateInput()) {
            writerDao.updateWriter(editWriterUiState.writerDetail.toWriter())

        }
    }

    private fun validateInput(uiState: WriterDetail = editWriterUiState.writerDetail): Boolean {
        return with(uiState) {
            name.isNotBlank() && gender.isNotBlank()
        }
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
                val editBookViewModel = EditWriterViewModel(
                    (application as WritersBooksApplication).conteiner.bookDao,
                    application.conteiner.writerDao,
                    application.conteiner.writerBookDao,
                    savedStateHandle
                ) as T
                return editBookViewModel
            }
        }
    }


}