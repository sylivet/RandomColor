package com.test.randomcolor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "RandomColor";

    private RecyclerViewUtil mRecyclerViewUtil;
    private int START_POS = 18;

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    String color;
    ArrayList<String> link = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //設置RecycleView
        recyclerView = findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        //設置初始資料
        for (int i = 0; i < 18; i++) {
            color = getColor();
            link.add("https://dummyimage.com/300x300/" + color + "/ffffff&text=" + color);
        }
        Log.d(TAG, String.valueOf(link.size()));
        mRecyclerViewUtil=new RecyclerViewUtil(recyclerView);
        mRecyclerViewUtil.setRecyclerViewLoadMoreListener(new RecyclerViewUtil.RecyclerViewLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Toast.makeText(getApplicationContext(),"已經到底，加載中",Toast.LENGTH_SHORT).show();
                mRecyclerViewUtil.setLoadMoreEnable(false);
                load(START_POS,(int)(Math.random()*36)+18);


            }
        });
    }

    private void load(int startPos, int count) {
        if (link.size()+count<=300){
            for (int i = 0; i < count; i++) {
                color = getColor();
                link.add("https://dummyimage.com/300x300/" + color + "/ffffff&text=" + color);
            }
            myAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "加載了" + count + "條資料", Toast.LENGTH_SHORT).show();
            mRecyclerViewUtil.setLoadMoreEnable(true);
        }else{
            for (int i = link.size(); i < 300; i++) {
                color = getColor();
                link.add("https://dummyimage.com/300x300/" + color + "/ffffff&text=" + color);
            }
            myAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "已無最新資料", Toast.LENGTH_SHORT).show();
            mRecyclerViewUtil.setLoadMoreEnable(false);
        }
        Log.d(TAG, String.valueOf(link.size()));

        START_POS = startPos + count;
    }


    public String getColor() {
        String str = "";
        //一個十六進位制的值得陣列
        String arr[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

        for (int i = 0; i < 6; i++) {
            //產生的每個隨機數都是一個索引，根據索引找到陣列中對應的值，拼接到一起
            int num = (int) (Math.random() * 16);
            str += arr[num];
        }
        return str;
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @SuppressLint("NotifyDataSetChanged")
        public void refreshList() {
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        //連接layout檔案，return一個View
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        //在這裡取得元件的控制(每個item內的控制)
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(MainActivity.this).load(link.get(position)).into(holder.imageView);

        }

        @Override
        //取得顯示數量，return一個int，通常都會return陣列長度(arrayList.size)
        public int getItemCount() {
            return link.size();
        }

        //取用方法並在內部連結所需要的控件
        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}