package by.genlife.just4you.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import by.genlife.just4you.R;
import by.genlife.just4you.activity.ThemeActivity;
import by.genlife.just4you.db.TopicDAO;
import by.genlife.just4you.model.Topic;

/**
 * Created by NotePad.by on 23.02.2016.
 */
public class TopicsFragment extends BaseFragment implements View.OnClickListener {


    private static final int REQUEST_CREATE_TOPIC = 100;
    private ListView mList;
    private TopicDAO topicDAO;
    private SimpleCursorAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setActionBarTitle(R.string.fragment_title_topics);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topics, container, false);
        topicDAO = new TopicDAO(getContext());

        v.findViewById(R.id.fab).setOnClickListener(this);
        mList = (ListView) v.findViewById(android.R.id.list);
        String[] from = {TopicDAO.NAME};
        int[] to = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(getContext(), R.layout.item_topic, topicDAO.findAll(), from, to, 0);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ThemeActivity.startActivity(getContext(), id);
            }
        });

        registerForContextMenu(mList);

        return v;
    }

    private void updateView() {
        adapter.changeCursor(topicDAO.findAll());
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
                topicDAO.delete(info.id);
                updateView();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        final EditText editText = new EditText(getContext());
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setSingleLine();


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter new topic name")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createTopic(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null)
                .setView(editText);
        final AlertDialog dialog = builder.create();
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    createTopic(editText.getText().toString());
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        dialog.show();
    }

    private void createTopic(String name) {
        Topic topic = new Topic();
        topic.name = name;
        topicDAO.insert(topic);
        updateView();
    }
}
