package com.example.buidxpert;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.buidxpert.R;

import java.util.List;


public class MyAdapter extends PagerAdapter {



    List<Integer> list;

    MyAdapter(List<Integer> imageList)
    {
        this.list=imageList;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_layout,container,false);
        ImageView image=view.findViewById(R.id.imageview);
        image.setImageResource(list.get(position));
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
