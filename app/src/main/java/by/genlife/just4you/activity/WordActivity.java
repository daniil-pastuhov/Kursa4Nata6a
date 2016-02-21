package by.genlife.just4you.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import by.genlife.just4you.R;
import by.genlife.just4you.fragment.WordPageFragment;
import by.genlife.just4you.word.WordDAO;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class WordActivity extends BaseActivity {

    private static final String EXTRA_WORD_ID = "EXTRA_WORD_ID";
    private static final String EXTRA_WORD_RANDOM = "EXTRA_WORD_RANDOM";
    private MyFragmentPagerAdapter pagerAdapter;

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
        long id = getIntent().getLongExtra(EXTRA_WORD_ID, 0L);
        boolean isRandom = getIntent().getBooleanExtra(EXTRA_WORD_RANDOM, false);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), id, isRandom);
        viewPager.setAdapter(pagerAdapter);

    }

    @Override
    protected void onDestroy() {
        pagerAdapter.destroy();
        super.onDestroy();
    }

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private long initPos;
        private boolean isRand;
        private WordDAO mWordDAO;
        private Cursor cursor;

        public MyFragmentPagerAdapter(FragmentManager fm, long id, boolean isRandom) {
            super(fm);
            initPos = id;
            isRand = isRandom;
            mWordDAO = new WordDAO(getApplicationContext());
            cursor = isRand ? mWordDAO.findAllRand(): mWordDAO.findAll();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return WordPageFragment.newInstance(initPos);
            } else {
                cursor.moveToPosition(position);
                return WordPageFragment.newInstance(cursor.getLong(cursor.getColumnIndex(WordDAO.ID)));
            }
        }

        @Override
        public int getCount() {
            return initPos != 0 ? cursor.getCount() : 1;
        }

        public void destroy() {
            cursor.close();
        }
    }
}
