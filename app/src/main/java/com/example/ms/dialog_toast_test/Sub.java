package com.example.ms.dialog_toast_test;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ms on 2016/04/08.
 */
public class Sub extends Activity {

    private final int REQUEST_CODE_PERMISSION = 1;

    private Context activityContext;
    private Context applicationContext;
    private Resources resources;

    private Activity activity;


    Sub(Context activityContext, Context applicationContext, Activity activity) {

        // 取得先検証
        this.activityContext   = activityContext;
        this.applicationContext = applicationContext;


        this.resources = activityContext.getResources();
        this.activity = activity;

    }


    void createDialog(final Context viewContext) {

        // 取得先検証
        //final Context context = activityContext;     // 落ちない
        //final Context context = applicationContext;  // 落ちる
        final Context context = viewContext;           // 落ちない

        new AlertDialog.Builder(context)
                .setTitle("aaa")
                .setMessage("test")
                .setPositiveButton("YES"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Resources resources = context.getResources();

                                Toast toast = Toast.makeText(context
                                        , resources.getString(R.string.app_name)
                                        , Toast.LENGTH_SHORT);
                                toast.show();

                            }
                        }
                )
                .setNegativeButton("NO"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }
                )
                .show();

    }


    void createToast(final Context viewContext) {

        // 取得先検証
        //final Context context = activityContext;     // 落ちない
        //final Context context = applicationContext;  // 落ちない
        final Context context = viewContext;           // 落ちない

        Toast toast = Toast.makeText(context
                , resources.getString(R.string.app_name)
                , Toast.LENGTH_SHORT);
        toast.show();

    }


    void checkPermission(final Context viewContext) {

        // 取得先検証
        //final Context context = activityContext;      // 落ちない
        //final Context context = applicationContext;   // 落ちるActivityのキャストができない
        //final Context context = viewContext;          // 未検証


        // 今まではAndroidManifest.xmlに記述された
        // 権限の許可をアプリのインストール時に求めていたが
        // Android6.0 Marshmallow (API 23)より
        // アプリが利用するタイミングでユーザーへ
        // 機能利用の許可を求める形に変更された
        if (Build.VERSION.SDK_INT < 23) {
            Toast toast = Toast.makeText(activityContext
                    , "チェック不要"
                    , Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (ActivityCompat.checkSelfPermission
                (activityContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            new AlertDialog.Builder(activityContext)
                    .setTitle("checkOK")
                    .setMessage("Permissionは既に許可されています。")
                    .setPositiveButton("OK", null)
                    .show();

        } else {

            new AlertDialog.Builder(activityContext)
                    .setTitle("checkNG")
                    .setMessage("Permissionの許可がされていません。")
                    .setPositiveButton("OK"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    requestPermissions(viewContext);

                                }
                            }
                    )
                    .show();

        }

    }


    private void requestPermissions(Context viewContext) {

        // 取得先検証
        final Context context = activityContext;
        //final Context context = applicationContext;
        //final Context context = viewContext

        //Activity activity = (Activity)activityContext;
        //Activity activity = (Activity)applicationContext;
        //Activity activity = (Activity)viewContext;


        Resources resources = context.getResources();

        Log.d("requestPermissions", "パーミッションリクエスト");

        // アクセス許可のダイアログを表示したことがあるか
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity
                , Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(activity
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , this.REQUEST_CODE_PERMISSION);

        }

        // アクセス許可のダイアログ表示1回目
        else {

            // Toastを表示して許可を促す
            Toast toast = Toast.makeText(context
                    , resources.getString(R.string.toast_request_permission)
                    , Toast.LENGTH_LONG);
            toast.show();

            ActivityCompat.requestPermissions(activity
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , this.REQUEST_CODE_PERMISSION);

        }

        // リクエスト結果はMainActivityのonRequestPermissionsResultにて受け取る

    }


    // requestPermissionsからは直接は受け取れない。MainActivityを通して受け取る
    public void onRequestPermissionsResult(
                       int requestCode
            , @NonNull String[] permissions
            , @NonNull int[] grantResults   ) {

        Log.d("Sub", "onRequestStart");

        // 取得先検証
        final Context context = activityContext;
        //final Context context = applicationContext;
        //final Context context = viewContext;

        final Resources res = context.getResources();

        if (requestCode == this.REQUEST_CODE_PERMISSION) {
            Log.d("onRequestPermission", "リクエストコード受信");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestPermission", "パーミッション許可");
            } else {
                Log.e("onRequestPermission", "AlertDialog表示");

                // 使用が拒否された場合、アプリ情報画面で許可の設定を促すダイアログを表示する
                new AlertDialog.Builder(context)
                        .setTitle(res.getString(R.string.alert_dialog_title_permission_error))
                        .setMessage(res.getString(R.string.alert_dialog_message_permission_error))
                        .setPositiveButton(res.getString(R.string.alert_dialog_yes)
                                ,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("onRequestPermission", "設定画面遷移");
                                        // 「はい」が押された時、アプリ情報画面に遷移する
                                        transitionSettings(context);
                                    }
                                }
                        )
                        .setNegativeButton(res.getString(R.string.alert_dialog_no)
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("onRequestPermission", "パーミッション拒否");
                                        // それでも拒否された場合はメッセージを表示してアプリを終了する
                                        finishApp(context);
                                    }
                                }
                        )
                        .show();
            }
        } else {
            Log.d("onRequestPermission", "リクエストコードが違います");
        }

    }


    private void transitionSettings(Context context) {

        String packageUri = "package:" + context.getPackageName();

        Log.d("packageUrl", packageUri);

        Activity activity = (Activity)context;

        Intent locationIntent = new Intent();
        locationIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        locationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        locationIntent.setData(Uri.parse(packageUri));

        try {

            activity.startActivity(locationIntent);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            // アプリ情報画面が見つからない場合は設定画面のトップに遷移する
            Intent settingsIntent = new Intent();
            settingsIntent.setAction(Settings.ACTION_SETTINGS);
            settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            startActivity(settingsIntent);

        }

    }


    private void finishApp(Context context) {

        Resources res = context.getResources();

        Toast toast = Toast.makeText(context
                , res.getString(R.string.toast_request_permission_denied)
                , Toast.LENGTH_LONG);
        toast.show();
        activity.finish();

    }


}
