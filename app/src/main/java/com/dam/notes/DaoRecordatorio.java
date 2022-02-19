package com.dam.notes;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface DaoRecordatorio {

    @Query("SELECT * FROM recordatorio")
    List<RecordatorioModel> getAll();

    @Insert
    void insertAll(RecordatorioModel... reco);

    @Insert(onConflict = IGNORE)
    Long insert(RecordatorioModel reco);

    @Delete
    void delete(RecordatorioModel reco);

}
