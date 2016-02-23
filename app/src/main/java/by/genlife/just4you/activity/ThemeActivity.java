package by.genlife.just4you.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import by.genlife.just4you.R;
import by.genlife.just4you.db.ThemeDAO;
import by.genlife.just4you.db.TopicDAO;
import by.genlife.just4you.model.Theme;

public class ThemeActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_TOPIC_ID = "topic.id.extra";
    private ThemeDAO themeDAO;
    private SimpleCursorAdapter adapter;
    private long topicId;


    @Override
    protected int getContentView() {
        return R.layout.activity_theme;
    }

    public static void startActivity(Context context, long id) {
        Intent intent = new Intent(context, ThemeActivity.class);
        intent.putExtra(EXTRA_TOPIC_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TopicDAO topicDAO = new TopicDAO(this);
        topicId = getIntent().getLongExtra(EXTRA_TOPIC_ID, -1);
        if (topicId == -1) {
            finish();
            return;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(topicDAO.find(topicId).name);
        themeDAO = new ThemeDAO(this);

        findViewById(R.id.fab).setOnClickListener(this);
        ListView mList = (ListView) findViewById(android.R.id.list);
        String[] from = {ThemeDAO.NAME};
        int[] to = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(this, R.layout.item_topic, themeDAO.findAll(topicId), from, to, 0);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ThemeViewActivity.startActivity(ThemeActivity.this, id);
            }
        });

        registerForContextMenu(mList);
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
                themeDAO.delete(info.id);
                updateView();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        final EditText editText = new EditText(this);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setSingleLine();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new theme name")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createTheme(editText.getText().toString());
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
                    createTheme(editText.getText().toString());
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        dialog.show();
    }

    private void createTheme(String string) {
        Theme theme = new Theme();
        theme.name = string;
        theme.topicID = topicId;
        themeDAO.insert(theme);
        updateView();
    }

    private void updateView() {
        adapter.changeCursor(themeDAO.findAll(topicId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }
}
