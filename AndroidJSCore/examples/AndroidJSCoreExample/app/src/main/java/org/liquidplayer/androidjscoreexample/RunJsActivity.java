package org.liquidplayer.androidjscoreexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.liquidplayer.androidjscoreexample.runjs.RunJsContext;

public class RunJsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_js);

//        JSContext context = new JSContext();
//        JSValue jsValue = context.evaluateScript("'Hello Android'");
//        Toast.makeText(this, jsValue.toString(), Toast.LENGTH_SHORT).show();

        RunJsContext context = new RunJsContext();
        context.evaluateScript("trace('Hello JavaScript')");
    }
}
