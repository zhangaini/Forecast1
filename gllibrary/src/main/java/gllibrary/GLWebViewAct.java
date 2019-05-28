package gllibrary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gl.gllibrary.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gllibrary.utils.GLActivityManager;
import gllibrary.utils.GLConstants;
import gllibrary.utils.GLLogUtil;
import gllibrary.utils.GLMakeQRCodeUtil;
import gllibrary.utils.GLToolImage;
import gllibrary.view.GLLoaderProgressDialog;

/**
 * 基本的网页视图
 */
public class GLWebViewAct extends Activity {
    // 基本组件名称
    public String TAG;
    private String urlStr;
    public Activity mActivity;
    WebView mWebView;
    Context mContext;
    LinearLayout mWebPagely;
    TextView mTitle;
    GLLoaderProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gl_act_webview);
        initView(savedInstanceState);
    }


    public void init() {
        mWebView = (WebView) findViewById(R.id.my_web);
        mWebPagely = (LinearLayout) findViewById(R.id.webview_page_ly);
        mTitle = (TextView) findViewById(R.id.gl_title);
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        TAG = this.getClass().getSimpleName();
        mActivity = this;
        mContext = this;
        findViewById(R.id.refresh).setVisibility(View.GONE);
    }


    protected void initView(Bundle savedInstanceState) {

        init();
        GLActivityManager.getAppManager().addTempAct(this);
        createShowDialog();
        urlStr = getIntent().getStringExtra("mUrl");
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true); //加上这句话才能使用javascript方法
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.requestFocus();
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setSaveFormData(false);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 30);//设置缓冲大小，我设的是8M
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        }
        //先阻塞加载图片
        mWebView.getSettings().setBlockNetworkImage(false);
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }

        /** web页面自适应 */
        mWebView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);
                mWebView.setDrawingCacheEnabled(true);
            }

            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                take();
                return true;
            }


            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                take();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                take();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                take();
            }
        });

        listenView();
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                if (url.startsWith("alipays:") || url.startsWith("alipay")) {
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    } catch (Exception e) {
                        new AlertDialog.Builder(GLWebViewAct.this)
                                .setMessage("未检测到支付宝客户端，请安装后重试。")
                                .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                        GLWebViewAct.this.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                    return true;
                }
                // ------- 处理结束 -------

                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }
                if (url.startsWith("tel:")) {//支持拨打电话
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                UrlSelectionRule(view, url);
                return true;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                startTimer();
                mTitle.setText("正在加载中...");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                cancelTimer();
                findViewById(R.id.refresh).setVisibility(View.GONE);

                if (view != null) {
                   /* if (TextUtils.isEmpty(view.getTitle()))//默认是地址，需要后台要么传apptitle 或者修改titlte
                        mTitle.setText("体育彩票");
                    else
                        mTitle.setText(view.getTitle());*/
                    mTitle.setText("体育彩票");
                }

                closeDialog();
            }

            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                GLLogUtil.e("========", "onReceivedError");
                //回调失败的相关操作
                findViewById(R.id.refresh).setVisibility(View.VISIBLE);
            }
