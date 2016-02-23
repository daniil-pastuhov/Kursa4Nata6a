package by.genlife.just4you.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import by.genlife.just4you.R;
import by.genlife.just4you.fragment.WordPageFragment;
import by.genlife.just4you.db.WordDAO;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class WordActivity extends BaseActivity {

    private static final String EXTRA_WORD_ID = "EXTRA_WORD_ID";
    private static final String EXTRA_WORD_RANDOM = "EXTRA_WORD_RANDOM";
    private MyFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    public static void startActivity(Context context) {
        startActivity(context, 0, false);
    }

    public static void startActivity(Context context, long id) {
        startActivity(context, id, false);
    }

    public static void startActivity(Context context, long id, boolean isRandom) {
        Intent intent = new Intent(context, WordActivity.class);
        intent.putExtra(EXTRA_WORD_ID, id);
        intent.putExtra(EXTRA_WORD_RANDOM, isRandom);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_word;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        long id = getIntent().getLongExtra(EXTRA_WORD_ID, 0L);
        boolean isRandom = getIntent().getBooleanExtra(EXTRA_WORD_RANDOM, false);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), id, isRandom);
        viewPager.setAdapter(pagerAdapter);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.word_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                pagerAdapter.deleteCurrent();
                return true;

//            case R.id.action_favorite:
//                pagerAdapter.changeFavorite();
//                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private long initItemId;
        private boolean isRand;
        private WordDAO mWordDAO;
        private ArrayList<Long> wordsList;

        public MyFragmentPagerAdapter(FragmentManager fm, long id, boolean isRandom) {
            super(fm);
            initItemId = id;
            isRand = isRandom;
            mWordDAO = new WordDAO(getApplicationContext());
            Cursor cursor = isRand ? mWordDAO.findAllRand() : mWordDAO.findAll();
            wordsList = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                wordsList.add(cursor.getLong(cursor.getColumnIndex(WordDAO.ID)));
            }
            if (initItemId != 0) {
                wordsList.remove(Long.valueOf(initItemId));
                wordsList.add(0, initItemId);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return WordPageFragment.newInstance(initItemId == 0 ? 0 : wordsList.get(position));
        }

        @Override
        public int getCount() {
            return initItemId != 0 ? wordsList.size() : 1;
        }

        public void deleteCurrent() {
            if (initItemId != 0) {
                int pos = viewPager.getCurrentItem();
                mWordDAO.delete(wordsList.get(pos));
            }
            finish();
        }
    }
}
