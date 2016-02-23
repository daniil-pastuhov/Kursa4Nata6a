package by.genlife.just4you.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import by.genlife.just4you.R;

public class CreateTopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CreateTopicActivity.class);
        context.startActivity(intent);
    }
}
