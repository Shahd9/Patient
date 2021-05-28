package com.msaproject.patient.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.msaproject.patient.ApplicationClass;
import com.msaproject.patient.model.DiseaseModel;
import com.msaproject.patient.model.SpecializationModel;

@Database(entities = {SpecializationModel.class, DiseaseModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract SpecializationDao getSpecializationDao();
    public abstract DiseaseDao getDiseaseDao();

    public static synchronized AppDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(ApplicationClass.getInstance().getApplicationContext(),
                        AppDatabase.class, "MSA_Project_Patient.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }

    public void clearDatabase() {
        INSTANCE.clearAllTables();
    }
}
