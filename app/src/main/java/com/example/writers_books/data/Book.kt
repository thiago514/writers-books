package com.example.writers_books.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "books")
@TypeConverters(DateConverter::class)
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isbn: String,
    val relaseDate: Date,
    val numPages: Int,
)

data class BookWithWriters(
    @Embedded val book: Book,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(WriterBook::class, parentColumn = "bookId", entityColumn = "writerId")
    )
    val writers: List<Writer>
)