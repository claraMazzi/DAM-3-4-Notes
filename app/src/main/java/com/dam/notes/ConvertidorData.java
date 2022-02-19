package com.dam.notes;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class ConvertidorData {
    @TypeConverter
    public static Long fromDate(Date fecha){
        return fecha == null ? null :fecha.getTime();
    }

    @TypeConverter
    public static Date toDate(Long fecha){
        return fecha == null ? null: new Date(fecha);
    }
}
