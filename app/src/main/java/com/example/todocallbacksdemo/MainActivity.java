package com.example.todocallbacksdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.todocallbacksdemo.adapter.ListItemAdapter;
import com.example.todocallbacksdemo.interfaces.ItemClickInterface;
import com.example.todocallbacksdemo.model.ItemsModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemClickInterface{
    RecyclerView recyclerView;
    ListItemAdapter listItemAdapter;
    //Context context;
    ArrayList<ItemsModel> itemsModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ItemsModel itemsModel1 = new ItemsModel("Rj", 1);
        ItemsModel itemsModel2 = new ItemsModel("Rj", 2);
        ItemsModel itemsModel3 = new ItemsModel("Rj", 3);
        ItemsModel itemsModel4 = new ItemsModel("Rj", 4);
        ItemsModel itemsModel5 = new ItemsModel("Rj", 5);
        ItemsModel itemsModel6 = new ItemsModel("Rj", 6);
        itemsModelArrayList = new ArrayList<ItemsModel>();
        itemsModelArrayList.add(itemsModel1);
        itemsModelArrayList.add(itemsModel2);
        itemsModelArrayList.add(itemsModel3);
        itemsModelArrayList.add(itemsModel4);
        itemsModelArrayList.add(itemsModel5);
        itemsModelArrayList.add(itemsModel6);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
       /* listItemAdapter = new ListItemAdapter(itemsModelArrayList, this, new ItemClickInterface() {
            @Override
            public void deleteitem(int position) {
                itemsModelArrayList.remove(position);
                listItemAdapter.notifyItemRemoved(position);
            }
        });*/

        listItemAdapter = new ListItemAdapter(itemsModelArrayList, this,this);
                recyclerView.setAdapter(listItemAdapter);
        listItemAdapter.notifyDataSetChanged();


    }


    @Override
    public void deleteitem(int position) {
        itemsModelArrayList.remove(position);
        listItemAdapter.notifyItemRemoved(position);
    }

    @Override
    public void showToast() {
    //activity khulwani hain
    //dont pass intent from adaptor that is wrong because context ka khel hain wo gaya toh crash hoga
    }
}

