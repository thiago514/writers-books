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

data class InsertWriterUiState(
    val writerDetail: WriterDetail = WriterDetail(),
    val bookList: List<BookSelect> = listOf(),
    val isEnteryValid: Boolean = false
)

data class BookSelect(
    val book: Book,
    var isSelected: Boolean
)

data class WriterDetail(
    val id: Int = 0,
    val name: String = "",
    val bornDate: Date = Date(),
    val gender: String = "",
)

fun WriterDetail.toWriter(): Writer {
    return Writer(
        id = id,
        name = name,
        bornDate = bornDate,
        gender = gender
    )
}

fun Writer.toWriterDetail(): WriterDetail {
    return WriterDetail(
        id = id,
        name = name,
        bornDate = bornDate,
        gender = gender
    )
}

class InsertWriterViewModel(private val writerDao: WriterDao,
    private val bookDao: BookDao,
    private val writerBookDao: WriterBookDao) : ViewModel() {

    var insertWriterUiState by mutableStateOf(InsertWriterUiState())
        private set

    init {
        viewModelScope.launch {
            bookDao.getAllBooks().collect {
                insertWriterUiState = insertWriterUiState.copy(bookList = it.map { book -> BookSelect(book, false) })
            }

        }
    }

    fun selectBook(book: BookSelect){
        insertWriterUiState = insertWriterUiState.copy(
            bookList = insertWriterUiState.bookList.map {
                if(it == book){
                    it.copy(isSelected = !it.isSelected)
                } else {
                    it
                }
            }
        )
    }

    fun updateUiState(writerDetail: WriterDetail) {
        insertWriterUiState = InsertWriterUiState(writerDetail = writerDetail)
    }

    suspend fun saveWriter() {
        if (validateInput()) {
            writerDao.insertWriter(insertWriterUiState.writerDetail.toWriter())
            val writerId = writerDao.getWriterByName(insertWriterUiState.writerDetail.name).first().id
            val selectedBooks = insertWriterUiState.bookList.filter { it.isSelected }.map { it.book }
            writerBookDao.insertMultipleBooksForOneWriter(selectedBooks, writerId)
        }
    }

    private fun validateInput(uiState: WriterDetail = insertWriterUiState.writerDetail): Boolean {
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
                @Suppress("UNCHECKED_CAST")
                return InsertWriterViewModel(
                    (application as WritersBooksApplication).conteiner.writerDao,
                    application.conteiner.bookDao,
                    application.conteiner.writerBookDao
                ) as T
            }
        }
    }


}