package com.example.todocallbacksdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todocallbacksdemo.R;
import com.example.todocallbacksdemo.interfaces.ItemClickInterface;
import com.example.todocallbacksdemo.model.ItemsModel;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.listViewHolder> {
    private List<ItemsModel> itemsModelList;
    private Context context;
    public ItemClickInterface deleteListener;


    public ListItemAdapter(List<ItemsModel> itemsModelList, Context context,ItemClickInterface deleteListener) {
        this.itemsModelList = itemsModelList;
        this.context = context;
        this.deleteListener=deleteListener;
    }

    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listViewHolder holder, final int position) {
        ItemsModel itemsModel=itemsModelList.get(position);
        holder.tvName.setText(itemsModel.getName());
        holder.tvNum.setText(""+itemsModel.getNum());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"open new acitivity",Toast.LENGTH_LONG).show();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.deleteitem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(itemsModelList==null){
            return 0;
        }
        return itemsModelList.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvNum;
        Button btnDelete;
        public listViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name);
            tvNum=itemView.findViewById(R.id.tv_num);
            btnDelete=itemView.findViewById(R.id.btn_del);
        }
    }


}

