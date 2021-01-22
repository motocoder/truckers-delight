package com.zonar.truckersdelight;

import android.util.Log;

import androidx.multidex.MultiDexApplication;
import androidx.room.Room;

import com.zonar.truckersdelight.menu.data.MenuItem;
import com.zonar.truckersdelight.menu.data.MenuItemDAO;
import com.zonar.truckersdelight.menu.data.MenuItemDatabase;

import java.util.UUID;
import java.util.concurrent.Executors;

public class DelightApplication extends MultiDexApplication {

    private MenuItemDAO itemDAO;

    @Override
    public void onCreate() {
        super.onCreate();

        //create an instance of the database and dao object
        MenuItemDatabase db =
            Room.databaseBuilder(
                getApplicationContext(),
                MenuItemDatabase.class,
                "menu-items.db"
            ).build();

        this.itemDAO = db.itemDAO();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

                //if nothing exists in the database, this is our first session, load in the default values.
                if(itemDAO.getAll().size() == 0) {

                    Log.d("TruckersDelight", "no items in database, initializing");

                    itemDAO.insertAll(
                            new MenuItem(UUID.randomUUID().toString(), "French Fries", MenuItem.VEGGIE),
                            new MenuItem(UUID.randomUUID().toString(), "VeggieBurger", MenuItem.VEGGIE),
                            new MenuItem(UUID.randomUUID().toString(), "Carrots", MenuItem.VEGGIE),
                            new MenuItem(UUID.randomUUID().toString(), "Apple", MenuItem.VEGGIE),
                            new MenuItem(UUID.randomUUID().toString(), "Banana", MenuItem.VEGGIE),
                            new MenuItem(UUID.randomUUID().toString(), "MilkShake", MenuItem.VEGGIE),
                            new MenuItem(UUID.randomUUID().toString(), "Cheeseburger", MenuItem.NON_VEGGIE),
                            new MenuItem(UUID.randomUUID().toString(), "Hamburger", MenuItem.NON_VEGGIE),
                            new MenuItem(UUID.randomUUID().toString(), "Hot dog", MenuItem.NON_VEGGIE)
                    );
                }

            }
        });

    }

    public MenuItemDAO getItemDAO() {
        return this.itemDAO;
    }

}
