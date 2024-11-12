package com.debugdesk.bot.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.debugdesk.bot.datamodel.Statics

@Dao
interface StaticsDao {
    @Query("SELECT * FROM statics_table")
    fun getAll(): List<Statics>

    @Insert
    fun create(statics: Statics)

    @Update
    fun update(statics: Statics)

    @Delete
    fun delete(statics: Statics)

    @Query("SELECT * FROM statics_table WHERE createdAt = :createdAt")
    fun getByDate(createdAt: String): List<Statics>

    @Query("DELETE FROM statics_table")
    fun deleteAllStatics()
}