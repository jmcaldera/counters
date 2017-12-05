package com.jmcaldera.counters.counters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jmcaldera.counters.R;
import com.jmcaldera.counters.addcounter.AddCounterActivity;
import com.jmcaldera.counters.data.model.Counter;
import com.jmcaldera.counters.utils.Injection;

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

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadCounters(true);
            }
        });

        mCountersAdapter = new CountersAdapter(new ArrayList<Counter>(0), mItemListener);

        new CountersPresenter(this, Injection.provideCountersRepository());

        RecyclerView countersList = (RecyclerView) findViewById(R.id.recyclerview_counters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CountersActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        countersList.setLayoutManager(layoutManager);
        countersList.setAdapter(mCountersAdapter);
        countersList.addItemDecoration(new DividerItemDecoration(countersList.getContext(), layoutManager.getOrientation()));
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
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showCounters(List<Counter> counters) {
        mCountersAdapter.replaceData(counters);

        // TODO: add no counters view
        // RecyclerView visible
        // No counters gone

        // Get counter sum
        mPresenter.getCountersSum();
    }

    @Override
    public void showNoCounters() {
        //TODO show no counters view
        //RecyclverView Gone
        // No counters visible. set drawable
        Toast.makeText(this, "No hay counters!", Toast.LENGTH_SHORT).show();
        mPresenter.getCountersSum();
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
//        mTextTotal.setText(String.format(getResources().getString(R.string.counter_total), sum));
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

            // TODO: swipe para borrar el contador
        }

        @Override
        public int getItemCount() {
            return mCounters.size();
        }

        public class CounterViewHolder extends RecyclerView.ViewHolder {

            TextView mTextCounterName;
            TextView mTextCount;
            Button mIncrementButton;
            Button mDecrementButton;

            public CounterViewHolder(View itemView) {
                super(itemView);

                mTextCounterName = (TextView) itemView.findViewById(R.id.text_counter_name);
                mTextCount = (TextView) itemView.findViewById(R.id.text_count);
                mIncrementButton = (Button) itemView.findViewById(R.id.button_increment);
                mDecrementButton = (Button) itemView.findViewById(R.id.button_decrement);
            }
        }
    }

    public interface CounterItemListener {

        void incrementCounter(String id);
        void decrementCounter(String id);
        void deleteCounter(String id);
    }
}
