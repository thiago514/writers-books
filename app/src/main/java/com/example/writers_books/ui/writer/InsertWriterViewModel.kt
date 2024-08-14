package com.example.writers_books.ui.book

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.writers_books.WritersBooksApplication
import com.example.writers_books.data.Writer
import com.example.writers_books.data.WriterDao
import java.util.Date

data class InsertWriterUiState(
    val writerDetail: WriterDetail = WriterDetail(),
    val isEnteryValid: Boolean = false
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

fun Writer.toWriterUiState(isEnteryValid: Boolean = false): InsertWriterUiState = InsertWriterUiState(
    writerDetail = this.toWriterDetail(),
    isEnteryValid = isEnteryValid
)


@Suppress("UNCHECKED_CAST")
class InsertWriterViewModel(private val writerDao: WriterDao) : ViewModel() {

    var insertWriterUiState by mutableStateOf(InsertWriterUiState())
        private set

    fun updateUiState(writerDetail: WriterDetail) {
        insertWriterUiState = InsertWriterUiState(writerDetail, validateInput(writerDetail))
    }

    suspend fun saveWriter() {
        if (validateInput()) {
            writerDao.insertWriter(insertWriterUiState.writerDetail.toWriter())
        }
    }

    private fun validateInput(uiState: WriterDetail = insertWriterUiState.writerDetail): Boolean {
        return with(uiState) {
            name.isNotBlank() && gender.isNotBlank()
        }
    }


    fun updateWriter(writer: Writer) {
        insertWriterUiState = writer.toWriterUiState()
    }

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ) :T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return InsertWriterViewModel(
                    (application as WritersBooksApplication).conteiner.writerDao,
                ) as T
            }
        }
    }


}