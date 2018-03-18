package com.hoangduy.sewingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoangduy.sewingapp.R;
import com.hoangduy.sewingapp.dto.History;
import com.hoangduy.sewingapp.utils.Constants;

import java.util.List;

/**
 * Created by hoangduy on 2/15/18.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryAdapterViewHolder> {

    private final List<History> listData;
    private final OnItemClickListener listener;

    public HistoryAdapter(List<History> listData, OnItemClickListener listener) {
        this.listData = listData;
        this.listener = listener;
    }

    @Override
    public HistoryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_item, parent, false);
        return new HistoryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapterViewHolder holder, int position) {
        holder.tv_shortDescription.setText(listData.get(position).getDescription());
        holder.tv_Date.setText(Constants.normalizeDate(listData.get(position).getDate()));
        holder.bind(listData.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(History history);

        void OnItemLongClick();
    }

    public class HistoryAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tv_shortDescription;
        TextView tv_Date;

        public HistoryAdapterViewHolder(View itemView) {
            super(itemView);
            tv_shortDescription = itemView.findViewById(R.id.tv_shortDescription);
            tv_Date = itemView.findViewById(R.id.item_date);
        }

        public void bind(final History history, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(history);
                }
            });
        }
    }
}
