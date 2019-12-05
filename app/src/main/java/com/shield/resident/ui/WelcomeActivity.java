package com.shield.resident.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.shield.resident.Adapter.WelcomeAdapter;
import com.shield.resident.R;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    ViewPager viewpager;
    TabLayout tab_layout;
    TextView tv_skip, tv_next;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        viewpager = findViewById(R.id.viewpager);
        tab_layout = findViewById(R.id.tab_layout);
        tv_next = findViewById(R.id.tv_next);
        tv_skip = findViewById(R.id.tv_skip);

        tab_layout.setupWithViewPager(viewpager, true);

        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.mipmap.slider1);
        arrayList.add(R.mipmap.slider2);
        arrayList.add(R.mipmap.slider3);
        arrayList.add(R.mipmap.slider4);
        arrayList.add(R.mipmap.slider5);

        ArrayList<String> textArray = new ArrayList<>();
        textArray.add("");
        textArray.add("");
        textArray.add("");
        textArray.add("");
        textArray.add("");



        initViewPager(arrayList, textArray);



        tv_next.setOnClickListener(v -> {

            int current = getItem(+1);
            if (current < arrayList.size()) {

                viewpager.setCurrentItem(current);

            } else {

                Intent intent = new Intent(WelcomeActivity.this,
                        Login.class);
                startActivity(intent);
                finish();

            }
        });

        tv_skip.setOnClickListener(v -> {

            Intent intent = new Intent(WelcomeActivity.this,
                    Login.class);
            startActivity(intent);
            finish();

        });

    }

    private int getItem(int i) {
        return viewpager.getCurrentItem() + i;
    }


    private void initViewPager(ArrayList<Integer> arrayList,
                               ArrayList<String> textArray) {

        WelcomeAdapter bannerAdapter = new WelcomeAdapter(WelcomeActivity.this,
                arrayList, textArray);
        viewpager.setAdapter(bannerAdapter);


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == arrayList.size() - 1) {
                    // last page. make button text to GOT IT
                    tv_next.setText("DONE");
                    tv_skip.setVisibility(View.GONE);
                } else {
                    // still pages are left
                    tv_next.setText("NEXT");
                    tv_skip.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}