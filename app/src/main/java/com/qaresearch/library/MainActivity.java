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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import android.view.animation.AlphaAnimation;

public class MainActivity extends Activity {

    private WebView webView;
    private View launchScreen;

    private boolean splashFinished = false;
    private boolean pageLoaded = false;

    private static final long SPLASH_DURATION = 3500;

    private static final String START_URL =
            "https://bonifaceudu-creator.github.io/qa-research-library-app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // =========================================================
        // FULLSCREEN STARTUP
        // =========================================================

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        getWindow().setStatusBarColor(Color.rgb(6, 42, 82));
        getWindow().setNavigationBarColor(Color.rgb(6, 42, 82));

        // =========================================================
        // MAIN CONTAINER
        // =========================================================

        FrameLayout container = new FrameLayout(this);

        container.setBackgroundColor(
                Color.rgb(6, 42, 82)
        );

        // =========================================================
        // WEBVIEW
        // =========================================================

        webView = new WebView(this);

        webView.setBackgroundColor(Color.WHITE);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        settings.setCacheMode(
                WebSettings.LOAD_DEFAULT
        );

        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);

        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);

        // Keep website hidden while splash is running.
        webView.setVisibility(View.INVISIBLE);

        // =========================================================
        // WEBVIEW CLIENT
        // =========================================================

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

                /*
                 * IMPORTANT:
                 *
                 * Do NOT hide the splash here.
                 *
                 * Otherwise every page/navigation event can
                 * interfere with the splash timing.
                 *
                 * The splash is controlled only by the timer below.
                 */
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
        // PROFESSIONAL SPLASH SCREEN
        // =========================================================

        LinearLayout splashLayout =
                new LinearLayout(this);

        splashLayout.setOrientation(
                LinearLayout.VERTICAL
        );

        splashLayout.setGravity(
                Gravity.CENTER
        );

        splashLayout.setBackgroundColor(
                Color.rgb(6, 42, 82)
        );

        splashLayout.setPadding(
                dpToPx(30),
                dpToPx(30),
                dpToPx(30),
                dpToPx(30)
        );

        // =========================================================
        // LOGO
        // =========================================================

        ImageView logo =
                new ImageView(this);

        logo.setImageResource(
                R.drawable.app_icon
        );

        logo.setScaleType(
                ImageView.ScaleType.FIT_CENTER
        );

        int logoSize =
                dpToPx(125);

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(
                        logoSize,
                        logoSize
                );

        logoParams.gravity =
                Gravity.CENTER;

        logoParams.bottomMargin =
                dpToPx(22);

        splashLayout.addView(
                logo,
                logoParams
        );

        // =========================================================
        // QA & RESEARCH
        // =========================================================

        TextView title =
                new TextView(this);

        title.setText(
                "QA & RESEARCH"
        );

        title.setTextColor(
                Color.WHITE
        );

        title.setTextSize(24);

        title.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                )
        );

        title.setGravity(
                Gravity.CENTER
        );

        title.setLetterSpacing(
                0.08f
        );

        splashLayout.addView(
                title,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        // =========================================================
        // DIGITAL LIBRARY
        // =========================================================

        TextView subtitle =
                new TextView(this);

        subtitle.setText(
                "DIGITAL LIBRARY"
        );

        subtitle.setTextColor(
                Color.rgb(143, 201, 244)
        );

        subtitle.setTextSize(13);

        subtitle.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                )
        );

        subtitle.setGravity(
                Gravity.CENTER
        );

        subtitle.setLetterSpacing(
                0.15f
        );

        LinearLayout.LayoutParams subtitleParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        subtitleParams.topMargin =
                dpToPx(5);

        splashLayout.addView(
                subtitle,
                subtitleParams
        );

        // =========================================================
        // DIVIDER
        // =========================================================

        View divider =
                new View(this);

        divider.setBackgroundColor(
                Color.rgb(143, 201, 244)
        );

        LinearLayout.LayoutParams dividerParams =
                new LinearLayout.LayoutParams(
                        dpToPx(70),
                        dpToPx(1)
                );

        dividerParams.gravity =
                Gravity.CENTER;

        dividerParams.topMargin =
                dpToPx(22);

        dividerParams.bottomMargin =
                dpToPx(18);

        splashLayout.addView(
                divider,
                dividerParams
        );

        // =========================================================
        // DEPARTMENT
        // =========================================================

        TextView department =
                new TextView(this);

        department.setText(
                "QUALITY ASSURANCE & RESEARCH DEPARTMENT"
        );

        department.setTextColor(
                Color.rgb(210, 225, 239)
        );

        department.setTextSize(10);

        department.setGravity(
                Gravity.CENTER
        );

        splashLayout.addView(
                department,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        // =========================================================
        // LOCATION
        // =========================================================

        TextView location =
                new TextView(this);

        location.setText(
                "AJAOKUTA STEEL PLANT"
        );

        location.setTextColor(
                Color.rgb(170, 195, 217)
        );

        location.setTextSize(10);

        location.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams locationParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        locationParams.topMargin =
                dpToPx(5);

        splashLayout.addView(
                location,
                locationParams
        );

        // =========================================================
        // ANIMATED SLOGAN
        // =========================================================

        TextView slogan =
                new TextView(this);

        slogan.setText(
                "THE BEDROCK OF INDUSTRIALIZATION"
        );

        slogan.setTextColor(
                Color.WHITE
        );

        slogan.setTextSize(13);

        slogan.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                )
        );

        slogan.setGravity(
                Gravity.CENTER
        );

        slogan.setLetterSpacing(
                0.08f
        );

        slogan.setVisibility(
                View.INVISIBLE
        );

        LinearLayout.LayoutParams sloganParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        sloganParams.topMargin =
                dpToPx(65);

        splashLayout.addView(
                slogan,
                sloganParams
        );

        // =========================================================
        // LOADING INDICATOR
        // =========================================================

        ProgressBar progressBar =
                new ProgressBar(this);

        progressBar.setIndeterminate(
                true
        );

        LinearLayout.LayoutParams progressParams =
                new LinearLayout.LayoutParams(
                        dpToPx(32),
                        dpToPx(32)
                );

        progressParams.gravity =
                Gravity.CENTER;

        progressParams.topMargin =
                dpToPx(35);

        splashLayout.addView(
                progressBar,
                progressParams
        );

        // =========================================================
        // ADD SPLASH TO CONTAINER
        // =========================================================

        launchScreen =
                splashLayout;

        FrameLayout.LayoutParams splashParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );

        splashParams.gravity =
                Gravity.CENTER;

        container.addView(
                launchScreen,
                splashParams
        );

        // =========================================================
        // SET CONTENT VIEW
        // =========================================================

        setContentView(container);

        // =========================================================
        // SLOGAN ANIMATION
        // =========================================================

        Handler handler =
                new Handler();

        handler.postDelayed(
                new Runnable() {

                    @Override
                    public void run() {

                        slogan.setVisibility(
                                View.VISIBLE
                        );

                        AlphaAnimation animation =
                                new AlphaAnimation(
                                        0.0f,
                                        1.0f
                                );

                        animation.setDuration(
                                1200
                        );

                        slogan.startAnimation(
                                animation
                        );
                    }

                },
                900
        );

        // =========================================================
        // LOAD WEBSITE
        // =========================================================

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

        // =========================================================
        // CONTROL SPLASH
        // =========================================================

        handler.postDelayed(
                new Runnable() {

                    @Override
                    public void run() {

                        finishSplash();

                    }

                },
                SPLASH_DURATION
        );
    }

    // =============================================================
    // FINISH SPLASH
    // =============================================================

    private void finishSplash() {

        if (splashFinished) {
            return;
        }

        splashFinished = true;

        if (webView != null) {

            webView.setVisibility(
                    View.VISIBLE
            );
        }

        if (launchScreen != null) {

            AlphaAnimation fade =
                    new AlphaAnimation(
                            1.0f,
                            0.0f
                    );

            fade.setDuration(
                    500
            );

            fade.setFillAfter(true);

            launchScreen.startAnimation(
                    fade
            );

            launchScreen.postDelayed(
                    new Runnable() {

                        @Override
                        public void run() {

                            if (launchScreen != null) {

                                launchScreen.setVisibility(
                                        View.GONE
                                );
                            }

                        }

                    },
                    500
            );
        }
    }

    // =============================================================
    // PDF DETECTION
    // =============================================================

    private boolean isPdf(String url) {

        if (url == null) {
            return false;
        }

        return url.toLowerCase().contains(".pdf");
    }

    // =============================================================
    // PDF DOWNLOAD
    // =============================================================

    private void downloadPdf(String url) {

        try {

            Uri uri =
                    Uri.parse(url);

            DownloadManager.Request request =
                    new DownloadManager.Request(uri);

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

            webView.saveState(
                    outState
            );
        }

        super.onSaveInstanceState(
                outState
        );
    }

    // =============================================================
    // CLEAN UP
    // =============================================================

    @Override
    protected void onDestroy() {

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

        return Math.round(
                dp * density
        );
    }
}
