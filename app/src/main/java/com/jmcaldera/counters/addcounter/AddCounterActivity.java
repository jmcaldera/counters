package com.jmcaldera.counters.addcounter;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jmcaldera.counters.R;
import com.jmcaldera.counters.utils.Injection;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddCounterActivity extends AppCompatActivity implements AddCounterContract.View {

    private static final String TAG = AddCounterActivity.class.getSimpleName();

    private AddCounterContract.Presenter mPresenter;

    private static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_counter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Crear contador");
        }
        new AddCounterPresenter(this, Injection.provideCountersRepository());
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
        getMenuInflater().inflate(R.menu.menu_add_counter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save_counter:
                validateTitle();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validateTitle() {
        String error = null;

        TextInputEditText editText = (TextInputEditText) findViewById(R.id.edittext_title);
        if (TextUtils.isEmpty(editText.getText())) {
            error = "Debe colocar un nombre";
            toggleTextInputError(error);
        } else {
            mPresenter.addCounter(editText.getText().toString());
        }
    }

    private void toggleTextInputError(String error) {
        TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
        inputLayout.setError(error);
        if (error == null) {
            inputLayout.setErrorEnabled(false);
        } else {
            inputLayout.setErrorEnabled(true);
        }
    }

    @Override
    public void setPresenter(@NonNull AddCounterContract.Presenter presenter) {
        this.mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showAddCounterSuccess() {
        Toast.makeText(this, "Contador guardado!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showAddCounterError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
