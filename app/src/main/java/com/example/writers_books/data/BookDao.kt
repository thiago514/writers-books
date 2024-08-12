package com.example.writers_books.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM BOOKS")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM BOOKS WHERE id = :id")
    fun getBookWithWriters(id: Int): Flow<List<BookWithWriters>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

}