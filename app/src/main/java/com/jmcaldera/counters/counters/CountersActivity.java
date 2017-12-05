package com.jmcaldera.counters.counters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jmcaldera.counters.R;
import com.jmcaldera.counters.addcounter.AddCounterActivity;
import com.jmcaldera.counters.counters.helper.RecyclerItemTouchHelper;
import com.jmcaldera.counters.data.model.Counter;
import com.jmcaldera.counters.utils.Injection;
import com.jmcaldera.counters.utils.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CountersActivity extends AppCompatActivity implements CountersContract.View {

    private static final String TAG = CountersActivity.class.getSimpleName();

    private CountersContract.Presenter mPresenter;

    private CountersAdapter mCountersAdapter;

    private static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counters);

        mCountersAdapter = new CountersAdapter(new ArrayList<Counter>(0), mItemListener);

        new CountersPresenter(this, Injection.provideCountersRepository());

        RecyclerView countersList = (RecyclerView) findViewById(R.id.recyclerview_counters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CountersActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        countersList.setLayoutManager(layoutManager);
        countersList.setAdapter(mCountersAdapter);
        countersList.addItemDecoration(new DividerItemDecoration(countersList.getContext(), layoutManager.getOrientation()));

        setupRecyclerViewSwipeBehaviour(countersList);

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        swipeRefreshLayout.setScrollUpChild(countersList);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadCounters(true);
            }
        });
        Button addCounter = (Button) findViewById(R.id.button_add_counter);
        addCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addNewCounter();
            }
        });

    }

    void setupRecyclerViewSwipeBehaviour(RecyclerView recyclerView) {

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                if (viewHolder instanceof CountersAdapter.CounterViewHolder) {
                    String name = mCountersAdapter.mCounters.get(viewHolder.getAdapterPosition()).getTitle();

                    // backup object
                    Counter counter = mCountersAdapter.mCounters.get(viewHolder.getAdapterPosition());
                    int deletedIndex = viewHolder.getAdapterPosition();

                    // remove item form rv
                    mCountersAdapter.removeItem(viewHolder.getAdapterPosition());

                    // show snackbar
                    Snackbar snackbar = Snackbar.make(CountersActivity.this.findViewById(android.R.id.content),
                            "Eliminado",Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(CountersActivity.this.getResources().getColor(R.color.colorAccent));
                    snackbar.show();
                }
            }
        });
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
        mPresenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mPresenter = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_counters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_counter:
                mPresenter.addNewCounter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(@NonNull CountersContract.Presenter presenter) {
        this.mPresenter = checkNotNull(presenter, "Presenter cannot be null");
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        srl.setRefreshing(active);
    }

    @Override
    public void showCounters(List<Counter> counters) {
        mCountersAdapter.replaceData(counters);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_counters);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayout noCountersContainer = (LinearLayout) findViewById(R.id.no_counters_container);
        noCountersContainer.setVisibility(View.GONE);
        LinearLayout totalContainer = (LinearLayout) findViewById(R.id.total_container);
        totalContainer.setVisibility(View.VISIBLE);
        // Get counter sum
        mPresenter.getCountersSum();
    }

    @Override
    public void showNoCounters() {
        //RecyclverView Gone
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_counters);
        recyclerView.setVisibility(View.GONE);
        LinearLayout noCountersContainer = (LinearLayout) findViewById(R.id.no_counters_container);
        noCountersContainer.setVisibility(View.VISIBLE);
        LinearLayout totalContainer = (LinearLayout) findViewById(R.id.total_container);
        totalContainer.setVisibility(View.GONE);
//        mPresenter.getCountersSum();
    }

    @Override
    public void showSuccessMessage() {
        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), "Success!",Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddNewCounter() {
        Intent intent = new Intent(this, AddCounterActivity.class);
        startActivity(intent);
    }

    @Override
    public void showCountersSum(int sum) {
        TextView mTextTotal = (TextView) findViewById(R.id.text_total);
        mTextTotal.setText(getResources().getString(R.string.counter_total, sum));
    }

    @Override
    public boolean isActive() {
        return active;
    }

    CounterItemListener mItemListener = new CounterItemListener() {
        @Override
        public void incrementCounter(String id) {
            mPresenter.incrementCounter(id);
        }

        @Override
        public void decrementCounter(String id) {
            mPresenter.decrementCounter(id);
        }

        @Override
        public void deleteCounter(String id) {
            mPresenter.deleteCounter(id);
        }
    };

    public class CountersAdapter extends RecyclerView.Adapter<CountersAdapter.CounterViewHolder> {

        private List<Counter> mCounters;

        private CounterItemListener mListener;

        public CountersAdapter(List<Counter> counters, CounterItemListener listener) {
            replaceData(counters);
            this.mListener = listener;
        }

        void replaceData(List<Counter> counters) {
            setList(counters);
            notifyDataSetChanged();
        }

        public void removeItem(int position) {
            mListener.deleteCounter(mCounters.get(position).getId());
            mCounters.remove(position);
            notifyItemRemoved(position);
        }

        public void restoreItem(Counter counter, int position) {
            mCounters.add(position, counter);
            notifyItemInserted(position);
        }

        private void setList(List<Counter> counters) {
            this.mCounters = checkNotNull(counters);
        }

        @Override
        public CounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.counter_layout, parent, false);

            return new CounterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CounterViewHolder holder, int position) {

            final Counter counter = mCounters.get(position);
            holder.mTextCounterName.setText(counter.getTitle());
            holder.mTextCount.setText(String.valueOf(counter.getCount()));

            holder.mIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.incrementCounter(counter.getId());
                }
            });

            holder.mDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.decrementCounter(counter.getId());
                }
            });

        }

        @Override
        public int getItemCount() {
            return mCounters.size();
        }

        public class CounterViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextCounterName;
            public TextView mTextCount;
            public AppCompatImageButton mIncrementButton;
            public AppCompatImageButton mDecrementButton;
            public RelativeLayout mBackgroundContainer;
            public RelativeLayout mForegroundContainer;

            public CounterViewHolder(View itemView) {
                super(itemView);

                mTextCounterName = (TextView) itemView.findViewById(R.id.text_counter_name);
                mTextCount = (TextView) itemView.findViewById(R.id.text_count);
                mIncrementButton = (AppCompatImageButton) itemView.findViewById(R.id.button_increment);
                mDecrementButton = (AppCompatImageButton) itemView.findViewById(R.id.button_decrement);
                mBackgroundContainer = (RelativeLayout) itemView.findViewById(R.id.background_container);
                mForegroundContainer = (RelativeLayout) itemView.findViewById(R.id.foreground_container);
            }
        }
    }

    public interface CounterItemListener {

        void incrementCounter(String id);
        void decrementCounter(String id);
        void deleteCounter(String id);
    }
}
