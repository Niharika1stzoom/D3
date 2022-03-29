package com.example.d3.Web;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewClientImpl extends WebViewClient {

    private Activity activity = null;
    private ProgressBar progressBar;

    public WebViewClientImpl(Activity activity, ProgressBar progressBar) {
        this.activity = activity;
        this.progressBar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        // if(url.indexOf("google.com") > -1 ) return false;
        return false;
       /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        return true;*/
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
        progressBar.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }
}