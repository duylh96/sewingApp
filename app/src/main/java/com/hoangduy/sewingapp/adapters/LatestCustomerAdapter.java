package com.hoangduy.sewingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hoangduy on 2/13/18.
 */

public class LatestCustomerAdapter extends RecyclerView.Adapter<LatestCustomerAdapter.LatestCustomerAdapterViewHolder> {
    @Override
    public LatestCustomerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(LatestCustomerAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class LatestCustomerAdapterViewHolder extends RecyclerView.ViewHolder {
        public LatestCustomerAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
