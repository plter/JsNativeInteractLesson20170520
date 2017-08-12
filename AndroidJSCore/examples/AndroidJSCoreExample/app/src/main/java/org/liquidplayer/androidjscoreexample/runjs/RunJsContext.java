package org.liquidplayer.androidjscoreexample.runjs;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import org.liquidplayer.androidjscoreexample.RunJsActivity;
import org.liquidplayer.webkit.javascriptcore.JSContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by plter on 5/20/17.
 */

public class RunJsContext extends JSContext implements IRunJsContext {

    private final RunJsActivity activity;
    private SparseArray<Object> sharedObjects = new SparseArray<Object>();

    private int id = 0;

    public RunJsContext(RunJsActivity activity) {
        super(IRunJsContext.class);

        this.activity = activity;
    }

    @Override
    public void trace(String msg) {
        System.out.println(msg);
    }

    @Override
    public void load(String fileName) {
        try {
            InputStream inputStream = activity.getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line = null;
            StringBuilder content = new StringBuilder();
            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }
            inputStream.close();
            evaluateScript(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createButton() {
        Button btn = new Button(activity);
        int id = getId();
        sharedObjects.put(id, btn);
        return id;
    }

    @Override
    public void addButton(final int btnId) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.getAppRoot().addView((View) sharedObjects.get(btnId), -2, -2);
            }
        });
    }

    @Override
    public void setButtonText(final int btnId, final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((Button) sharedObjects.get(btnId)).setText(text);
            }
        });
    }


    public int getId() {
        id++;
        return id;
    }
}
