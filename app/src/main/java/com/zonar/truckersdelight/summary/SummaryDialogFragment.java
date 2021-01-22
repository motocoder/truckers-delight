package com.zonar.truckersdelight.summary;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zonar.truckersdelight.MainActivity;
import com.zonar.truckersdelight.menu.MenuFragment;
import com.zonar.truckersdelight.R;
import com.zonar.truckersdelight.summary.ui.OrderSummaryAdapter;

import java.util.ArrayList;
import java.util.List;

public class SummaryDialogFragment extends DialogFragment {

    private static final String IDS = "ids";
    private List<String> ids;

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private OrderSummaryAdapter mAdapter;

    public static SummaryDialogFragment newInstance(List<String> itemsIDs) {

        final SummaryDialogFragment f = new SummaryDialogFragment();

        final Bundle args = new Bundle();
        args.putStringArrayList(IDS, new ArrayList<String>(itemsIDs));
        f.setArguments(args);

        return f;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();

        if (args != null) {
            this.ids = args.getStringArrayList(IDS);
        }
        else {
            this.ids = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(
        final LayoutInflater inflater,
        final ViewGroup container,
        final Bundle savedInstanceState
    ) {

        View rootView = inflater.inflate(R.layout.fragment_dialog_summary, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new OrderSummaryAdapter(getContext(), MenuFragment.getDAO(getContext()), ids);
        mRecyclerView.setAdapter(mAdapter);

        final Button doneButton = rootView.findViewById(R.id.done);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentActivity act = getActivity();

                if(act instanceof MainActivity) {
                    ((MainActivity)act).refreshMenu();
                }

                dismiss();

            }
        });

        return rootView;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                //do your stuff
            }
        };
    }


}
