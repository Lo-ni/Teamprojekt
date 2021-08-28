package application.android.com.watchthewatchers.OSMConnection;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import application.android.com.watchthewatchers.OSMConnection.models.SampleModel;
import application.android.com.watchthewatchers.OSMConnection.models.SampleModelDao;


@Database(entities={SampleModel.class}, version=1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract SampleModelDao sampleModelDao();

    public static final String NAME = "MyDataBase";
}
