package com.example.oasis.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.ArrayList;
import java.util.List;

public class SortedListViewModel extends BaseObservable {

    private String title = "";

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyChange();
    }
}
