package com.example.myapplication.outils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    //propriétes
    private String creation="create table profil("
            +"datemesure TEXT PRIMARY KEY,"
            +"poids INTEGER NOT NULL,"
            +"taille INTEGER NOT NULL,"
            +"age INTEGER NOT NULL,"
            +"sexe INTEGER NOT NULL);";
    /**
     * Constructeur
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name,
                              @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Si changement de BD
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(creation);
    }

    /**
     * si changement de version
     * @param sqLiteDatabase
     * @param i ancien version
     * @param i1 nouvelle version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
