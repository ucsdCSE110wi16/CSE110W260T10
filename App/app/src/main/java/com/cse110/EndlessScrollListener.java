package com.cse110;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class EndlessScrollListener implements OnScrollListener {

    private ContentActivity activity;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;


    public EndlessScrollListener() {
    }

    public EndlessScrollListener(ContentActivity activity, int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
        this.activity = activity;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }

        if (loading) {
            if (totalItemCount > previousTotalItemCount) {
                loading = false;
                previousTotalItemCount = totalItemCount;
                currentPage++;
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                activity.addMorePosts(previousTotalItemCount, 10);
                loading = true;
                Log.d("post", "fetched posts.");
        }

        ListView guidesList =  activity.listView;
        int topRowVerticalPosition = (guidesList == null || guidesList.getChildCount() == 0) ?
                0 : guidesList.getChildAt(0).getTop();

        activity.swipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

        Log.d("post", "We are loading: " + loading);

    }

    public void reset() {
        previousTotalItemCount = 0;
        currentPage = 0;
        loading = true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}