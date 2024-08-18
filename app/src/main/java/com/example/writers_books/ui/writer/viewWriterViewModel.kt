package com.example.writers_books.ui.writer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.writers_books.WritersBooksApplication
import com.example.writers_books.data.Writer
import com.example.writers_books.data.WriterDao
import com.example.writers_books.data.WriterWithBooks
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

data class ViewWriterUiState(
    val writerDetail: WriterWithBooks = WriterWithBooks(Writer(0,"",Date(),""), listOf())
)

class ViewWriterViewModel(
    savedStateHandle: SavedStateHandle,
    private val writerDao: WriterDao) : ViewModel() {

    private val writerId: Int = checkNotNull(savedStateHandle[ViewWriterDestination.writerIdArg])

    var viewWriterUiState: StateFlow<ViewWriterUiState> =
        writerDao.getWriterWithBooks(writerId)
            .filterNotNull()
            .map { writerWithBooks ->
                if(writerWithBooks.isEmpty()) return@map ViewWriterUiState()
                ViewWriterUiState(writerWithBooks.first())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = ViewWriterUiState(WriterWithBooks(Writer(0,"",Date(),""), listOf()))
            )


    suspend fun deleteWriter(navigateBack: () -> Unit) {
            writerDao.deleteWriter(viewWriterUiState.value.writerDetail.writer)
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
                val insertBookViewModel = ViewWriterViewModel(
                    savedStateHandle,
                    (application as WritersBooksApplication).conteiner.writerDao
                ) as T
                return insertBookViewModel
            }
        }
    }


}