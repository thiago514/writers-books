package com.example.writers_books.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.writers_books.WritersBooksApplication
import com.example.writers_books.ui.book.InsertBookViewModel
import com.example.writers_books.ui.book.ListBookViewModel

object AppViewModelProvider {
    val application = WritersBooksApplication()
    val Factory = viewModelFactory {

        initializer {
            ListBookViewModel(
                application.conteiner.bookDao
            )
        }
        initializer {
            InsertBookViewModel(
                application.conteiner.bookDao
            )
        }
    }
}