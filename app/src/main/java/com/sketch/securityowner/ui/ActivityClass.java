package com.sketch.securityowner.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sketch.securityowner.Adapter.AdapterActivity;
import com.sketch.securityowner.Adapter.AdaptorVisitor;
import com.sketch.securityowner.R;

import java.util.ArrayList;

public class ActivityClass extends AppCompatActivity {
    private RecyclerView recyclerView,recycle_upcoming;
    private AdapterActivity mAdapter;
    private AdaptorVisitor upcomingAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager1;
    LinearLayout center_button,car1,rel_upcoming_visitor,rel_all_visitor;
    private  boolean button1IsVisible = true;
    private  boolean viwe1IsVisible = true;
    View view_all_visitor,view_upcoming_visitor;

    ScrollView all_visitor,upcoming_visitor;
    ImageView setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        recyclerView =  findViewById(R.id.recycle_activity);
        recycle_upcoming =  findViewById(R.id.recycle_upcoming);
        center_button =  findViewById(R.id.button_E2);
        car1 =  findViewById(R.id.car1);
        view_upcoming_visitor =  findViewById(R.id.view_upcoming_visitor);
        view_all_visitor =  findViewById(R.id.view_all_visitor);
        rel_all_visitor =  findViewById(R.id.rel_all_visitor);
        rel_upcoming_visitor =  findViewById(R.id.rel_upcoming_visitor);
        rel_all_visitor =  findViewById(R.id.rel_all_visitor);
        all_visitor =  findViewById(R.id.scroll_activity);
        upcoming_visitor =  findViewById(R.id.scroll_upcoming);
        setting =  findViewById(R.id.edit);
        ArrayList<String > aList = new ArrayList<String >();

        aList.add("");
        aList.add("");
        aList.add("");
        aList.add("");
        aList.add("");
        aList.add("");
        aList.add("");
        ArrayList<String > aList1 = new ArrayList<String >();

        aList1.add("");
        aList1.add("");
        aList1.add("");
        aList1.add("");
        aList1.add("");
        aList1.add("");
        aList1.add("");
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        recycle_upcoming.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager1 = new LinearLayoutManager(this);
        recycle_upcoming.setLayoutManager(layoutManager1);

        // specify an adapter (see also next example)
        mAdapter = new AdapterActivity(ActivityClass.this,aList);
        upcomingAdapter = new AdaptorVisitor(ActivityClass.this,aList1);
        recyclerView.setAdapter(mAdapter);
        recycle_upcoming.setAdapter(upcomingAdapter);
        center_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button1IsVisible==true)
                {

                    car1.setVisibility(View.VISIBLE);
                    button1IsVisible = false;
                }
                else if(button1IsVisible==false)
                {
                    // car1.animate().alpha(1.0f);
                    car1.setVisibility(View.GONE);
                    button1IsVisible = true;
                }
            }
        });
        rel_all_visitor.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        all_visitor.setVisibility(View.VISIBLE);
        upcoming_visitor.setVisibility(View.GONE);
        view_all_visitor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
        view_upcoming_visitor.setBackgroundColor(Color.parseColor("#DCDCDC"));    }
});
        rel_upcoming_visitor.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        all_visitor.setVisibility(View.GONE);
        upcoming_visitor.setVisibility(View.VISIBLE);
        view_upcoming_visitor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
        view_all_visitor.setBackgroundColor(Color.parseColor("#DCDCDC"));    }
});
        setting.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent setting=new Intent(getApplicationContext(),SettingActivity.class);
        startActivity(setting);
          }
});

    }

}
