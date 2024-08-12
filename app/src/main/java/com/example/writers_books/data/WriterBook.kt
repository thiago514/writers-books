package com.example.writers_books.data

import androidx.room.Entity


@Entity(tableName = "writers_books", primaryKeys = ["writerId", "bookId"])
data class WriterBook(
    val writerId: Int,
    val bookId: Int
)
