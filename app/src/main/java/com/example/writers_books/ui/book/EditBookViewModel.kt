package com.example.writers_books.ui.book

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.writers_books.data.Book
import com.example.writers_books.data.BookDao
import com.example.writers_books.data.Writer
import com.example.writers_books.data.WriterBookDao
import com.example.writers_books.data.WriterDao
import kotlinx.coroutines.delay
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

data class EditBookUiState(
    val bookDetail: BookDetail = BookDetail(),
    val writerList: List<WriterSelect> = listOf(),
    val isEnteryValid: Boolean = false
)

@Suppress("UNCHECKED_CAST")
class EditBookViewModel(private val bookDao: BookDao,
    private val writerDao: WriterDao,
    private val writerBookDao: WriterBookDao,
    savedStateHandle: SavedStateHandle) : ViewModel() {

    var insertBookUiState by mutableStateOf(InsertBookUiState())
        private set

    private val bookId: Int = checkNotNull(savedStateHandle[EditBooksDestination.bookIdArgEdit])

    init {
        viewModelScope.launch {
            writerDao.getAllWriters().collect { writters ->
                bookDao.getBookWithWriters(bookId).collect { bookWithWriters ->
                    val bookDetail = bookWithWriters.first().book
                    insertBookUiState = insertBookUiState.copy(
                        bookDetail = bookDetail.toBookDetail(),
                        writerList = writters.map { writer ->
                            WriterSelect(writer, bookWithWriters.first().writers.contains(writer))
                        })
                }
            }
        }

    }



    suspend fun selectWriter(writer: WriterSelect) {
        println("selectWriter")
        insertBookUiState = insertBookUiState.copy(writerList = insertBookUiState.writerList.map { writerSelect ->
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
        insertBookUiState = insertBookUiState.copy(bookDetail = bookDetail)
    }


    suspend fun saveBook() {
        if (validateInput()) {
            bookDao.updateBook(insertBookUiState.bookDetail.toBook())

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
                val savedStateHandle = extras.createSavedStateHandle()
                val editBookViewModel = EditBookViewModel(
                    (application as WritersBooksApplication).conteiner.bookDao,
                    (application as WritersBooksApplication).conteiner.writerDao,
                    (application as WritersBooksApplication).conteiner.writerBookDao,
                    savedStateHandle
                ) as T
                return editBookViewModel;
            }
        }
    }


}