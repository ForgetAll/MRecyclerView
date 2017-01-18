package com.xiasuhuei321.sample;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiasuhuei321.mrecyclerview.MRecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MRecyclerView list = (MRecyclerView) findViewById(R.id.mrv);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new MyAdapter());
//        list.setAdapter();
        ImageView ivTest = (ImageView) findViewById(R.id.iv_test);
        AnimationDrawable drawable = (AnimationDrawable) ivTest.getDrawable();
        drawable.start();
    }

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SimpleViewHolder(
                    LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.item_test, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
