package com.zonar.truckersdelight.menu.data;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class MenuItem {

    public static final String VEGGIE = "veggie";
    public static final String NON_VEGGIE = "non-veggie";

    @PrimaryKey
    @NonNull
    private final String id;

    @ColumnInfo(name = "title")
    private final String title;

    @ColumnInfo(name = "type")
    private final String type;

    /**
     *
     * @param id - this should be a guid
     * @param title - title of the menu item
     * @param type - type, use VEGGIE or NON_VEGGIE
     */
    public MenuItem(
        final String id,
        final String title,
        final String type
    ) {

        this.type = type;
        this.title = title;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) //TODO use different method for < kitkat
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return id.equals(menuItem.id) &&
                Objects.equals(title, menuItem.title) &&
                Objects.equals(type, menuItem.type);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) //TODO use different method for < kitkat
    @Override
    public int hashCode() {
        return Objects.hash(id, title, type);
    }
}
