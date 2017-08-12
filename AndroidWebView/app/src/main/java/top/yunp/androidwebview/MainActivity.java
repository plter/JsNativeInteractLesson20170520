package top.yunp.androidwebview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {


    private WebView myWebView;
    private LinearLayout rootContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootContainer = (LinearLayout) findViewById(R.id.rootContainer);

        myWebView = (WebView) findViewById(R.id.myWebView);
//        myWebView = new WebView(this);
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new JsInterface(this, rootContainer, myWebView), "AndroidJs");
//        myWebView.loadData("<html><body>Hello Android</body></html>", "text/html", "utf-8");
//        myWebView.loadUrl("http://xw.qq.com/index.htm");


        try {
            InputStream inputStream = getAssets().open("app.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            inputStream.close();

            myWebView.loadData(sb.toString(), "text/html", "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
