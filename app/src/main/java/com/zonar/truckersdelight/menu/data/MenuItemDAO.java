package com.zonar.truckersdelight.menu.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.Set;

@Dao
public interface MenuItemDAO {

    @Query("SELECT * FROM menuitem")
    List<MenuItem> getAll();

    /**
     *
     * @param types - Types to load, MenuItem.VEGGIE or MenuItem.NON_VEGGIE
     * @return
     */
    @Query("SELECT * FROM menuitem WHERE type IN (:types)")
    List<MenuItem> getAll(final Set<String> types);

    /**
     *
     * @param ids - GUIDS to laod
     * @return
     */
    @Query("SELECT * FROM menuitem WHERE id IN (:ids)")
    List<MenuItem> loadAllByIds(String ... ids);

    /**
     *
     * @param ids - GUIDS to load
     * @param types - Types to load, MenuItem.VEGGIE or MenuItem.NON_VEGGIE
     * @return
     */
    @Query("SELECT * FROM menuitem WHERE id IN (:ids) AND type IN (:types)")
    List<MenuItem> loadAll(List<String> ids, final Set<String> types);

    /**
     *
     * @param ids - GUIDS to load
     * @return
     */
    @Query("SELECT * FROM menuitem WHERE id IN (:ids)")
    List<MenuItem> loadAll(Set<String> ids);

    /**
     *
     * @param items - items to insert
     */
    @Insert
    void insertAll(MenuItem... items);

    /**
     *
     * @param item - item to delete
     */
    @Delete
    void delete(MenuItem item);

}
