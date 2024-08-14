package com.example.writers_books.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.writers_books.WritersBooksApplication
import com.example.writers_books.data.Writer
import com.example.writers_books.data.WriterDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ListWritersUiState(
    val writersList: List<Writer> = listOf()
)


@Suppress("UNCHECKED_CAST")
class ListWritersViewModel(writerDao: WriterDao): ViewModel() {

    val uiState: StateFlow<ListWritersUiState> =
        writerDao.getAllWriters().map { writers ->
            ListWritersUiState(writers)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ListWritersUiState()
        )

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ) :T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return ListWritersViewModel(
                    (application as WritersBooksApplication).conteiner.writerDao,
                ) as T
            }
        }
    }
}