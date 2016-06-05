package cn.jhc.listview;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListAdapter;

/**
 * Created by W~Q on 2016/6/5.
 */
interface IListViewMethod {
    void addHeaderView(View v);

    boolean dispatchKeyEvent(KeyEvent event);

    boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event);

    ListAdapter getAdapter();

    long[] getCheckedItemIds();

    int getCheckedItemPosition();

    SparseBooleanArray getCheckedItemPositions();

    int getChoiceMode();

    Drawable getDivider();

    int getDividerHeight();

    int getFooterViewsCount();

    int getHeaderViewsCount();

    boolean getItemsCanFocus();

    int getMaxScrollAmount();

    boolean isItemChecked(int position);

//    void onRestoreInstanceState(Parcelable state);
//    Parcelable onSaveInstanceState();

    boolean onTouchEvent(MotionEvent ev);

    boolean performItemClick(View view, int position, long id);

    boolean removeHeaderView(View v);

    boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate);

    void setAdapter(ListAdapter adapter);

    void setCacheColorHint(int color);

    void setChoiceMode(int choiceMode);

    void setDivider(Drawable divider);

    void setDividerHeight(int height);

    void setFooterDividersEnabled(boolean footerDividersEnabled);

    void setHeaderDividersEnabled(boolean headerDividersEnabled);

    void setItemChecked(int position, boolean value);

    void setItemsCanFocus(boolean itemsCanFocus);

    void setSelection(int position);

    void setSelectionAfterHeaderView();

    void setSelectionFromTop(int position, int y);

}
