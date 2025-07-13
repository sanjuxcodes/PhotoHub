package com.example.photoHub;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhotographerDao {

    @Query("SELECT * FROM Photographer")
    List<Photographer> getAllPhotographers();

    @Insert
    void insertP(Photographer photographer);

    @Update
    void updateP(Photographer photographer);

    @Delete
    void delP(Photographer photographer);
}
