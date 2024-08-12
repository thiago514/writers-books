package com.example.writers_books.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WriterDao {

    @Query("SELECT * FROM WRITERS")
    fun getAllWriters(): Flow<List<Writer>>

    @Query("SELECT * FROM WRITERS WHERE id = :id")
    fun getWriterWithBooks(id: Int): Flow<List<WriterWithBooks>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWriter(writer: Writer)

    @Update
    suspend fun updateWriter(writer: Writer)

    @Delete
    suspend fun deleteWriter(writer: Writer)
}