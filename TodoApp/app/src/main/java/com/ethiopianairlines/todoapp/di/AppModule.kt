package com.ethiopianairlines.todoapp.di

import android.content.Context
import com.ethiopianairlines.todoapp.data.local.TodoDatabase
import com.ethiopianairlines.todoapp.data.remote.TodoApi
import com.ethiopianairlines.todoapp.data.repository.TodoRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideTodoApi(moshi: Moshi): TodoApi {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TodoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext context: Context) = 
        TodoDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase) = database.todoDao()

    @Provides
    @Singleton
    fun provideTodoRepository(
        todoApi: TodoApi,
        todoDao: TodoDatabase
    ) = TodoRepository(todoApi, todoDao.todoDao())
}
