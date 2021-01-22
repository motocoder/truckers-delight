package com.zonar.truckersdelight.menu.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zonar.truckersdelight.R;
import com.zonar.truckersdelight.menu.data.MenuItem;
import com.zonar.truckersdelight.menu.data.MenuItemDAO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This is the adapter for the menu recycler view.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private final Set<String> selected = new HashSet<>();
    private final List<String> data;
    private final MenuItemDAO dao;
    private final ExecutorService executor;
    private final Context context;
    private final Handler handler;
    private boolean nonVeggiesEnabled;
    private boolean veggiesEnabled;

    /**
     *
     * @param context
     * @param dao
     * @param selected - checked items
     * @param veggiesEnabled - veggies filter enabled
     * @param nonVeggiesEnabled - non-veggies filter enabled
     */
    public MenuAdapter(
        final @NonNull Context context,
        final @NonNull MenuItemDAO dao,
        final @NonNull Set<String> selected,
        final boolean veggiesEnabled,
        final boolean nonVeggiesEnabled
    ) {

        this.veggiesEnabled = veggiesEnabled;
        this.nonVeggiesEnabled = nonVeggiesEnabled;
        this.dao = dao;
        this.data = new ArrayList<>();
        this.context = context;
        this.selected.addAll(selected);

        this.handler = new Handler(Looper.myLooper());

        //arbitrarily creating a thread pool with 5 items to do background loading.
        executor = Executors.newFixedThreadPool(5);

        //load the initial items.
        reload();

    }

    public void reload() {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                data.clear();

                final Set<String> types = new HashSet<>();

                if(veggiesEnabled) {
                    types.add(MenuItem.VEGGIE);
                }

                if(nonVeggiesEnabled) {
                    types.add(MenuItem.NON_VEGGIE);
                }

                for(final MenuItem items : dao.getAll(types)) {
                    data.add(items.getId());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        notifyDataSetChanged();

                    }
                });

            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.menu_item, parent,
            false
        );

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String guid = data.get(position);

        //default previous title
        holder.title.setText("");

        holder.itemView.setTag(R.id.loading_tag, guid);

        //load in the new data
        final Future<?> future = executor.submit(new Runnable() {
            @Override
            public void run() {

                //view was recycled
                if(!guid.equals(holder.itemView.getTag(R.id.loading_tag))) {
                    return;
                }

                final List<MenuItem> loaded = dao.loadAllByIds(guid);

                if(loaded.size() == 1) { //always should be 1 since we only load one guid

                    final MenuItem item = loaded.get(0);

                    final int color;

                    if(item.getType().equals(MenuItem.VEGGIE)) {
                        color = ContextCompat.getColor(context, R.color.green);
                    }
                    else {
                        color = ContextCompat.getColor(context, R.color.red);
                    }

                    //do ui work on the main looper
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.title.setText(item.getTitle());
                            holder.title.setTextColor(color);
                        }
                    });

                }
            }
        });

        final Future<?> previousLoading = (Future<?>) holder.itemView.getTag(R.id.loading_future);

        holder.itemView.setTag(R.id.loading_future, future);

        //cancel any previous load if it isn't done and we're using a recycled view.
        if(previousLoading != null && !previousLoading.isDone() && !previousLoading.isCancelled()) {
            previousLoading.cancel(true);
        }

        holder.selected.setOnCheckedChangeListener(null);
        holder.selected.setChecked(selected.contains(guid));

        //logic for handling selected/unselected
        holder.selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    selected.add(guid);
                }
                else {
                    selected.remove(guid);
                }

            }
        });

    }

    public Set<String> getSelected() {
        return new HashSet<>(selected);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setVeggiesEnabled(boolean isChecked) {
        this.veggiesEnabled = isChecked;
        reload();
    }

    public void setNonVeggiesEnabled(boolean isChecked) {
        this.nonVeggiesEnabled = isChecked;
        reload();
    }

    public void clearSelected() {
        selected.clear();

        reload();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox selected;
        private final TextView title;

        /**
         *
         * @param view
         */
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            this.selected = (CheckBox) view.findViewById(R.id.item_selected);
            this.title = (TextView) view.findViewById(R.id.title);

        }

        public TextView getTitle() {
            return title;
        }
    }

}
