package com.hoangduy.sewingapp.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.hoangduy.sewingapp.R;
import com.hoangduy.sewingapp.dto.History;
import com.hoangduy.sewingapp.utils.Constants;

import java.util.List;

/**
 * Created by hoangduy on 2/13/18.
 */

public class LatestCustomerAdapter extends RecyclerView.Adapter<LatestCustomerAdapter.LatestCustomerAdapterViewHolder> {

    private final List<History> listData;
    private final OnItemClickListener listener;

    public LatestCustomerAdapter(List<History> listData, OnItemClickListener listener) {
        this.listData = listData;
        this.listener = listener;
    }

    @Override
    public LatestCustomerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.schedule_item, parent, false);
        return new LatestCustomerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LatestCustomerAdapterViewHolder holder, int position) {
        holder.customer_avatar.setTitleText(String.valueOf(listData.get(position).getName().charAt(0)).toUpperCase());
        holder.customer_avatar.setBackgroundColor(Constants.randomColor());
        holder.customer_name.setText(listData.get(position).getName());

        holder.schedule_date.setText(Constants.normalizeDate(listData.get(position).getDate()));
        switch (position) {
            case 0:
                holder.schedule_date.setTextColor(Color.RED);
                break;
            case 1:
                holder.schedule_date.setTextColor(Color.YELLOW);
                break;
            default:
                break;
        }
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

    public class LatestCustomerAdapterViewHolder extends RecyclerView.ViewHolder {
        RoundedLetterView customer_avatar;
        TextView customer_name;
        TextView schedule_date;

        public LatestCustomerAdapterViewHolder(View itemView) {
            super(itemView);
            customer_avatar = itemView.findViewById(R.id.schedule_title_name);
            customer_name = itemView.findViewById(R.id.schedule_customer_name);
            schedule_date = itemView.findViewById(R.id.schedule_date);
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
