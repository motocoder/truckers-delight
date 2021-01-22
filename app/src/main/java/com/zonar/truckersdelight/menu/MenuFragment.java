package com.zonar.truckersdelight.menu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zonar.truckersdelight.DelightApplication;
import com.zonar.truckersdelight.R;
import com.zonar.truckersdelight.add.AddItemDialogFragment;
import com.zonar.truckersdelight.menu.data.MenuItem;
import com.zonar.truckersdelight.menu.data.MenuItemDAO;
import com.zonar.truckersdelight.menu.ui.MenuAdapter;
import com.zonar.truckersdelight.summary.SummaryDialogFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MenuAdapter mAdapter;
    private CheckBox nonVeggiesCheck;
    private CheckBox veggiesCheck;

    @Override
    public View onCreateView(
        final LayoutInflater inflater,
        final ViewGroup container,
        final Bundle savedInstanceState
    ) {

        final View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final Set<String> selected;
        final boolean veggiesChecked;
        final boolean nonVeggiesChecked;

        //check saved state
        if(savedInstanceState != null) {

            final List<String> selectedList = savedInstanceState.getStringArrayList(SELECTED);

            if(selectedList != null) {
                selected = new HashSet<>(selectedList);
            }
            else {
                selected = new HashSet<>();
            }

            veggiesChecked = savedInstanceState.getBoolean(VEGGIES_CHECKED, true);
            nonVeggiesChecked = savedInstanceState.getBoolean(NON_VEGGIES_CHECKED, true);

        }
        else {

            veggiesChecked = true;
            nonVeggiesChecked = true;
            selected = new HashSet<>();

        }

        mAdapter = new MenuAdapter(getContext(), getDAO(getContext()), selected, veggiesChecked, nonVeggiesChecked);
        mRecyclerView.setAdapter(mAdapter);

        final Button submit = rootView.findViewById(R.id.submit_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected.size() == 0) {
                    Toast.makeText(getContext(), "You must select at least one item", Toast.LENGTH_LONG).show();
                    return;
                }

                sendOrder(mAdapter.getSelected());


            }
        });

        nonVeggiesCheck = rootView.findViewById(R.id.non_veggies_check);
        nonVeggiesCheck.setChecked(nonVeggiesChecked);
        veggiesCheck = rootView.findViewById(R.id.veggies_check);
        veggiesCheck.setChecked(veggiesChecked);

        //if the veggies only gets checked, change the filter
        nonVeggiesCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mAdapter.setNonVeggiesEnabled(isChecked);
        });

        //if the non veggies only gets checked, change the filter
        veggiesCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mAdapter.setVeggiesEnabled(isChecked);
        });

        return rootView;

    }

    /**
     * This is where we place an order to the specified items
     * @param selected
     */
    private void sendOrder(Set<String> selected) {
        showSummaryDialog(selected);
    }

    /**
     *
     * @param selected - items that are being ordered
     */
    private void showSummaryDialog(Set<String> selected) {

        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        final Fragment prev =  getChildFragmentManager().findFragmentByTag("summary-dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final DialogFragment newFragment = SummaryDialogFragment.newInstance(new ArrayList<>(selected));
        newFragment.show(ft, "summary-dialog");
    }

    private static final String SELECTED = "selected";
    private static final String VEGGIES_CHECKED = "veggies_checked";
    private static final String NON_VEGGIES_CHECKED = "non_veggies_checked";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(SELECTED, new ArrayList<>(mAdapter.getSelected()));
        outState.putBoolean(VEGGIES_CHECKED, veggiesCheck.isChecked());
        outState.putBoolean(SELECTED, nonVeggiesCheck.isChecked());

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static MenuItemDAO getDAO(Context context) {

        if(context.getApplicationContext() instanceof DelightApplication) {
            final DelightApplication application = (DelightApplication) context.getApplicationContext();

            return application.getItemDAO();

        }
        else {
            return new MenuItemDAO() {
                @Override
                public List<MenuItem> getAll() {
                    return new ArrayList<>();
                }

                @Override
                public List<MenuItem> getAll(Set<String> types) {
                    return new ArrayList<>();
                }

                @Override
                public List<MenuItem> loadAllByIds(String... ids) {
                    return new ArrayList<>();
                }

                @Override
                public List<MenuItem> loadAll(List<String> ids, Set<String> types) {
                    return new ArrayList<>();
                }

                @Override
                public List<MenuItem> loadAll(Set<String> ids) {
                    return new ArrayList<>();
                }

                @Override
                public void insertAll(MenuItem... users) {

                }

                @Override
                public void delete(MenuItem user) {

                }
            };
        }
    }

    /**
     * reloads the data in the menu
     */
    public void reload() {
        mAdapter.reload();
    }

    public void refreshMenu() {

        nonVeggiesCheck.setChecked(true);
        veggiesCheck.setChecked(true);
        mAdapter.clearSelected();

    }
}