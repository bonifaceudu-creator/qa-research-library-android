package com.qaresearch.library;

import android.app.Activity;
import android.content.Intent;
import android.app.DownloadManager;
import android.os.Bundle;
import android.os.Environment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Handler;

public class MainActivity extends Activity {

    private WebView webView;
    private View launchScreen;

    private Handler splashHandler = new Handler();

    private boolean pageLoaded = false;
    private boolean minimumTimePassed = false;

    private static final long SPLASH_DURATION = 3500;

    private static final String START_URL =
            "https://bonifaceudu-creator.github.io/qa-research-library-app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // =========================================================
        // MAIN CONTAINER
        // =========================================================

        FrameLayout container = new FrameLayout(this);
        container.setBackgroundColor(Color.WHITE);

        // =========================================================
        // WEBVIEW
        // =========================================================

        webView = new WebView(this);
        webView.setBackgroundColor(Color.WHITE);

        // Clear old cache so updated WebP images are used.
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

        // Keep website hidden while splash is displayed.
        webView.setVisibility(View.INVISIBLE);

        // =========================================================
        // WEBVIEW CLIENT
        // =========================================================

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                String url = request.getUrl().toString();

                if (isPdf(url)) {
                    openPdf(url);
                    return true;
                }

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    String url) {

                if (isPdf(url)) {
                    openPdf(url);
                    return true;
                }

                return false;
            }

            @Override
            public void onPageFinished(
                    WebView view,
                    String url) {

                super.onPageFinished(view, url);

                pageLoaded = true;

                checkSplashReady();
            }
        });

        container.addView(
                webView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );

        // =========================================================
        // PROFESSIONAL BRANDED SPLASH SCREEN
        // =========================================================

        LinearLayout splashLayout = new LinearLayout(this);

        splashLayout.setOrientation(LinearLayout.VERTICAL);
        splashLayout.setGravity(Gravity.CENTER);
        splashLayout.setBackgroundColor(Color.rgb(6, 42, 82));
        splashLayout.setPadding(
                dpToPx(30),
                dpToPx(30),
                dpToPx(30),
                dpToPx(30)
        );

        // ---------------------------------------------------------
        // LARGE LOGO
        // ---------------------------------------------------------

        ImageView logo = new ImageView(this);

        logo.setImageResource(R.drawable.app_icon);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        // Increased from 125dp to 170dp.
        int logoSize = dpToPx(170);

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(
                        logoSize,
                        logoSize
                );

        logoParams.gravity = Gravity.CENTER;
        logoParams.bottomMargin = dpToPx(18);

        splashLayout.addView(logo, logoParams);

        // ---------------------------------------------------------
        // QA & RESEARCH
        // ---------------------------------------------------------

        TextView title = new TextView(this);

        title.setText("QA & RESEARCH");
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);

        title.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                )
        );

        title.setGravity(Gravity.CENTER);
        title.setLetterSpacing(0.08f);

        splashLayout.addView(
                title,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        // ---------------------------------------------------------
        // DIGITAL LIBRARY
        // ---------------------------------------------------------

        TextView subtitle = new TextView(this);

        subtitle.setText("DIGITAL LIBRARY");
        subtitle.setTextColor(Color.rgb(143, 201, 244));
        subtitle.setTextSize(13);

        subtitle.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                )
        );

        subtitle.setGravity(Gravity.CENTER);
        subtitle.setLetterSpacing(0.15f);

        LinearLayout.LayoutParams subtitleParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        subtitleParams.topMargin = dpToPx(5);

        splashLayout.addView(
                subtitle,
                subtitleParams
        );

        // ---------------------------------------------------------
        // DIVIDER
        // ---------------------------------------------------------

        View divider = new View(this);

        divider.setBackgroundColor(
                Color.rgb(143, 201, 244)
        );

        LinearLayout.LayoutParams dividerParams =
                new LinearLayout.LayoutParams(
                        dpToPx(70),
                        dpToPx(1)
                );

        dividerParams.gravity = Gravity.CENTER;
        dividerParams.topMargin = dpToPx(22);
        dividerParams.bottomMargin = dpToPx(18);

        splashLayout.addView(
                divider,
                dividerParams
        );

        // ---------------------------------------------------------
        // DEPARTMENT
        // ---------------------------------------------------------

        TextView department = new TextView(this);

        department.setText(
                "QUALITY ASSURANCE & RESEARCH DEPARTMENT"
        );

        department.setTextColor(
                Color.rgb(210, 225, 239)
        );

        department.setTextSize(10);
        department.setGravity(Gravity.CENTER);

        splashLayout.addView(
                department,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        // ---------------------------------------------------------
        // LOCATION
        // ---------------------------------------------------------

        TextView location = new TextView(this);

        location.setText("AJAOKUTA STEEL PLANT");

        location.setTextColor(
                Color.rgb(170, 195, 217)
        );

        location.setTextSize(10);
        location.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams locationParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        locationParams.topMargin = dpToPx(5);

        splashLayout.addView(
                location,
                locationParams
        );

        // ---------------------------------------------------------
        // SLOGAN
        // ---------------------------------------------------------

        TextView slogan = new TextView(this);

        slogan.setText("");
        slogan.setTextColor(Color.WHITE);
        slogan.setTextSize(11);

        slogan.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                )
        );

        slogan.setGravity(Gravity.CENTER);
        slogan.setLetterSpacing(0.08f);

        LinearLayout.LayoutParams sloganParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        sloganParams.topMargin = dpToPx(28);

        splashLayout.addView(
                slogan,
                sloganParams
        );

        // ---------------------------------------------------------
        // LOADING INDICATOR
        // ---------------------------------------------------------

        ProgressBar progressBar = new ProgressBar(this);

        progressBar.setIndeterminate(true);

        LinearLayout.LayoutParams progressParams =
                new LinearLayout.LayoutParams(
                        dpToPx(32),
                        dpToPx(32)
                );

        progressParams.gravity = Gravity.CENTER;
        progressParams.topMargin = dpToPx(25);

        splashLayout.addView(
                progressBar,
                progressParams
        );

        // =========================================================
        // SAVE SPLASH SCREEN
        // =========================================================

        launchScreen = splashLayout;

        FrameLayout.LayoutParams splashParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );

        splashParams.gravity = Gravity.CENTER;

        container.addView(
                launchScreen,
                splashParams
        );

        // =========================================================
        // SET CONTENT VIEW
        // =========================================================

        setContentView(container);

        // =========================================================
        // START SPLASH TIMER
        // =========================================================

        splashHandler.postDelayed(
                new Runnable() {

                    @Override
                    public void run() {

                        minimumTimePassed = true;

                        checkSplashReady();
                    }

                },
                SPLASH_DURATION
        );

        // =========================================================
        // START SLOGAN ANIMATION
        // =========================================================

        animateSlogan(slogan);

        // =========================================================
        // LOAD WEBSITE
        // =========================================================

        if (savedInstanceState == null) {

            webView.loadUrl(START_URL);

        } else {

            webView.restoreState(savedInstanceState);

            pageLoaded = true;
            minimumTimePassed = true;

            webView.setVisibility(View.VISIBLE);
            launchScreen.setVisibility(View.GONE);
        }
    }

    // =============================================================
    // SLOGAN LETTER-BY-LETTER ANIMATION
    // =============================================================

    private void animateSlogan(final TextView slogan) {

        final String text =
                "THE BEDROCK OF INDUSTRIALIZATION";

        final Handler handler = new Handler();

        handler.postDelayed(
                new Runnable() {

                    int position = 0;

                    @Override
                    public void run() {

                        if (position <= text.length()) {

                            slogan.setText(
                                    text.substring(0, position)
                            );

                            position++;

                            handler.postDelayed(
                                    this,
                                    70
                            );
                        }
                    }

                },
                700
        );
    }

    // =============================================================
    // CHECK WHETHER SPLASH CAN DISAPPEAR
    // =============================================================

    private void checkSplashReady() {

        if (pageLoaded && minimumTimePassed) {

            webView.setVisibility(View.VISIBLE);

            launchScreen.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .withEndAction(
                            new Runnable() {

                                @Override
                                public void run() {

                                    launchScreen.setVisibility(
                                            View.GONE
                                    );

                                    launchScreen.setAlpha(1f);
                                }
                            }
                    )
                    .start();
        }
    }

    // =============================================================
    // PDF DETECTION
    // =============================================================

    private boolean isPdf(String url) {

        return url != null &&
                url.toLowerCase().contains(".pdf");
    }

    // =============================================================
    // OPEN PDF
    // =============================================================

    private void openPdf(String url) {

        try {

            Uri uri = Uri.parse(url);

            Intent intent = new Intent(
                    Intent.ACTION_VIEW
            );

            intent.setDataAndType(
                    uri,
                    "application/pdf"
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
            );

            startActivity(intent);

        } catch (Exception e) {

            e.printStackTrace();

            try {

                Uri uri = Uri.parse(url);

                Intent browserIntent =
                        new Intent(
                                Intent.ACTION_VIEW,
                                uri
                        );

                browserIntent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                );

                startActivity(browserIntent);

            } catch (Exception browserException) {

                browserException.printStackTrace();
            }
        }
    }

    // =============================================================
    // ANDROID BACK BUTTON
    // =============================================================

    @Override
    public void onBackPressed() {

        if (webView != null &&
                webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();
        }
    }

    // =============================================================
    // SAVE WEBVIEW STATE
    // =============================================================

    @Override
    protected void onSaveInstanceState(
            Bundle outState) {

        if (webView != null) {
            webView.saveState(outState);
        }

        super.onSaveInstanceState(outState);
    }

    // =============================================================
    // CLEAN UP
    // =============================================================

    @Override
    protected void onDestroy() {

        if (splashHandler != null) {
            splashHandler.removeCallbacksAndMessages(null);
        }

        if (webView != null) {

            webView.stopLoading();
            webView.destroy();
        }

        super.onDestroy();
    }

    // =============================================================
    // DP TO PIXELS
    // =============================================================

    private int dpToPx(int dp) {

        float density =
                getResources()
                        .getDisplayMetrics()
                        .density;

        return Math.round(dp * density);
    }
}
