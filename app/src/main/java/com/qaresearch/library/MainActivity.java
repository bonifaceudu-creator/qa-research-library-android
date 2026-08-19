package com.qaresearch.library;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    private WebView webView;
    private View launchScreen;

    private boolean pageLoaded = false;
    private boolean minimumTimeReached = false;

    private static final long SPLASH_DURATION = 4500;

    private static final String START_URL =
            "https://bonifaceudu-creator.github.io/qa-research-library-app/";

    private final Handler splashHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * =========================================================
         * PREVENT WHITE FLASH
         * =========================================================
         */

        Window window = getWindow();

        window.setStatusBarColor(Color.rgb(6, 42, 82));
        window.setNavigationBarColor(Color.rgb(6, 42, 82));

        window.setBackgroundDrawable(
                new ColorDrawable(Color.rgb(6, 42, 82))
        );

        /*
         * =========================================================
         * MAIN CONTAINER
         * =========================================================
         */

        FrameLayout container = new FrameLayout(this);

        container.setBackgroundColor(
                Color.rgb(6, 42, 82)
        );

        /*
         * =========================================================
         * WEBVIEW
         * =========================================================
         */

        webView = new WebView(this);

        webView.setBackgroundColor(Color.WHITE);

        webView.clearCache(true);

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

        /*
         * Keep website hidden while splash is displayed.
         */

        webView.setVisibility(View.INVISIBLE);

        /*
         * =========================================================
         * WEBVIEW CLIENT
         * =========================================================
         */

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                String url =
                        request.getUrl().toString();

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

                /*
                 * The splash does NOT disappear immediately.
                 * It waits for the minimum display time.
                 */

                checkSplashReady();
            }
        });

        /*
         * Add WebView.
         */

        container.addView(
                webView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );

        /*
         * =========================================================
         * PROFESSIONAL SPLASH SCREEN
         * =========================================================
         */

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
                dpToPx(25),
                dpToPx(25),
                dpToPx(25),
                dpToPx(25)
        );

        /*
         * =========================================================
         * LOGO
         * =========================================================
         *
         * Uses the EXISTING app_icon resource.
         *
         * FIT_CENTER prevents the logo from being cropped.
         */

        ImageView logo =
                new ImageView(this);

        logo.setImageResource(
                R.drawable.app_icon
        );

        logo.setScaleType(
                ImageView.ScaleType.FIT_CENTER
        );

        int logoWidth = dpToPx(210);
        int logoHeight = dpToPx(210);

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(
                        logoWidth,
                        logoHeight
                );

        logoParams.gravity = Gravity.CENTER;

        logoParams.bottomMargin =
                dpToPx(24);

        splashLayout.addView(
                logo,
                logoParams
        );

        /*
         * =========================================================
         * QA & RESEARCH
         * =========================================================
         */

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

        title.setLetterSpacing(0.08f);

        splashLayout.addView(
                title,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        /*
         * =========================================================
         * DIGITAL LIBRARY
         * =========================================================
         */

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

        subtitle.setLetterSpacing(0.15f);

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

        /*
         * =========================================================
         * DIVIDER
         * =========================================================
         */

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

        /*
         * =========================================================
         * DEPARTMENT
         * =========================================================
         */

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

        /*
         * =========================================================
         * LOCATION
         * =========================================================
         */

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

        /*
         * =========================================================
         * ANIMATED SLOGAN
         * =========================================================
         */

        TextView slogan =
                new TextView(this);

        slogan.setText("");

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

        slogan.setLetterSpacing(0.08f);

        LinearLayout.LayoutParams sloganParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        sloganParams.topMargin =
                dpToPx(60);

        sloganParams.bottomMargin =
                dpToPx(22);

        splashLayout.addView(
                slogan,
                sloganParams
        );

        /*
         * =========================================================
         * LOADING INDICATOR
         * =========================================================
         */

        ProgressBar progressBar =
                new ProgressBar(this);

        progressBar.setIndeterminate(true);

        LinearLayout.LayoutParams progressParams =
                new LinearLayout.LayoutParams(
                        dpToPx(32),
                        dpToPx(32)
                );

        progressParams.gravity =
                Gravity.CENTER;

        splashLayout.addView(
                progressBar,
                progressParams
        );

        /*
         * =========================================================
         * SAVE SPLASH VIEW
         * =========================================================
         */

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

        /*
         * =========================================================
         * SET CONTENT VIEW
         * =========================================================
         */

        setContentView(container);

        /*
         * =========================================================
         * ANIMATE SLOGAN
         * =========================================================
         */

        animateSlogan(slogan);

        /*
         * =========================================================
         * MINIMUM SPLASH DISPLAY TIME
         * =========================================================
         */

        splashHandler.postDelayed(
                new Runnable() {

                    @Override
                    public void run() {

                        minimumTimeReached = true;

                        checkSplashReady();
                    }

                },
                SPLASH_DURATION
        );

        /*
         * =========================================================
         * LOAD WEBSITE
         * =========================================================
         */

        if (savedInstanceState == null) {

            webView.loadUrl(
                    START_URL
            );

        } else {

            webView.restoreState(
                    savedInstanceState
            );

            pageLoaded = true;

            minimumTimeReached = true;

            webView.setVisibility(
                    View.VISIBLE
            );

            launchScreen.setVisibility(
                    View.GONE
            );
        }
    }

    /*
     * =============================================================
     * ANIMATED SLOGAN
     * =============================================================
     */

    private void animateSlogan(
            final TextView slogan) {

        final String text =
                "THE BEDROCK OF INDUSTRIALIZATION";

        final Handler handler =
                new Handler();

        final int[] index = {0};

        handler.postDelayed(
                new Runnable() {

                    @Override
                    public void run() {

                        if (index[0] <= text.length()) {

                            slogan.setText(
                                    text.substring(
                                            0,
                                            index[0]
                                    )
                            );

                            index[0]++;

                            handler.postDelayed(
                                    this,
                                    65
                            );
                        }
                    }

                },
                900
        );
    }

    /*
     * =============================================================
     * SPLASH CONTROL
     * =============================================================
     */

    private void checkSplashReady() {

        if (!pageLoaded ||
                !minimumTimeReached) {

            return;
        }

        if (launchScreen == null) {

            return;
        }

        /*
         * Show the homepage.
         */

        webView.setVisibility(
                View.VISIBLE
        );

        /*
         * Smoothly fade the splash away.
         */

        AlphaAnimation fade =
                new AlphaAnimation(
                        1.0f,
                        0.0f
                );

        fade.setDuration(500);

        fade.setFillAfter(true);

        fade.setAnimationListener(
                new android.view.animation.Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(
                            android.view.animation.Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(
                            android.view.animation.Animation animation) {

                        launchScreen.setVisibility(
                                View.GONE
                        );
                    }

                    @Override
                    public void onAnimationRepeat(
                            android.view.animation.Animation animation) {
                    }
                }
        );

        launchScreen.startAnimation(
                fade
        );
    }

    /*
     * =============================================================
     * PDF DETECTION
     * =============================================================
     */

    private boolean isPdf(String url) {

        if (url == null) {

            return false;
        }

        return url
                .toLowerCase()
                .contains(".pdf");
    }

    /*
     * =============================================================
     * PDF OPENING
     * =============================================================
     *
     * First tries to open the PDF using an Android PDF viewer/browser.
     *
     * If no PDF application is available, it falls back to
     * DownloadManager.
     */

    private void openPdf(String url) {

        try {

            Uri uri =
                    Uri.parse(url);

            Intent intent =
                    new Intent(
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

            downloadPdf(url);
        }
    }

    /*
     * =============================================================
     * PDF DOWNLOAD FALLBACK
     * =============================================================
     */

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

                manager.enqueue(request);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*
     * =============================================================
     * ANDROID BACK BUTTON
     * =============================================================
     */

    @Override
    public void onBackPressed() {

        if (webView != null &&
                webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();
        }
    }

    /*
     * =============================================================
     * SAVE WEBVIEW STATE
     * =============================================================
     */

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

    /*
     * =============================================================
     * CLEAN UP
     * =============================================================
     */

    @Override
    protected void onDestroy() {

        splashHandler.removeCallbacksAndMessages(
                null
        );

        if (webView != null) {

            webView.stopLoading();
            webView.destroy();
        }

        super.onDestroy();
    }

    /*
     * =============================================================
     * DP TO PIXELS
     * =============================================================
     */

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
