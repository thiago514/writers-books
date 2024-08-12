package com.example.writers_books.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface WriterBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWriterBook(writerBook: WriterBook)

    suspend fun insertMultipleBooksForOneWriter(books: List<Book>, writerId: Int) {
        books.forEach { book ->
            insertWriterBook(WriterBook(writerId, book.id))
        }
    }

    suspend fun insertMultipleWritersForOneBook(writers: List<Writer>, bookId: Int) {
        writers.forEach { writer ->
            insertWriterBook(WriterBook(writer.id, bookId))
        }
    }
}