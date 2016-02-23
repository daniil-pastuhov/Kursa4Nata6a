package by.genlife.just4you.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import by.genlife.just4you.R;
import by.genlife.just4you.db.ThemeDAO;
import by.genlife.just4you.model.Theme;

public class ThemeViewActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_THEME_ID = "theme.id.extra";
    private ThemeDAO themeDAO;
    private long themeId;
    private EditText text;
    private String uniqName;

    @Override
    protected int getContentView() {
        return R.layout.activity_theme_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void startActivity(Context context, long id) {
        Intent intent = new Intent(context, ThemeViewActivity.class);
        intent.putExtra(EXTRA_THEME_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        themeDAO = new ThemeDAO(this);
        themeId = getIntent().getLongExtra(EXTRA_THEME_ID, -1);
        if (themeId == -1) {
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
        toolbar.setTitle(themeDAO.find(themeId).name);

        findViewById(R.id.fab).setOnClickListener(this);

        text = (EditText) findViewById(R.id.edit_text);

        Theme theme = themeDAO.find(themeId);
        uniqName = theme.name + theme.id;

        initWithData();

    }

    private void initWithData() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            DataInputStream in = new DataInputStream(openFileInput(uniqName));

            String line;
            while ((line = in.readUTF()) != null) {
                stringBuilder.append(line).append("\n");
            }
            in.close();
        } catch (IOException ex) {
            //do nothing
        }
        text.setText(stringBuilder.toString());
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
                themeDAO.delete(themeId);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {
        save();
        finish();
    }

    private void save() {
        try {
            DataOutputStream out = new DataOutputStream(openFileOutput(uniqName, Context.MODE_PRIVATE));
            out.writeUTF(text.getText().toString());
            out.flush();
            out.close();
        } catch (IOException ex) {
            //do nothing
        }
    }

}
