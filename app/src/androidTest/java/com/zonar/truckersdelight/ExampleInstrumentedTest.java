package com.zonar.truckersdelight;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.zonar.truckersdelight.menu.data.MenuItem;
import com.zonar.truckersdelight.menu.data.MenuItemDAO;
import com.zonar.truckersdelight.menu.data.MenuItemDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.zonar.truckersdelight", appContext.getPackageName());

        assertTrue(appContext.getApplicationContext() instanceof DelightApplication);

        final MenuItemDAO itemDAO = ((DelightApplication) appContext.getApplicationContext()).getItemDAO();

        {

            //load in all the menu items
            final List<MenuItem> all = itemDAO.getAll();

            assertNotEquals(0, all.size());

            //create a new item
            final String uuid = UUID.randomUUID().toString();

            itemDAO.insertAll(new MenuItem(uuid, "Test made item", MenuItem.VEGGIE));

            final List<MenuItem> newAll = itemDAO.getAll();

            //verify the new item is in the list now
            assertEquals(all.size() + 1, newAll.size());

            boolean found = false;

            for(final MenuItem item : newAll) {
                if(item.getId().equalsIgnoreCase(uuid)) {
                    found = true;
                    break;
                }
            }

            assertTrue(found);

            //delete the new item
            itemDAO.delete(itemDAO.loadAllByIds(uuid).get(0));

            //verify the list is back to what it was when we started.
            final List<MenuItem> newNewAll = itemDAO.getAll();

            assertEquals(newNewAll, all);

        }

    }
}