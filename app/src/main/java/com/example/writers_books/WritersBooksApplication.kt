package com.example.writers_books

import android.app.Application
import com.example.writers_books.data.AppConteiner
import com.example.writers_books.data.AppConteinerImpl
import com.example.writers_books.data.WritersBooksDatabase

class WritersBooksApplication: Application() {

    lateinit var conteiner: AppConteiner


    override fun onCreate() {
        super.onCreate()
        conteiner = AppConteinerImpl(this)
    }
}