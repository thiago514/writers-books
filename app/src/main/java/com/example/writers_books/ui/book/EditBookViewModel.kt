package com.example.writers_books.ui.book

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
import kotlinx.coroutines.launch

data class EditBookUiState(
    val bookDetail: BookDetail = BookDetail(),
    val writerList: List<WriterSelect> = listOf(),
    val isEnteryValid: Boolean = false
)

class EditBookViewModel(private val bookDao: BookDao,
    private val writerDao: WriterDao,
    private val writerBookDao: WriterBookDao,
    savedStateHandle: SavedStateHandle) : ViewModel() {

    var editBookUiState by mutableStateOf(EditBookUiState())
        private set

    private val bookId: Int = checkNotNull(savedStateHandle[EditBooksDestination.bookIdArgEdit])

    init {
        viewModelScope.launch {
            writerDao.getAllWriters().collect { writters ->
                bookDao.getBookWithWriters(bookId).collect { bookWithWriters ->
                    val bookDetail = bookWithWriters.first().book
                    editBookUiState = editBookUiState.copy(
                        bookDetail = bookDetail.toBookDetail(),
                        writerList = writters.map { writer ->
                            WriterSelect(writer, bookWithWriters.first().writers.contains(writer))
                        })
                }
            }
        }

    }



    suspend fun selectWriter(writer: WriterSelect) {
        editBookUiState = editBookUiState.copy(writerList = editBookUiState.writerList.map { writerSelect ->
            if (writerSelect == writer) {
                val writerAlterado = writerSelect.copy(isSelected = !writerSelect.isSelected)
                if (writerAlterado.isSelected) {
                    writerBookDao.insertWriterBook(com.example.writers_books.data.WriterBook(writerAlterado.writer.id, bookId))
                    writerAlterado
                } else {
                    writerBookDao.deleteWriterBook(writerAlterado.writer.id, bookId)
                    writerAlterado
                }
            } else {
                writerSelect
            }
        })
    }

    fun updateUiState(bookDetail: BookDetail) {
        editBookUiState = editBookUiState.copy(bookDetail = bookDetail)
    }

    suspend fun saveBook() {
        if (validateInput()) {
            bookDao.updateBook(editBookUiState.bookDetail.toBook())
        }
    }

    private fun validateInput(uiState: BookDetail = editBookUiState.bookDetail): Boolean {
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
                val savedStateHandle = extras.createSavedStateHandle()
                @Suppress("UNCHECKED_CAST")
                val editBookViewModel = EditBookViewModel(
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