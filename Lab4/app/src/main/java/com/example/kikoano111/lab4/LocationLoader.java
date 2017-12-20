package com.example.kikoano111.lab4;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import java.util.List;


/**
 * Created by kikoano111 on 20/12/2017.
 */

public class LocationLoader extends AsyncTaskLoader<List<LocationEntity>> {

    public LocationLoader(Context context) {
        super(context);
    }

    @Override
    public List<LocationEntity> loadInBackground() {
        LocationsDatabase db = Room
                .databaseBuilder(getContext(), LocationsDatabase.class, "locations")
                .fallbackToDestructiveMigration()
                .build();
        List<LocationEntity> locationEntities = db.locationDao().getAll();
        return locationEntities;
    }
}
