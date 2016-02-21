package by.genlife.just4you.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import by.genlife.just4you.R;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private boolean mDestroyed;
    private ProgressDialog mProgressDialog;

    protected Toolbar mToolbar;

    protected abstract int getContentView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentView());

        initToolbar();

        mDestroyed = false;

        init(savedInstanceState);
    }

    private void initToolbar() {
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            // mToolbar.setTitle("");
            // mToolbar.setLogo(R.drawable.ic_toolbar_logo);
            setSupportActionBar(mToolbar);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void init(Bundle savedInstanceState) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDestroyed = true;
    }

    public boolean hasBeenDestroyed() {
        return mDestroyed;
    }

    public void hideProgressDialog() {
        if (mProgressDialog == null || hasBeenDestroyed()) {
            return;
        }

        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    protected void showKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void hideKeyboard() {
        if (hasBeenDestroyed()) {
            return;
        }

        View view = getCurrentFocus();

        if (view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public Toolbar getToolbar() {
        if (mToolbar != null) {
            return mToolbar;
        } else {
            throw new IllegalStateException("Toolbar was not initialized. Please initialize it");
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
