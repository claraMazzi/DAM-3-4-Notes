package com.dam.notes;

public class RecordatorioRepository {

    private final RecordatorioDataSource datasource;
    public RecordatorioRepository(final RecordatorioDataSource datasource) {
        this.datasource = datasource;
    }

    // Metodos que recuperan los recordatorios usando el data source

    public void guardarRecordatorio(RecordatorioDataSource.GuardarRecordatorioCallback callback,RecordatorioModel recordatorio){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                datasource.guardarRecordatorio(recordatorio, callback);
            }
        };
        r.run();
    }




    public void traerRecordatorios(RecordatorioDataSource.RecuperarRecordatorioCallback callback){

        Runnable r = new Runnable() {
            @Override
            public void run() {
                datasource.recuperarRecordatorios(callback);
            }
        };
        r.run();
    }
}