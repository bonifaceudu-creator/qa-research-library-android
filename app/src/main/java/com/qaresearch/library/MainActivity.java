package com.qaresearch.library;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.os.Environment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Handler;

public class MainActivity extends Activity {

    private WebView webView;

    private View splashScreen;

    private TextView sloganText;

    private boolean pageLoaded = false;

    private static final long MIN_SPLASH_TIME = 3500;

    private long splashStartTime;

    private Handler handler = new Handler();

    private static final String START_URL =
            "https://bonifaceudu-creator.github.io/qa-research-library-app/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        splashStartTime = System.currentTimeMillis();

        FrameLayout root = new FrameLayout(this);

        root.setBackgroundColor(Color.WHITE);


        // ============================================================
        // WEBVIEW
        // ============================================================

        webView = new WebView(this);

        webView.setBackgroundColor(Color.WHITE);

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

                String url =
                        request.getUrl().toString();

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

                pageLoaded = true;

                attemptToCloseSplash();
            }

        });


        FrameLayout.LayoutParams webParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );

        root.addView(webView, webParams);


        // ============================================================
        // PROFESSIONAL SPLASH SCREEN
        // ============================================================

        LinearLayout splash =
                new LinearLayout(this);

        splashScreen = splash;

        splash.setOrientation(
                LinearLayout.VERTICAL
        );

        splash.setGravity(Gravity.CENTER);

        splash.setBackgroundColor(
                Color.rgb(6, 42, 82)
        );

        splash.setPadding(
                35,
                35,
                35,
                35
        );


        // ============================================================
        // LOGO
        // ============================================================

        ImageView logo =
                new ImageView(this);

        logo.setImageResource(
                R.drawable.app_icon
        );

        logo.setScaleType(
                ImageView.ScaleType.CENTER_INSIDE
        );

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(
                        190,
                        190
                );

        logoParams.gravity =
                Gravity.CENTER;

        logoParams.bottomMargin = 20;

        splash.addView(
                logo,
                logoParams
        );


        // ============================================================
        // QA & RESEARCH
        // ============================================================

        TextView department =
                new TextView(this);

        department.setText(
                "QA & RESEARCH"
        );

        department.setTextColor(
                Color.WHITE
        );

        department.setTextSize(
                24
        );

        department.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        department.setGravity(
                Gravity.CENTER
        );


        splash.addView(
                department,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );


        // ============================================================
        // DIGITAL LIBRARY
        // ============================================================

        TextView library =
                new TextView(this);

        library.setText(
                "DIGITAL LIBRARY"
        );

        library.setTextColor(
                Color.rgb(169, 213, 245)
        );

        library.setTextSize(
                13
        );

        library.setGravity(
                Gravity.CENTER
        );

        library.setLetterSpacing(
                0.15f
        );


        LinearLayout.LayoutParams libraryParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        libraryParams.topMargin = 5;

        splash.addView(
                library,
                libraryParams
        );


        // ============================================================
        // SLOGAN
        // ============================================================

        sloganText =
                new TextView(this);

        sloganText.setText("");

        sloganText.setTextColor(
                Color.WHITE
        );

        sloganText.setTextSize(
                12
        );

        sloganText.setGravity(
                Gravity.CENTER
        );

        sloganText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        sloganText.setPadding(
                10,
                45,
                10,
                10
        );


        splash.addView(
                sloganText,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );


        // ============================================================
        // ADD SPLASH TO ROOT
        // ============================================================

        FrameLayout.LayoutParams splashParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );

        splashParams.gravity =
                Gravity.CENTER;

        root.addView(
                splash,
                splashParams
        );


        setContentView(root);


        // ============================================================
        // START SLOGAN ANIMATION
        // ============================================================

        animateSlogan();


        // ============================================================
        // LOAD WEBSITE
        // ============================================================

        if (savedInstanceState == null) {

            webView.loadUrl(
                    START_URL
            );

        } else {

            webView.restoreState(
                    savedInstanceState
            );

            pageLoaded = true;
        }

    }


    // ================================================================
    // SLOGAN TYPEWRITER ANIMATION
    // ================================================================

    private void animateSlogan() {

        final String slogan =
                "THE BEDROCK OF INDUSTRIALIZATION";

        final Handler animationHandler =
                new Handler();

        animationHandler.postDelayed(
                new Runnable() {

                    int position = 0;

                    @Override
                    public void run() {

                        if (position <= slogan.length()) {

                            sloganText.setText(
                                    slogan.substring(
                                            0,
                                            position
                                    )
                            );

                            position++;

                            animationHandler.postDelayed(
                                    this,
                                    65
                            );

                        } else {

                            fadeSlogan();

                        }

                    }

                },
                900
        );
    }


    // ================================================================
    // SUBTLE SLOGAN FADE
    // ================================================================

    private void fadeSlogan() {

        AlphaAnimation animation =
                new AlphaAnimation(
                        0.65f,
                        1.0f
                );

        animation.setDuration(
                600
        );

        animation.setRepeatMode(
                Animation.REVERSE
        );

        animation.setRepeatCount(
                1
        );

        sloganText.startAnimation(
                animation
        );
    }


    // ================================================================
    // KEEP SPLASH UNTIL:
    //
    // 1. Website has loaded
    // 2. Minimum splash time has passed
    // ================================================================

    private void attemptToCloseSplash() {

        if (!pageLoaded) {
            return;
        }

        long elapsed =
                System.currentTimeMillis()
                - splashStartTime;

        long remaining =
                MIN_SPLASH_TIME - elapsed;


        if (remaining <= 0) {

            closeSplash();

        } else {

            handler.postDelayed(
                    new Runnable() {

                        @Override
                        public void run() {

                            closeSplash();

                        }

                    },
                    remaining
            );

        }
    }


    // ================================================================
    // CLOSE SPLASH
    // ================================================================

    private void closeSplash() {

        if (splashScreen == null) {
            return;
        }


        // Make website visible first

        webView.setVisibility(
                View.VISIBLE
        );


        // Fade splash away

        AlphaAnimation fade =
                new AlphaAnimation(
                        1.0f,
                        0.0f
                );

        fade.setDuration(
                500
        );

        fade.setFillAfter(
                true
        );


        fade.setAnimationListener(
                new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(
                            Animation animation) {
                    }


                    @Override
                    public void onAnimationEnd(
                            Animation animation) {

                        splashScreen.setVisibility(
                                View.GONE
                        );
                    }


                    @Override
                    public void onAnimationRepeat(
                            Animation animation) {
                    }

                }
        );


        splashScreen.startAnimation(
                fade
        );

    }


    // ================================================================
    // PDF DETECTION
    // ================================================================

    private boolean isPdf(String url) {

        if (url == null) {
            return false;
        }

        return url.toLowerCase().contains(".pdf");
    }


    // ================================================================
    // PDF DOWNLOAD
    // ================================================================

    private void downloadPdf(String url) {

        try {

            Uri uri =
                    Uri.parse(url);

            DownloadManager.Request request =
                    new DownloadManager.Request(
                            uri
                    );

            request.setTitle(
                    "QA Research Library PDF"
            );

            request.setDescription(
                    "Downloading document..."
            );

            request.setNotificationVisibility(
                    DownloadManager.Request
                            .VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            );

            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "QA_Research_Library_Document.pdf"
            );

            request.setMimeType(
                    "application/pdf"
            );


            DownloadManager manager =
                    (DownloadManager)
                            getSystemService(
                                    DOWNLOAD_SERVICE
                            );


            if (manager != null) {

                manager.enqueue(
                        request
                );

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    // ================================================================
    // ANDROID BACK BUTTON
    // ================================================================

    @Override
    public void onBackPressed() {

        if (webView != null &&
                webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();

        }

    }


    // ================================================================
    // SAVE WEBVIEW STATE
    // ================================================================

    @Override
    protected void onSaveInstanceState(
            Bundle outState) {

        if (webView != null) {

            webView.saveState(
                    outState
            );

        }

        super.onSaveInstanceState(
                outState
        );

    }


    // ================================================================
    // CLEAN UP
    // ================================================================

    @Override
    protected void onDestroy() {

        if (handler != null) {

            handler.removeCallbacksAndMessages(
                    null
            );

        }


        if (webView != null) {

            webView.stopLoading();

            webView.destroy();

        }


        super.onDestroy();

    }

}

What this version changes

The splash now has a 3.5-second minimum duration. It won't disappear just because the homepage loads quickly.

The sequence is:

0.0s → Branded blue splash appears
0.9s → "THE BEDROCK OF INDUSTRIALIZATION" begins appearing letter by letter
~2.9s → slogan finishes
3.5s minimum → homepage becomes visible
3.5–4.0s → splash smoothly fades away

The important part is that the 3.5 seconds is a minimum, not an arbitrary delay. If the website takes 5 seconds to load, the splash remains until the website is ready.

Your existing PDF download handling, WebView, back button, WebP loading, and GitHub Pages URL are retained.

One thing to check before building

Because this code uses:

R.drawable.app_icon

your Android project must still contain:

"app/src/main/res/drawable/app_icon.png"

Since your previous splash worked with "app_icon", you shouldn't need to change anything there.

After replacing "MainActivity.java", commit the change and run your existing "assembleDebug" GitHub Actions workflow.
