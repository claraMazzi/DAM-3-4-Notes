package com.dam.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RecordatorioPreferencesDataSource implements RecordatorioDataSource {
    private final SharedPreferences sharedPreferences;
    RecordatorioPreferencesDataSource(final Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // Implementación de los metodos de la interface
    @Override
    public void guardarRecordatorio(RecordatorioModel recordatorio, GuardarRecordatorioCallback callback) {

    }

    @Override
    public void recuperarRecordatorios(RecuperarRecordatorioCallback callback) {

    }

}