package com.example.writers_books.data

import androidx.room.Entity


@Entity(tableName = "writers_books", primaryKeys = ["writerId", "bookId"], foreignKeys = [
    androidx.room.ForeignKey(
        entity = Writer::class,
        parentColumns = ["id"],
        childColumns = ["writerId"],
        onDelete = androidx.room.ForeignKey.CASCADE
    ),
    androidx.room.ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = androidx.room.ForeignKey.CASCADE
    )
])
data class WriterBook(
    val writerId: Int,
    val bookId: Int
)
