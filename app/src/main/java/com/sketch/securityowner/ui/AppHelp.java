package com.sketch.securityowner.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sketch.securityowner.Adapter.AppHelpAdapter;
import com.sketch.securityowner.R;
import com.sketch.securityowner.dialogs.LoaderDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppHelp extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    LoaderDialog loaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_help);
        ButterKnife.bind(this);
        actionViews();

    }

    private void actionViews(){

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("App Tour");

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        recycler_view.setLayoutManager(new LinearLayoutManager(this));


        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Shield Authorise, Validate and Manage your Gate");
        arrayList.add("Receive Notification with photo before anyone knocks at your door");
        arrayList.add("Do Authorise when your Delivery Arrives and have option to leave the material at gate also");
        arrayList.add("Do Authorise when your Child goes outside");
        arrayList.add("Pre Approve before your cab arrives");
        arrayList.add("You can send domestic threat alert to the security guard");
        arrayList.add("You can Pay your maintenance bill from the App and check statement");
        arrayList.add("You can manage your multiple property (home & office) from the App only No need to visit");
        arrayList.add("You can also manage your rented property from the App only No need to visit");
        arrayList.add("You can validate, authorise and manage shield from anywhere");


        AppHelpAdapter appHelpAdapter = new AppHelpAdapter(AppHelp.this, arrayList);
        recycler_view.setAdapter(appHelpAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }



}
