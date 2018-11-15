package com.cy.cylibrary.recycler.common;

import android.content.Context;
import android.view.LayoutInflater;
import com.cy.cylibrary.recycler.common.base.ItemViewDelegate;
import com.cy.cylibrary.recycler.common.base.ViewHolder;
import java.util.List;

/**
 * Android 万能的Adapter for ListView,RecyclerView,GridView等，支持多种Item类型的情况。
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T>
{
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }



    protected abstract void convert(ViewHolder holder, T t, int position);


}
