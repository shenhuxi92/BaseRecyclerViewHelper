package com.laxy.recyclerview.helper;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.laxy.recyclerview.library.BaseAdapter;
import com.laxy.recyclerview.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> data = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            data.add(String.valueOf(i));
        }

        RecyclerView main = findViewById(R.id.main_list);

        final MainAdapter adapter = new MainAdapter(data);
        adapter.setEmptyLayout(R.layout.item_main_empty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        main.setLayoutManager(layoutManager);
        main.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<String> data = new ArrayList<>();
                for (int i = 0; i < position; i++) {
                    data.add(i + "= in");
                }
                adapter.updateData(data);
            }
        });
        adapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Toast(position + " = position");
                adapter.removeData(position);
            }
        });
        adapter.setOnItemViewClickListener(
                R.id.item_main_text,
                new BaseAdapter.OnItemViewClickListener() {
                    @Override
                    public void onItemViewClick(View view, int position) {
                        List<String> data = new ArrayList<>();
                        for (int i = 0; i < position; i++) {
                            data.add(i + "= in");
                        }
                        adapter.addData(data);
                    }
                }
        );
    }

    public void Toast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    class MainAdapter extends BaseAdapter<String> {

        MainAdapter(List<String> data) {
            super(data);
        }

        @Override
        protected int setLayout() {
            return R.layout.item_main;
        }

        @Override
        protected void bindViewData(@NonNull BaseViewHolder holder, String data, int position) {
            TextView textView = holder.findViewById(R.id.item_main_text);
            textView.setText(data);
        }

        @Override
        public void bindEmptyView(View itemView) {
            super.bindEmptyView(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> data = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        data.add(i + "= in");
                    }
                    addData(data);
                }
            });
        }
    }

}
