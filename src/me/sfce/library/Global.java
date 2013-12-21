package me.sfce.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.actionbarsherlock.internal.widget.IcsToast;
import me.sfce.library.util.Http;
import me.sfce.library.util.MyTextAsyncResponseHandler;

import java.io.*;
import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: sfce
 * Date: 13-10-22
 * Time: 下午1:00
 */
public class Global {
    public static final boolean DEBUG = true;
    public static class Update {
        public static void checkNewVersion(final Context context, final boolean silent) {
            Http.Get.execute("/sly/version.txt", null, new MyTextAsyncResponseHandler(context, null) {
                @Override
                public void onSuccess(String content) {
                    String versionOnServer = content;
                    if (null != content) {
                        String versionInstalled = Global.Device.getSelfVersion(context).versionName;
                        if (versionOnServer.compareTo(versionInstalled) > 0) {
                            new AlertDialog.Builder(context).
                                    setMessage("检测到新版本，是否更新？\n最新版本：" + versionOnServer + "\n当前版本：" + versionInstalled)
                                    .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                downloadApk(context);
                                        }
                                    }).setNegativeButton("取消",null).create().show();
                        } else {
                            if (!silent) {
                                IcsToast.makeText(context, "当前已是最新版本", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        if (!silent) {
                            Toast.makeText(context, "当前已是最新版本", Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFinish() {
                }

                @Override
                public void onFailure(String responseBody, Throwable error) {
                }
            });
        }

        private static void downloadApk(final Context context) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Http.getAbsoluteUrl("/sly/SmsCleaner.apk"))));
        }
    }

    public static class System {
        public static void hideIME(Activity context) {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = context.getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏软键盘
            }
        }

        public static void hideIME(Activity context, View focus) {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (focus != null) {
                imm.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏软键盘
            }
        }