//            @Override
//            public void onReceivedError(WebView view, int errorCode,
//                                        String description, String failingUrl) {
//                // TODO Auto-generated method stub
//                super.onReceivedError(view, errorCode, description, failingUrl);
//                findViewById(R.id.refresh).setVisibility(View.VISIBLE);
//            }
        });
        mWebView.addJavascriptInterface(JSInterface(), "appjs");
        loadData();
    }

    /**
     * 监听
     */
    private void listenView() {
        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        findViewById(R.id.gl_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.gl_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else
                    finish();
            }
        });
    }


    protected void loadData() {
        GLLogUtil.e(TAG, urlStr);
        mWebView.loadUrl(urlStr);
    }

    public void createShowDialog() {
        if (dialog == null)
            dialog = GLLoaderProgressDialog.createDialog(mContext);
        dialog.show();
    }

    /**
     * 关闭加载
     */
    public void closeDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeDialog();
    }

    private MyCountDownTimer timer;
    private final long TIME = 30 * 1000L;
    private final long INTERVAL = 1000L;

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;

        }

        @Override
        public void onFinish() {
            findViewById(R.id.refresh).setVisibility(View.VISIBLE);
            cancelTimer();
        }
    }


    /**
     * 开始倒计时
     */
    private void startTimer() {
        findViewById(R.id.refresh).setVisibility(View.GONE);
        if (timer == null) {
            timer = new MyCountDownTimer(TIME, INTERVAL);
        }
        timer.start();
    }

    /**
     * 取消倒计时
     */
    private void cancelTimer() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    /**
     * 跳转选择，进行URL规则判断，若是正常URL则加载页面。
     */
    private void UrlSelectionRule(WebView view, String url) {

        GLLogUtil.e("aaa====", url);
        if (url.contains("platformapi/startApp")) {
            startAlipayActivity(url);
            // android  6.0 两种方式获取intent都可以跳转支付宝成功,7.1测试不成功
        } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                && (url.contains("platformapi") && url.contains("startApp"))) {
            startAlipayActivity(url);
        } else {
            urlStr = url;
            mWebView.loadUrl(url);
        }

    }

    // 调起支付宝并跳转到指定页面
    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GLConstants.CODE_SCAN && resultCode == RESULT_OK) {
            String url = data.getStringExtra("result");
            mWebView.loadUrl(url);
//            Intent intent = new Intent(mContext,WebViewAct.class);
//            intent.putExtra("mUrl",data.getStringExtra("result"));
//            startActivityForResult(intent, Constants.ACT_REFRESH);
        }
        if (requestCode == GLConstants.ACT_REFRESH && resultCode == RESULT_OK) {
            // if(TextUtils.isEmpty(data.getStringExtra("redirectUrl")))
            loadData();
        }
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {

                if (result != null) {
                    String path = getPath(getApplicationContext(),
                            result);
                    Uri uri = Uri.fromFile(new File(path));
                    mUploadMessage
                            .onReceiveValue(uri);
                } else {
                    mUploadMessage.onReceiveValue(imageUri);
                }
                mUploadMessage = null;


            }
        }

    }

    @SuppressWarnings("null")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;

        if (resultCode == RESULT_OK) {

            if (data == null) {

                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }


    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        // Create the storage directory if it does not exist
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * H5 JS交互区
     *
     * @return
     */
    private Object JSInterface() {
        Object insertObj = new Object() {
            @JavascriptInterface//关闭当前ACTIVITY
            public void appToBack() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });

            }

            @JavascriptInterface//重定向
            public void toAppRedirect(final String url) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, GLWebViewAct.class);
                        intent.putExtra("mUrl", url);
                        startActivity(intent);
                    }
                });
            }


            @JavascriptInterface//返回到返回时刷新上一个界面
            public void refreshPreView(String market) {

                setResult(RESULT_OK, new Intent());
            }

            @JavascriptInterface//返回到入口页
            public void returnHome(String market) {
                GLActivityManager.getAppManager().closeTempAct();
            }

            @JavascriptInterface//保存图片并打开支付宝
            public void toSaveAndAplipay(final String str) {//地址|金额|订单号|时间
                GLLogUtil.e("str===", str);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new MyAsyncTask().execute(str);
                        } catch (Exception e) {
                            e.printStackTrace();
                            closeDialog();
                            Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @JavascriptInterface//分享长图 当url="" 则分享长图，如果url地址非空，则为分享地址，url ="地址|标题|内容|logo地址" | 隔开
            public void appShareLongImage(final String url) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        };
        return insertObj;
    }

    class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            //这里是开始线程之前执行的,是在UI线程
            createShowDialog();
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //这是在后台子线程中执行的
            Bitmap bitmap = null;
            try {
                saveCode(params[0]); //保存二维码照片
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onCancelled() {
            //当任务被取消时回调
            super.onCancelled();
            closeDialog();
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //当任务执行完成是调用,在UI线程
            openAlipayScan();
            closeDialog();
        }
    }


    /**
     * @author xujp
     * @time 2017/3/30 0030  上午 9:36
     * @describe 保存二维码照片
     */
    private void saveCode(String str) {
        String[] strs = str.split("\\|");


        //异步操作相关代码
        Bitmap logo = GLMakeQRCodeUtil.gainBitmap(mContext, R.drawable.gl_logo);
        Bitmap background = null;

        background = GLMakeQRCodeUtil.gainBitmap(mContext, R.drawable.gl_bg_order_qrcode);
        int height = background.getWidth() * 7 / 15;//mCode.getMeasuredHeight();
        int width = height;//mCode.getMeasuredWidth();
        // Bitmap markBMP = GLGLMakeQRCodeUtil.gainBitmap(mContext, R.drawable.water);
        try {
            //获得二维码图片
            Bitmap bitmap = GLMakeQRCodeUtil.makeQRImage(logo,
                    strs[0],
                    width, height);

            bitmap = GLMakeQRCodeUtil.addBackground(bitmap, background, mContext, strs[1], strs[2], strs[3]);
            //加水印
            //bitmap = GLGLMakeQRCodeUtil.composeWatermark(bitmap, markBMP);
            GLToolImage.saveImageToGallery(
                    mContext, bitmap);
            logo.recycle();
            background.recycle();
            //markBMP.recycle();
            bitmap.recycle();


        } catch (Exception e) {
            closeDialog();
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 调用支付宝扫一扫
     */
    private void openAlipayScan() {
        try {
            Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "打开支付宝失败，请手动前往支付宝", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {

        // 防止弹窗后退出当前Activity的崩溃
        if (mWebView != null) {
            mWebView.destroy();
            // 防止在3.X系统上，ZoomButtonsController图标自动消失前退出当前Activity的话，就会报上面的这些异常。
            mWebView.setVisibility(View.GONE);
            try {
                mWebPagely.removeView(mWebView);
                mWebView.removeAllViews();
                mWebView.destroy();

            } catch (Exception e) {
                GLLogUtil.e(TAG, e.toString());
            }
        }
        System.gc();
        // 表示java虚拟机会做一些努力运行已被丢弃对象（即没有被任何对象引用的对象）的 finalize
        // 方法，前提是这些被丢弃对象的finalize方法还没有被调用过
        System.runFinalization();
        super.onDestroy();
    }


}