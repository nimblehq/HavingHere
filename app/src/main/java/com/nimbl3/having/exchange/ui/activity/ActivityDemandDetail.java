package com.nimbl3.having.exchange.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nimbl3.having.exchange.R;

/**
 * Created by thuypham on 3/15/2018 AD.
 */

public class ActivityDemandDetail extends ActivityBase {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDemandDetail.this, ActivityChat.class);
                startActivity(intent);
            }
        });
    }
}
