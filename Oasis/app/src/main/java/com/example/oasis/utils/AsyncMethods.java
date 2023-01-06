package com.example.oasis.utils;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class AsyncMethods<T> extends AsyncTask<Callable<T>, Void, T> {

    @Override
    protected T doInBackground(Callable<T>... callables) {
        return (T)callables;
    }
}
