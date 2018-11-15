package com.cy.cylibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.cy.cylibrary.utils.CLogger;

/**
 * Created by cy on 2018/6/5.
 * 高度为子child的ListView(自适应ScrollView的ListView)
 */
public class WarpHeightListView extends ListView{
    public WarpHeightListView(Context context)
    {
        super(context);
    }

    public WarpHeightListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public WarpHeightListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //解决当ScrollView里嵌套listView，listView自适应高度，当然GridView也可以采用这个方法，不过GridView要判断是否可滑动
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        CLogger.d("WarpHeightListView MeasureSpec = " + MeasureSpec.toString(expandSpec));//测试MeasureSpec = MeasureSpec: AT_MOST 536870911
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
