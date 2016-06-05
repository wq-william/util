package cn.jhc.listview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by W~Q on 2016/6/4.
 * 集成listView的上拉加载更多，和下拉刷新功能、
 * （实际集成非ListView,使用5.0下拉刷新的原理，现在的方式是复写listView的常用方法）
 */
public class MyListView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, IListViewMethod {
    private ListView listView;
    private LinearLayout footerView;
    private String loadMoreContent = "正在获取更多...";
    private OnRefreshListener refreshListener;
    private OnLoadMoreListener loadMoreListener;

    private int colorAccent = android.R.color.holo_blue_bright;
    private int colorRefreshBackground = Color.WHITE;
    private int progressWidth = 80;
    private int progressHeight = progressWidth;
    private int footerViewPadding = 12;
    private int progressTextPadding = 34;

    private boolean isRefreshing = false;//正在刷新
    private boolean isLoadingMore = false;//正在加载更多
    private boolean isNeedLoadMore = true;

    public MyListView(Context context) {
        super(context);
        init(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        initListView(context);
        initFootView(context);
        initDefaultView();
        initListener();
        initData();
    }

    private void initData() {
        isRefreshing = false;//正在刷新
        isLoadingMore = false;//正在加载更多
        isNeedLoadMore = true;
    }

    private void initFootView(Context context) {
        ProgressBar progressBar = new ProgressBar(context);
        LayoutParams params = new LayoutParams(progressWidth, progressHeight);
        progressBar.setLayoutParams(params);

        TextView textView = new TextView(context);
        textView.setText(loadMoreContent);
        textView.setPadding(progressTextPadding - 10, progressTextPadding, progressTextPadding + 40, progressTextPadding);

        footerView = new LinearLayout(context);
        footerView.setOrientation(LinearLayout.HORIZONTAL);
        footerView.setPadding(footerViewPadding, footerViewPadding, footerViewPadding, footerViewPadding);
        footerView.setGravity(Gravity.CENTER);
        footerView.addView(progressBar);
        footerView.addView(textView);
    }

    private void initListener() {
        setOnRefreshListener(this);
    }

    private void initDefaultView() {
        setColorSchemeResources(colorAccent);
        setProgressBackgroundColorSchemeColor(colorRefreshBackground);
    }

    private void initListView(Context context) {
        listView = new ListView(context);
        listView.setOnScrollListener(this);
        addView(listView);
    }

    /**
     * 结束刷新
     */
    public void stopRefresh() {
        setRefreshing(false);
        listView.smoothScrollToPosition(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(listView.getFirstVisiblePosition()==0){
                        isRefreshing = false;
                        break;
                    }
                }
            }
        }).start();
    }

    /**
     * 结束加载更多
     */
    public void stopLoadMore() {
        if (listView.getFooterViewsCount() > 0) {
            listView.removeFooterView(footerView);
        }
        isLoadingMore = false;
    }

    @Override
    public void onRefresh() {
        if (refreshListener != null) {
            isNeedLoadMore = true;
            isRefreshing = true;
            refreshListener.onRefresh();
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount>0 && (firstVisibleItem + visibleItemCount) >= (totalItemCount - 1)) {//倒数第二条
            int footerCount = listView.getFooterViewsCount();
            synchronized (this) {
                if (footerCount == 0 && !isRefreshing) {
                    if (loadMoreListener != null && isNeedLoadMore && !isLoadingMore) {
                        listView.addFooterView(footerView);
                        isLoadingMore = true;
                        loadMoreListener.onLoadMore();
                    }
                }
            }
        }
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void isNeedLoadMore(boolean isNeedLoadMore) {
        this.isNeedLoadMore = isNeedLoadMore;
        if (!isNeedLoadMore) {
            stopLoadMore();
        }
    }

    public OnRefreshListener getRefreshListener() {
        return refreshListener;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.refreshListener = listener;
    }

    public OnLoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void addHeaderView(View v) {
        listView.addHeaderView(v);
    }

    @Override
    public ListAdapter getAdapter() {
        return listView.getAdapter();
    }

    @Override
    public long[] getCheckedItemIds() {
        return listView.getCheckedItemIds();
    }

    @Override
    public int getCheckedItemPosition() {
        return listView.getCheckedItemPosition();
    }

    @Override
    public SparseBooleanArray getCheckedItemPositions() {
        return listView.getCheckedItemPositions();
    }

    @Override
    public int getChoiceMode() {
        return listView.getChoiceMode();
    }

    @Override
    public Drawable getDivider() {
        return listView.getDivider();
    }

    @Override
    public int getDividerHeight() {
        return listView.getDividerHeight();
    }

    @Override
    public int getFooterViewsCount() {
        return listView.getFooterViewsCount();
    }

    @Override
    public int getHeaderViewsCount() {
        return listView.getHeaderViewsCount();
    }

    @Override
    public boolean getItemsCanFocus() {
        return listView.getItemsCanFocus();
    }

    @Override
    public int getMaxScrollAmount() {
        return listView.getMaxScrollAmount();
    }

    @Override
    public boolean isItemChecked(int position) {
        return listView.isItemChecked(position);
    }

    @Override
    public boolean performItemClick(View view, int position, long id) {
        return listView.performItemClick(view, position, id);
    }

    @Override
    public boolean removeHeaderView(View view) {
        return listView.removeHeaderView(view);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    @Override
    public void setCacheColorHint(int color) {
        listView.setCacheColorHint(color);
    }

    @Override
    public void setChoiceMode(int choiceMode) {
        listView.setChoiceMode(choiceMode);
    }

    @Override
    public void setDivider(Drawable divider) {
        listView.setDivider(divider);
    }

    @Override
    public void setDividerHeight(int height) {
        listView.setDividerHeight(height);
    }

    @Override
    public void setFooterDividersEnabled(boolean footerDividersEnabled) {
        listView.setFooterDividersEnabled(footerDividersEnabled);
    }

    @Override
    public void setHeaderDividersEnabled(boolean headerDividersEnabled) {
        listView.setHeaderDividersEnabled(headerDividersEnabled);
    }

    @Override
    public void setItemChecked(int position, boolean value) {
        listView.setItemChecked(position, value);
    }

    @Override
    public void setItemsCanFocus(boolean itemsCanFocus) {
        listView.setItemsCanFocus(itemsCanFocus);
    }

    @Override
    public void setSelection(int position) {
        listView.setSelection(position);
    }

    @Override
    public void setSelectionAfterHeaderView() {
        listView.setSelectionAfterHeaderView();
    }

    @Override
    public void setSelectionFromTop(int position, int y) {
        listView.setSelectionFromTop(position, y);
    }

}
