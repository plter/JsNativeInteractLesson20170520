package top.yunp.androidwebview;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by plter on 8/12/17.
 */

class JsInterface {


    private static Map<Integer, Button> buttons = new HashMap<>();
    private static int id = 1;

    private final MainActivity context;
    private final LinearLayout rootContainer;
    private final WebView webview;

    public JsInterface(MainActivity context, LinearLayout rootContainer, WebView myWebView) {
        this.context = context;
        this.rootContainer = rootContainer;
        this.webview = myWebView;
    }

    @JavascriptInterface
    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void createButton(final String label) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button btn = new Button(context);
                btn.setText(label);

                int currentBtnId = id;
                buttons.put(currentBtnId, btn);
                id++;

                webview.evaluateJavascript("window.oncreateButton(" + currentBtnId + ");", null);
            }
        });
    }

    @JavascriptInterface
    public void addBtn(final int btnId) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootContainer.addView(buttons.get(btnId));
            }
        });
    }
}
