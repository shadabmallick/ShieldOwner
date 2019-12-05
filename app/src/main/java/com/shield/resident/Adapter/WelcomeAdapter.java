package com.shield.resident.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.shield.resident.R;

import java.util.ArrayList;

public class WelcomeAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Integer> images;
    private ArrayList<String> textArray;
    private LayoutInflater layoutInflater;


    public WelcomeAdapter(Context context, ArrayList<Integer> images,
                          ArrayList<String> textArray) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.images = images;
        this.textArray = textArray;


    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.welcome_item,
                container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        TextView tv_msg = itemView.findViewById(R.id.tv_msg);

        imageView.setImageResource(images.get(position));
        tv_msg.setText(textArray.get(position));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}