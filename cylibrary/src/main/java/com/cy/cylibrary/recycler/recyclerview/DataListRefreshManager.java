package com.cy.cylibrary.recycler.recyclerview;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cy.cylibrary.R;
import com.cy.cylibrary.recycler.common.CommonAdapter;
import com.cy.cylibrary.recycler.common.base.ViewHolder;
import com.cy.cylibrary.recycler.view.CommonLoadingDataPage;

import java.util.ArrayList;
import java.util.List;


/**
 * 数据列表分页管理类
 * @author Administrator
 */
public class DataListRefreshManager<T> {
    /**
     * 获取列表数据
     */
    public List<T> getDataList() {
        return mList;
    }

    private IDataListRefreshManager mRefreshCallBack;
    /** 无数据展示控件 */
    private CommonLoadingDataPage mLayoutNoData;
    private String mNoDataReason = "";
    private int mNoDataImgResId;

    private Activity mActivity;
    private LRecyclerView mRecyclerView;
    private LRecyclerViewAdapter mAdapter;
    private List<T> mList = new ArrayList<>();

    /** 服务器端一共多少条数据*/
    private int totalPage;
    /** 每一页展示多少条数据 */
    private final int REQUEST_COUNT = 30;
    /** 当前页数 */
    private int mCurrentPage = 1;


    public interface IDataListRefreshManager<T> {
        /**
         * 获取列表条目布局
         */
        int getItemLayoutId();

        /**
         * 加载列表数据请求
         */
        void loadData(int currentPage, int pageSize);

        /**
         * 列表条目数据填充
         */
        void convert(ViewHolder holder, T entity, int position);

        /**
         * 列表条目点击事件
         */
        void onItemClick(View view, int position, T data);

    }

    /**
     * 提供给外部的Method
     * @param activity
     * @param recyclerView
     * @param refreshCallBack
     */
    public DataListRefreshManager(Activity activity, LRecyclerView recyclerView, IDataListRefreshManager refreshCallBack) {
        this.mActivity = activity;
        this.mRecyclerView = recyclerView;
        this.mRefreshCallBack = refreshCallBack;
        initRecyclerView();
    }

    /**
     * 加载列表数据
     */
    public void loadData() {
        mRefreshCallBack.loadData(mCurrentPage, REQUEST_COUNT);
    }

    /**
     * 加载首页数据
     */
    public void loadFirstPageData() {
        mCurrentPage = 1;
        mRecyclerView.refresh();
    }


    /**
     * 设置无数据时的布局
     *
     * @param noDataLayout 无数据控件
     * @param noDataReason 无数据提示文字
     */
    public void setNoDataLayout(CommonLoadingDataPage noDataLayout, String noDataReason) {
        setNoDataLayout(noDataLayout, noDataReason, R.drawable.icon_no_data);
    }

    /**
     * 设置无数据时的布局及提示文字
     * @param noDataReason 无数据提示文字
     */
    public void setNoDataLayoutText(String noDataReason){
        if(mLayoutNoData == null){
            return;
        }
        this.mNoDataReason = noDataReason;
        mLayoutNoData.setNoDataReason(mNoDataReason);
    }


    /**
     * 设置无数据时的布局
     *
     * @param noDataLayout   无数据控件
     * @param noDataReason   无数据提示文字
     * @param noDataImgResId 无数据提示图片
     */
    public void setNoDataLayout(CommonLoadingDataPage noDataLayout, String noDataReason, int noDataImgResId) {
        this.mLayoutNoData = noDataLayout;
        this.mNoDataReason = noDataReason;
        this.mNoDataImgResId = noDataImgResId;
        noDataLayout.setOnClickListener(v -> {
            // 无数据 、 请求失败 重新加载
            loadFirstPageData();
        });
    }

    private Activity getActivity() {
        return mActivity;
    }

    private void initRecyclerView() {
        mAdapter = new LRecyclerViewAdapter(setBaseAdapter());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setOnRefreshListener(() -> {
            mCurrentPage = 1;
            loadData();
        });

        mRecyclerView.setOnLoadMoreListener(() -> {
            if (mCurrentPage < totalPage) {
                mCurrentPage++;
                loadData();
            } else {
                mRecyclerView.setNoMore(true);
            }
        });
        mAdapter.setOnItemClickListener((view, position) -> {
            mRefreshCallBack.onItemClick(view, position, mList.get(position));
        });
    }

    private CommonAdapter setBaseAdapter() {
        return new CommonAdapter<T>(mActivity, mRefreshCallBack.getItemLayoutId(), mList) {
            @Override
            protected void convert(ViewHolder holder, T entity, int position) {
                mRefreshCallBack.convert(holder, entity, position);
            }
        };
    }

    public void addHeader(View v) {
        if (v == null) {
            return;
        }
        if (mCurrentPage == 1) {
            mAdapter.removeHeaderView();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(lp);
            mAdapter.addHeaderView(v);
        }
    }

    /**
     * 加载数据成功
     */
    public void loadDataSucess(int total, List<T> dataList) {
        totalPage = total;
        if (mCurrentPage == 1) {
            mList.clear();
        }
        mList.addAll(dataList);

        mRecyclerView.refreshComplete(REQUEST_COUNT);

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        if (mLayoutNoData != null) {
            mRecyclerView.setEmptyView(mLayoutNoData);
            mLayoutNoData.setNoDataReason(mNoDataReason);
            mLayoutNoData.setNoDataIcon(mNoDataImgResId);
        }
    }

    /**
     * 加载列表数据失败
     */
    public void loadDataFailure() {
        //获取网络数据失败
        mRecyclerView.refreshComplete(REQUEST_COUNT);
        mAdapter.notifyDataSetChanged();
        if (mLayoutNoData != null) {
            mRecyclerView.setEmptyView(mLayoutNoData);
            mLayoutNoData.setNetError();
        }

        mRecyclerView.setOnNetWorkErrorListener(() -> {
            mCurrentPage--;
            loadData();
        });
    }

    /**
     * 加载列表数据失败
     */
    public void loadDataFailureNoRule() {
        //获取网络数据失败
        mRecyclerView.refreshComplete(REQUEST_COUNT);
        mAdapter.notifyDataSetChanged();
        if (mLayoutNoData != null) {
            mRecyclerView.setEmptyView(mLayoutNoData);
        }
        mRecyclerView.setOnNetWorkErrorListener(() -> {
            mCurrentPage--;
            loadData();
        });
    }

    public void notifyItemRemoved(T bean) {
        mAdapter.notifyItemRemoved(mList.indexOf(bean) + 2);
        mList.remove(bean);
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    public LRecyclerViewAdapter getLAdapter() {
        return mAdapter;
    }


    public void notifyItemRemovedOther(T bean) {
        mAdapter.notifyItemRemoved(mList.indexOf(bean) + 1);
        mList.remove(bean);
        if(mList.size() <= 0){
            loadFirstPageData();
        }
    }
}
