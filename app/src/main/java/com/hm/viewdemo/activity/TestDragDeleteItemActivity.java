package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hm.viewdemo.R;
import com.hm.viewdemo.databinding.ItemDragDeleteBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by p_dmweidu on 2024/1/23
 * Desc: 测试侧滑删除
 */
public class TestDragDeleteItemActivity extends AppCompatActivity {

    public static final String TAG = TestDragDeleteItemActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<String> stringList;
    private RvAdapter adapter;
    private LinearLayoutManager layoutManager;

    public static void launch(Context context) {
        Intent starter = new Intent(context, TestDragDeleteItemActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drag_delete_item);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        stringList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            stringList.add("string" + i);
        }
        adapter = new RvAdapter(this, stringList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(TestDragDeleteItemActivity.this, "position=" + position, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemDeleteListener(new OnItemDeleteListener() {
            @Override
            public void onItemDelete(int position) {
                stringList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH> {

        private Context context;
        private List<String> stringList;
        private OnItemClickListener onItemClickListener;
        private OnItemDeleteListener onItemDeleteListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
            this.onItemDeleteListener = onItemDeleteListener;
        }

        public RvAdapter(Context context, List<String> stringList) {
            this.context = context;
            this.stringList = stringList;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemDragDeleteBinding binding = ItemDragDeleteBinding.inflate(LayoutInflater.from(context), parent, false);
            return new VH(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            if (onItemClickListener != null) {
                holder.itemLlContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "onClick: position=" + position);
                        onItemClickListener.onItemClick(v, holder.getAdapterPosition());
                    }
                });
            }
            if (onItemDeleteListener != null) {
                holder.itemTextRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemDeleteListener.onItemDelete(holder.getAdapterPosition());
                    }
                });
            }
            holder.itemImageBook.setImageResource(R.mipmap.ic_launcher);
            holder.itemTextBookIsbn.setText(stringList.get(position));
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }

        class VH extends RecyclerView.ViewHolder {

            ImageView itemImageBook;
            TextView itemTextBookIsbn;
            RelativeLayout itemLlContent;
            TextView itemTextRemove;

            public VH(ItemDragDeleteBinding binding) {
                super(binding.getRoot());
                itemImageBook = binding.itemImageBook;
                itemTextBookIsbn = binding.itemTextBookIsbn;
                itemLlContent = binding.itemLlContent;
                itemTextRemove = binding.itemTextRemove;
            }
        }
    }

    interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    interface OnItemDeleteListener {

        void onItemDelete(int position);
    }
}
