package cn.jhc.wq_util;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import cn.jhc.listview.MyListView;
import cn.jhc.wq_util.Util.TestData;


/**
 * Created by W~Q on 2016/6/4.
 */
public class TestMyListViewActivity extends AppCompatActivity implements MyListView.OnRefreshListener,MyListView.OnLoadMoreListener {
    MyListView myListView;
    ArrayList<String> data=new ArrayList<>();
    ArrayAdapter adapter;
    int page=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_listview);
        myListView= (MyListView) findViewById(R.id.listview);
        data.addAll(TestData.getTestData(page++));
        adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        myListView.setAdapter(adapter);
        myListView.setOnRefreshListener(this);
        myListView.setOnLoadMoreListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page=0;
//                
                data.removeAll(data);
                data.addAll(TestData.getTestData(page++));
                myListView.stopRefresh();
                adapter.notifyDataSetChanged();
            }
        }, 2000); // 2秒后发送消息，停止刷新
    }

    @Override
    public void onLoadMore() {
        if(page==4){
            myListView.isNeedLoadMore(false);
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                data.addAll(TestData.getTestData(page++));
                myListView.stopLoadMore();
                adapter.notifyDataSetChanged();
            }
        }, 3000); // 3秒后发送消息，停止刷新
    }
}