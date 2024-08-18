package com.example.writers_books.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WriterBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWriterBook(writerBook: WriterBook)




    @Query("DELETE FROM writers_books WHERE writerId = :writerId AND bookId = :bookId")
    suspend fun deleteWriterBook(writerId: Int, bookId: Int)


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