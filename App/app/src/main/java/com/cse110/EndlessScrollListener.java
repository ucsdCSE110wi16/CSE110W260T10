package com.cse110;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class EndlessScrollListener implements OnScrollListener {

    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private ContentActivity activity;
    private boolean loading = true;

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(ContentActivity activity, int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
        this.activity = activity;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }

            ListView guidesList =  activity.listView;
            int topRowVerticalPosition = (guidesList == null || guidesList.getChildCount() == 0) ?
                    0 : guidesList.getChildAt(0).getTop();

            activity.swipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);


        }
        else if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                activity.addMorePosts(previousTotal, 10);
                loading = true;
                Log.d("post", "fetched posts.");

        }

    }

    public void reset() {
        previousTotal = 0;
        currentPage = 0;
        loading = true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}