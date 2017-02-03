package com.xiasuhuei321.sample;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiasuhuei321.mrecyclerview.MRecyclerView;

public class MainActivity extends AppCompatActivity {

    private MRecyclerView list;
    private int count = 0;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (MRecyclerView) findViewById(R.id.mrv);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter();
        list.setAdapter(adapter);
//        ImageView ivTest = (ImageView) findViewById(R.id.iv_test);
//        AnimationDrawable drawable = (AnimationDrawable) ivTest.getDrawable();
//        drawable.start();

        list.setOnRefreshListener(new MRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                count++;
                handler.sendEmptyMessageDelayed(1, 2000L);
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SimpleViewHolder(
                    LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.item_refresh, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (count > 0) {
                SimpleViewHolder h = (SimpleViewHolder) holder;
                h.text.setText("这是第 " + count + " 次刷新！");
            }
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        private final TextView text;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
            list.refreshComplete();
        }
    };

}
