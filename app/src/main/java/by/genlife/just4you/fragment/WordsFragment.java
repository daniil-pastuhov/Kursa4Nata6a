package by.genlife.just4you.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import by.genlife.just4you.R;
import by.genlife.just4you.activity.WordActivity;
import by.genlife.just4you.adapter.ListCursorAdapter;
import by.genlife.just4you.db.WordDAO;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class WordsFragment extends BaseFragment implements View.OnClickListener {

    public static final String ACTION_DATA_CHANGED = "ACTION_DATA_CHANGED";

    private RecyclerView mList;
    private WordDAO mWordDAO;
    private ListCursorAdapter adapter;
    private BroadcastReceiver mReceiver = new DataWasChangedReceiver();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setActionBarTitle(R.string.fragment_title_words);
        getActivity().registerReceiver(mReceiver, new IntentFilter(ACTION_DATA_CHANGED));
    }

    @Override
    public void onDetach() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_words, container, false);
        mWordDAO = new WordDAO(getContext());

        v.findViewById(R.id.fab).setOnClickListener(this);
        mList = (RecyclerView) v.findViewById(R.id.words_list);

        adapter = new ListCursorAdapter(getContext(), mWordDAO.findAll());
        mList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(adapter);

        registerForContextMenu(mList);

        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_random) {
            adapter.changeCursor(mWordDAO.findAllRand());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, R.string.btn_delete, Menu.NONE, R.string.btn_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.string.btn_delete:
                mWordDAO.delete(info.id);
                adapter.changeCursor(mWordDAO.findAll());
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        WordActivity.startActivity(getContext());
    }

    class DataWasChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (adapter != null) {
                adapter.changeCursor(mWordDAO.findAll());
            }
        }
    }
}