        public static void showIME(Activity context) {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = context.getCurrentFocus();
            if (view != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT); // 显示软键盘
            }
        }

        public static void showIME(Activity context, View focus) {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (focus != null) {
                imm.showSoftInput(focus, InputMethodManager.SHOW_IMPLICIT); // 显示软键盘
            }
        }
    }

    public static class Form {
        public final static String REGEX_TELEPHONE = "^(1(([35][0-9])|(47)|[8][01236789]))\\d{8}$";
        public final static String REGEX_NUMBER = "^[1-9]\\d*$";
        public static final String IDCARD = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";

        public static boolean isTelephone(CharSequence resource) {
            return resource.toString().matches(REGEX_TELEPHONE);
        }

        public static boolean isNumber(CharSequence resource) {
            return resource.toString().matches(REGEX_NUMBER);
        }

        public static boolean isIDCard(CharSequence resource) {
            return resource.toString().matches(IDCARD);
        }

        public static Spanned toLinkStyle(CharSequence resource) {
            return Html.fromHtml("<u><i>" + resource + "</i></u>");
        }
    }

    public static class Device {
        public static void installApk(Context context, File apk) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        public static String getImei(final Context ctx) {
            TelephonyManager telManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (DEBUG)
                return "359513040775545";
            //		return "358059041252309";
            String imei = telManager.getDeviceId();
            if (imei == null) {
                return "";
            }
            return imei;
        }

        public static String getImsi(final Context ctx) {
            TelephonyManager telManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = telManager.getSubscriberId();
            if (imsi == null) {
                return "";
            }
            return imsi;
        }

        public static String getMacAddress(final Context ctx) {
            WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String macText = info.getMacAddress();
            return macText;
        }

        /**
         * @return 获取状态栏高度
         */
        public static int getStatusBarHeight(final Context ctx) {
            Class<?> clazz = null;
            Object obj = null;
            Field field = null;
            int value = 0, statusBarHeight = 0;
            try {
                clazz = Class.forName("com.android.internal.R$dimen");
                obj = clazz.newInstance();
                field = clazz.getField("status_bar_height");
                value = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = ctx.getResources().getDimensionPixelSize(value);
                return statusBarHeight;
            } catch (Exception e1) {
                e1.printStackTrace();
                return 0;
            }
        }

        /**
         * @return 得到屏幕密度
         */
        public static float getDensity(final Context context) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            return density;
        }

        /**
         * @return 获取屏幕宽度
         */
        public static int getScreenWidth(final Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            if (getAndroidSDKVersion() >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Point point = new Point();
                display.getSize(point);
                return point.x;
            }
            return display.getWidth();
        }

        /**
         * @return 获取屏幕高度
         */
        public static int getScreenHeight(final Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            if (getAndroidSDKVersion() >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Point point = new Point();
                display.getSize(point);
                return point.y;
            }
            int height = display.getHeight();
            return height;
        }

        /**
         * 获取设备型号
         *
         * @return
         */
        public static String getDeviceModel() {
            String model = Build.MODEL;
            return model;
        }

        /**
         * 检查SDCard是否是可写的
         *
         * @return
         */
        public static boolean isSdcardWriteAble() {
            String state = Environment.getExternalStorageState();
            boolean externalStorageWriteable = false;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // We can read and write the media
                externalStorageWriteable = true;
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                // We can only read the media
                externalStorageWriteable = true;
            } else {
                // Something else is wrong. It may be one of many other states, but all we need
                // to know is we can neither read nor write
                externalStorageWriteable = false;
            }
            return externalStorageWriteable;
        }

        /**
         * 获取当前运行的SDK版本
         *
         * @return SDK版本
         */
        public static int getAndroidSDKVersion() {
            int version = 0;
            try {
                version = Integer.valueOf(Build.VERSION.SDK_INT);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return version;
        }

        /**
         * 获取软件自身的信息
         *
         * @return
         */
        public static PackageInfo getSelfVersion(final Context context) {
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                        PackageManager.GET_ACTIVITIES);
                return info;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static boolean isInstalled(Context context, String packageName) {
            PackageManager pm = context.getPackageManager();
            for (PackageInfo packageInfo : pm.getInstalledPackages(0)) {
                if (packageName.equals(packageInfo.packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class IOUtil {
        /**
         * 从输入流中读取所有的字节
         *
         * @param inStream
         * @return
         * @throws java.io.IOException
         */
        public static byte[] readStream(InputStream inStream) throws IOException {
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            return outSteam.toByteArray();
        }

        public static void string2File(String string, String path) throws IOException {
            File file = new File(path);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(string.getBytes());
            outStream.flush();
            outStream.close();
        }

        public static void write2File(byte[] bytes, String path) throws IOException {
            File file = new File(path);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(bytes);
            outStream.flush();
            outStream.close();
        }

        public interface FileWriterWatcher {
            void notify(int progress);

            void onFinish(File apk);

            boolean isAlive();

            void setAlive(boolean alive);
        }


        public static void write2File(byte[] datas, File file, FileWriterWatcher watcher) throws IOException {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            ByteArrayInputStream inStream = new ByteArrayInputStream(datas);
            FileOutputStream outStream = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int len;
            int sum = 0;
            while (watcher.isAlive() && (len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
                sum += len;
                watcher.notify(sum);
            }
            inStream.close();
            outStream.flush();
            outStream.close();
            watcher.onFinish(file);
        }

        public static void write2File(byte[] bytes, File file) throws IOException {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileOutputStream outStream = new FileOutputStream(file);

            outStream.write(bytes);
            outStream.flush();
            outStream.close();
        }

        public static String fileInAssets2String(Context context, String fileName) {
            try {
                return new String(readStream(context.getAssets().open(fileName)));
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }

        public static Bitmap getBitmapFromAsset(Context context, String strName) {
            String picPathString = "images/" + strName;

            AssetManager assetManager = context.getAssets();

            InputStream is = null;
            try {
                is = assetManager.open(picPathString);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        }
    }
}
