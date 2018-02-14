package com.hoangduy.sewingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.hoangduy.sewingapp.R;
import com.hoangduy.sewingapp.dto.Customer;

import java.util.List;

/**
 * Created by hoangduy on 2/13/18.
 */

public class AllCustomerAdapter extends RecyclerView.Adapter<AllCustomerAdapter.AllCustomerAdapterViewHolder> {

    private final List<Customer> listData;
    private final OnItemClickListener listener;

    public AllCustomerAdapter(List<Customer> listData, OnItemClickListener listener) {
        this.listData = listData;
        this.listener = listener;
    }

    @Override
    public AllCustomerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.all_customer_item, parent, false);
        return new AllCustomerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllCustomerAdapter.AllCustomerAdapterViewHolder holder, int position) {
        String customerName = listData.get(position).getName();
        holder.customerName.setText(customerName);
        int space_letter = customerName.lastIndexOf(" ");
        if (space_letter > 0)
            holder.customerAvatar.setTitleText(String.valueOf(customerName.substring(customerName.lastIndexOf(" ") + 1).charAt(0)).toUpperCase());
        else
            holder.customerAvatar.setTitleText(String.valueOf(customerName.charAt(0)).toUpperCase());

        holder.bind(listData.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(Customer customer);

        void OnItemLongClick();
    }

    public class AllCustomerAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView customerName;
        private RoundedLetterView customerAvatar;

        public AllCustomerAdapterViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.item_customer_Name);
            customerAvatar = itemView.findViewById(R.id.item_customerAvatar);
        }

        public void bind(final Customer customer, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(customer);
                }
            });
        }
    }
}