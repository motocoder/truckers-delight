package com.zonar.truckersdelight.summary.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zonar.truckersdelight.R;
import com.zonar.truckersdelight.menu.data.MenuItem;
import com.zonar.truckersdelight.menu.data.MenuItemDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {

    private final List<String> data;
    private final MenuItemDAO dao;
    private final ExecutorService executor;
    private final Context context;

    /**
     *
     * @param context
     * @param dao
     * @param data
     */
    public OrderSummaryAdapter(
        final Context context,
        final MenuItemDAO dao,
        final List<String> data
    ) {

        this.dao = dao;
        this.data = new ArrayList<>(data);
        this.context = context;

        executor = Executors.newFixedThreadPool(5);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.summary_item, parent,
            false
        );

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String guid = data.get(position);

        holder.title.setText("");

        //load the data in
        final Future<?> future = executor.submit(new Runnable() {
            @Override
            public void run() {

                //view was recycled
                if(!guid.equals(holder.itemView.getTag(R.id.loading_tag))) {
                    return;
                }

                final List<MenuItem> loaded = dao.loadAllByIds(guid);

                if(loaded.size() == 1) {

                    final MenuItem item = loaded.get(0);

                    holder.title.setText(item.getTitle());

                    final int color;

                    if(item.getType().equals(MenuItem.VEGGIE)) {
                        color = ContextCompat.getColor(context, R.color.green);
                    }
                    else {
                        color = ContextCompat.getColor(context, R.color.red);
                    }

                    holder.title.setTextColor(color);


                }
            }
        });

        final Future<?> previousLoading = (Future<?>) holder.itemView.getTag(R.id.loading_future);

        //cancel previous loading if we are using a recycled view.
        if(previousLoading != null && !previousLoading.isDone() && !previousLoading.isCancelled()) {
            previousLoading.cancel(true);
        }

        holder.itemView.setTag(R.id.loading_tag, guid);
        holder.itemView.setTag(R.id.loading_future, future);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            this.title = (TextView) view.findViewById(R.id.title);

        }

    }

}
