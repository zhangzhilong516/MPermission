package zhang.zhilong.mpermission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Test(View v) {
//        MPermission.with(this)
//                .requestPermission(Manifest.permission.CALL_PHONE)
//                .requestCode(12)
//                .request();

        MPermission.with(this).requestPermission(Manifest.permission.CALL_PHONE)
                .request(new MPermissionListener() {
                    @Override
                    void onPermissionGranted() {
                        callPhone("10086");
                    }

                    @Override
                    void onPermissionDenied() {

                    }
                });
    }
    private final int request_code = 12;
    @MPermissionGranted(requestCode = 12)
    public void onPermissionGranted(){
        callPhone("10086");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermission.requestPermissionsResult(this,requestCode,permissions);
    }

    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
