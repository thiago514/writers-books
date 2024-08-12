package com.example.writers_books.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "writers")
@TypeConverters(DateConverter::class)
data class Writer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val bornDate: Date,
    val gender: String
)

data class WriterWithBooks(
    @Embedded val writer: Writer,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(WriterBook::class, parentColumn = "writerId", entityColumn = "bookId")
    )
    val books: List<Book>
)

