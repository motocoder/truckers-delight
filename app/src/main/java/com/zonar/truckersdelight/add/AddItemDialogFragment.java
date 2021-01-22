package com.zonar.truckersdelight.add;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zonar.truckersdelight.MainActivity;
import com.zonar.truckersdelight.R;
import com.zonar.truckersdelight.menu.MenuFragment;
import com.zonar.truckersdelight.menu.data.MenuItem;
import com.zonar.truckersdelight.summary.ui.OrderSummaryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddItemDialogFragment extends DialogFragment {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static AddItemDialogFragment newInstance() {

        final AddItemDialogFragment f = new AddItemDialogFragment();

        final Bundle args = new Bundle();
        f.setArguments(args);

        return f;

    }

    @Override
    public View onCreateView(
        final LayoutInflater inflater,
        final ViewGroup container,
        final Bundle savedInstanceState
    ) {

        //inflate the view hierarchy
        final View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

        final CheckBox isVeggie = rootView.findViewById(R.id.is_veggie);
        final EditText itemName = rootView.findViewById(R.id.item_name);
        final Button doneButton = rootView.findViewById(R.id.done);
        final Button cancelButton = rootView.findViewById(R.id.cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //done button will create the new item and save it to the database
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(itemName.getText().toString().isEmpty()){

                    itemName.setError("error");
                    itemName.setBackgroundResource(R.drawable.edterr);
                    return;

                }else {
                    itemName.setBackgroundResource(R.drawable.edtnormal);
                }

                final MenuItem newItem =
                    new MenuItem(
                        UUID.randomUUID().toString(),
                        itemName.getText().toString(),
                        isVeggie.isChecked() ? MenuItem.VEGGIE : MenuItem.NON_VEGGIE
                    );

                final Handler handler =
                    new Handler(Looper.myLooper());

                executor.execute(new Runnable() {
                    @Override
                    public void run() {

                        MenuFragment.getDAO(getContext()).insertAll(newItem);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                final FragmentActivity act = getActivity();

                                if(act instanceof MainActivity) {
                                    ((MainActivity)act).reloadMenu();
                                }

                                dismiss();
                            }
                        });

                    }
                });

            }
        });

        return rootView;

    }


}
