package com.example.kizuhane.shoplist;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    //delete all data
    @Query("DELETE FROM note_table")
    void deleteAllNotes();


    //delete all complete task
    @Query("DELETE FROM note_table WHERE complete = '1'")
    void deleteAllCompleteNotes();
//    LiveData<List<Note>> deleteAllCompleteNotes();

    //select all complete task and sort by incomplete
    @Query("SELECT * FROM note_table ORDER BY complete")
    LiveData<List<Note>> getAllNotes();

}
