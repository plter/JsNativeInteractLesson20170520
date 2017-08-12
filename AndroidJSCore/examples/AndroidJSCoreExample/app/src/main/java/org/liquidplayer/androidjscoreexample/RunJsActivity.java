package org.liquidplayer.androidjscoreexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import org.liquidplayer.androidjscoreexample.runjs.RunJsContext;

public class RunJsActivity extends AppCompatActivity {

    private FrameLayout appRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_js);

        appRoot = (FrameLayout) findViewById(R.id.appRoot);

//        JSContext context = new JSContext();
//        JSValue jsValue = context.evaluateScript("'Hello Android'");
//        Toast.makeText(this, jsValue.toString(), Toast.LENGTH_SHORT).show();

        RunJsContext context = new RunJsContext(this);
        context.evaluateScript("load('main.js')");
//        context.evaluateScript("trace('>>>>>>>> main.js')");
    }

    public FrameLayout getAppRoot() {
        return appRoot;
    }
}
