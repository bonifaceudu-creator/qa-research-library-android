package com.qaresearch.library;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.os.Environment;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;
    private ImageView launchScreen;

    private static final String START_URL =
            "https://bonifaceudu-creator.github.io/qa-research-library-app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout container = new FrameLayout(this);
        container.setBackgroundColor(Color.WHITE);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.WHITE);

        // Clear the old HTTP cache.
        // This allows the updated WebP files to be downloaded.
        webView.clearCache(true);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);

        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);

        webView.setVisibility(View.INVISIBLE);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                String url = request.getUrl().toString();

                if (isPdf(url)) {
                    downloadPdf(url);
                    return true;
                }

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    String url) {

                if (isPdf(url)) {
                    downloadPdf(url);
                    return true;
                }

                return false;
            }

            @Override
            public void onPageFinished(
                    WebView view,
                    String url) {

                super.onPageFinished(view, url);

                view.setVisibility(View.VISIBLE);

                if (launchScreen != null) {
                    launchScreen.setVisibility(View.GONE);
                }
            }
        });

        container.addView(
                webView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );

        // Launch screen
        launchScreen = new ImageView(this);
        launchScreen.setBackgroundColor(Color.WHITE);
        launchScreen.setImageResource(R.drawable.app_icon);
        launchScreen.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );
        launchScreen.setPadding(70, 70, 70, 70);

        FrameLayout.LayoutParams launchParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );

        launchParams.gravity = Gravity.CENTER;

        container.addView(
                launchScreen,
                launchParams
        );

        setContentView(container);

        if (savedInstanceState == null) {

            webView.loadUrl(START_URL);

        } else {

            webView.restoreState(savedInstanceState);

            webView.setVisibility(View.VISIBLE);
            launchScreen.setVisibility(View.GONE);
        }
    }

    private boolean isPdf(String url) {

        if (url == null) {
            return false;
        }

        return url.toLowerCase().contains(".pdf");
    }

    private void downloadPdf(String url) {

        try {

            Uri uri = Uri.parse(url);

            DownloadManager.Request request =
                    new DownloadManager.Request(uri);

            request.setTitle("QA Research Library PDF");
            request.setDescription("Downloading document...");

            request.setNotificationVisibility(
                    DownloadManager.Request
                            .VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            );

            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "QA_Research_Library_Document.pdf"
            );

            request.setMimeType("application/pdf");

            DownloadManager manager =
                    (DownloadManager) getSystemService(
                            DOWNLOAD_SERVICE
                    );

            if (manager != null) {
                manager.enqueue(request);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (webView != null &&
                webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(
            Bundle outState) {

        if (webView != null) {
            webView.saveState(outState);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {

        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }

        super.onDestroy();
    }
}
