package com.zonar.truckersdelight.menu.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MenuItem.class}, version = 1)
public abstract class MenuItemDatabase extends RoomDatabase {

    public abstract MenuItemDAO itemDAO();

}
