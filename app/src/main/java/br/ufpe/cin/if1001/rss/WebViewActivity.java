package br.ufpe.cin.if1001.rss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView wv = (WebView) findViewById(R.id.wv);

        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);

        Intent i = getIntent();
        String url = i.getStringExtra("url");

        // Setando novo Client para que a url não seja aberta no browser
        wv.setWebViewClient(new WebViewClient());

        wv.loadUrl(url);

    }
}
