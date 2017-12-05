package com.jmcaldera.counters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jmcaldera.counters.data.CountersRepository;
import com.jmcaldera.counters.data.DataSource;
import com.jmcaldera.counters.data.model.Counter;
import com.jmcaldera.counters.data.remote.RemoteDataSource;

import java.util.List;

public class CountersActivity extends AppCompatActivity {

    private static final String TAG = CountersActivity.class.getSimpleName();

    private CountersRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counters);

        mRepository = CountersRepository.getInstance(RemoteDataSource.getInstance());

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRepository.getCounters(new DataSource.LoadCountersCallback() {
                    @Override
                    public void onSuccess(List<Counter> counters) {
                        Log.d(TAG, "Counters: " + counters.toString());
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Counters error: " + error.getMessage());
                    }
                });
            }
        });

    }
}
