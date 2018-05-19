package com.wang.materialdesigntest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLauyout;
    private NavigationView navView;
    private FloatingActionButton fab;

    RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Fruit[] fruits = {new Fruit("Apple", R.drawable.apple), new Fruit("Banana", R.drawable.banana),
            new Fruit("Orange", R.drawable.orange), new Fruit("Watermelon", R.drawable.watermelon),
            new Fruit("Pear", R.drawable.pear), new Fruit("Grape", R.drawable.grape),
            new Fruit("Pineapple", R.drawable.pineapple), new Fruit("Strawberry", R.drawable.strawberry),
            new Fruit("Cherry", R.drawable.cherry), new Fruit("Mango", R.drawable.mango)};
    private List<Fruit> fruitList=new ArrayList<>();
    private FruitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        mDrawerLauyout=(DrawerLayout)findViewById(R.id.draw_layout);
        navView=(NavigationView)findViewById(R.id.nav_view);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               mDrawerLauyout.closeDrawers();
               return  true;
            }
        });

        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity.this,"FAB clicked",Toast.LENGTH_SHORT).show();
                Snackbar.make(v,"Data deleted",Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this,"Data restored",Toast.LENGTH_SHORT).show();

                            }
                        }).show();

            }
        });

        intitFruits();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);


        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {   //请求数据（网络或本地）操作
                refreshFruits();
            }
        });

    }


    private void intitFruits(){
        fruitList.clear();
        for(int i=0;i<50;i++){
            Random random=new Random();
            int index=random.nextInt(fruits.length);
            fruitList.add(fruits[index]);

        }
    }

    private void refreshFruits(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000); //模拟耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        intitFruits();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false); //刷新事件结束，隐藏进度条
                    }
                });


            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLauyout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}
