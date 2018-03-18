package com.hoangduy.sewingapp.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by hoangduy on 2/23/18.
 */

@SuppressLint("ParcelCreator")
public class CustomerSearchItem implements SearchSuggestion {
    private String name;

    public CustomerSearchItem() {
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String getBody() {
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
