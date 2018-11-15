package com.cy.cylibrary.recycler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cy.cylibrary.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能描述:获取数据失败,数据为空展示页面
 */

public class CommonLoadingDataPage extends LinearLayout {
    private LinearLayout mLayout;
    private ImageView mIvIcon;
    private TextView mTvReason;
    private Context mContext;
    private int res;

    public CommonLoadingDataPage(Context context) {
        this(context, null);

    }

    public CommonLoadingDataPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CommonLoadingDataPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View view  = LayoutInflater.from(context).inflate(R.layout.customer_loading_nodata_page, this, true);
        mLayout = view.findViewById(R.id.ll_no_data_layout);
        mIvIcon = view.findViewById(R.id.customer_no_data_iv_icon);
        mTvReason = view.findViewById(R.id.customer_no_data_tv_reason);
    }


    public void setNoDataIcon(int res) {
        this.res = res;
        mIvIcon.setImageResource(res);
    }

    public void setNoDataReason(int res) {
        mTvReason.setText(mContext.getResources().getString(res));
    }

    public void setNoDataReason(String res) {
        mTvReason.setText(res);
    }

    public void setLayoutClickLoadData(OnClickListener onClickListener) {
        mLayout.setOnClickListener(onClickListener);
    }

    public int getNoDataIcon() {
        return res;
    }


    /**
     *网络异常空
     */
    public void setNetError(){
        setNoDataIcon(R.drawable.icon_no_data);
        setNoDataReason(R.string.string_load_data_failure);
    }

    /**
     * 数据空
     */
    public void setDataEmpty(){
        setNoDataIcon(R.drawable.icon_no_data);
        setNoDataReason(R.string.string_load_data_nothing);
    }

}
