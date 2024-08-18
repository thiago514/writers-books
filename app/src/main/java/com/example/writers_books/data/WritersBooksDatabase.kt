package com.example.writers_books.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Book::class, Writer::class, WriterBook::class], version = 2, exportSchema = false)
abstract class WritersBooksDatabase: RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun writerDao(): WriterDao
    abstract fun writerBookDao(): WriterBookDao

    companion object {
        @Volatile
        private var INSTANCE: WritersBooksDatabase? = null

        fun getDatabase(context: Context): WritersBooksDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    WritersBooksDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }

            }
        }
    }

}

interface AppConteiner {
    val bookDao: BookDao
    val writerDao: WriterDao
    val writerBookDao: WriterBookDao
}

class AppConteinerImpl(private val context: Context): AppConteiner {
    override val bookDao: BookDao by lazy {
        WritersBooksDatabase.getDatabase(context).bookDao()
    }
    override val writerDao: WriterDao by lazy {
        WritersBooksDatabase.getDatabase(context).writerDao()
    }
    override val writerBookDao: WriterBookDao by lazy {
        WritersBooksDatabase.getDatabase(context).writerBookDao()
    }
}