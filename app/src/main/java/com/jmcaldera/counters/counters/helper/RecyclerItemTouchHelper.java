package com.jmcaldera.counters.counters.helper;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.jmcaldera.counters.counters.CountersActivity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jmcaldera on 05/12/2017.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener mListener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, @NonNull RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.mListener = checkNotNull(listener);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foregroundView = ((CountersActivity.CountersAdapter.CounterViewHolder) viewHolder).mForegroundContainer;
            getDefaultUIUtil().onSelected(foregroundView);
        }
//        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((CountersActivity.CountersAdapter.CounterViewHolder) viewHolder).mForegroundContainer;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
//        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View foregroundView = ((CountersActivity.CountersAdapter.CounterViewHolder) viewHolder).mForegroundContainer;
        getDefaultUIUtil().clearView(foregroundView);
        //        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((CountersActivity.CountersAdapter.CounterViewHolder) viewHolder).mForegroundContainer;
        getDefaultUIUtil().onDraw(c, recyclerView,foregroundView, dX, dY, actionState, isCurrentlyActive);
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mListener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

}
