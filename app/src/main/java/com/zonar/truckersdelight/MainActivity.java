package com.zonar.truckersdelight;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zonar.truckersdelight.add.AddItemDialogFragment;
import com.zonar.truckersdelight.menu.MenuFragment;
import com.zonar.truckersdelight.summary.SummaryDialogFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //ADD button was selected, start up the add fragment dialog
        if (id == R.id.action_add) {

            //design change proposal, add password prompt to enter the add item dialog...

            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            final Fragment prev =  getSupportFragmentManager().findFragmentByTag("add-dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            final DialogFragment newFragment = AddItemDialogFragment.newInstance();
            newFragment.show(ft, "add-dialog");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will reload the menu fragment.
     */
    public void reloadMenu() {

        final MenuFragment fragment = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment);

        if(fragment != null) {
            fragment.reload();
        }

    }

    public void refreshMenu() {

        final MenuFragment fragment = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment);

        if(fragment != null) {
            fragment.refreshMenu();
        }

    }
}