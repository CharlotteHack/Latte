package com.salad.latte.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.salad.latte.MainActivity;
import com.salad.latte.Objects.News;
import com.salad.latte.R;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    ArrayList<News> news;
    MainActivity rootContext;

    public NewsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<News> objects) {
        super(context, resource, objects);
        news = objects;
        rootContext = (MainActivity) context;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = ((LayoutInflater)rootContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_news_item,parent,false);
        return v;
    }
}
