package zhang.zhilong.mpermission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

/**
 * @author: zhangzhilong
 * @date: 2018/5/18
 * @des: 权限管理类
 */
public class MPermission{

    private Object mObject;
    private int mRequestCode;
    private String[] mRequestPermission;

    private MPermission(Object object){
        this.mObject = object;
    }

    public static void  requestPermission(Activity activity, int requestCode, String ... permissions){
        MPermission.with(activity).requestCode(requestCode).
                requestPermission(permissions).request();

    }


    public static void  requestPermission(Fragment fragment, int requestCode, String[] permissions){
        MPermission.with(fragment).requestCode(requestCode).
                requestPermission(permissions).request();
    }

    public static void  requestPermission(Context context, int requestCode, String[] permissions){
        MPermission.with(context).requestCode(requestCode).
                requestPermission(permissions).request();
    }

    public static MPermission with(Activity activity){
        return new MPermission(activity);
    }

    public static MPermission with(Fragment fragment){
        return new MPermission(fragment);
    }

    public static MPermission with(Context context){return new MPermission(context);}

    public MPermission requestCode(int requestCode){
        this.mRequestCode = requestCode;
        return this;
    }

    public MPermission requestPermission(String... permissions){
        this.mRequestPermission = permissions;
        return this;
    }

    public void request() {
        if(!PermissionUtils.isOverMarshmallow()){
            PermissionUtils.executeGrantedMethod(mObject,mRequestCode);
        }else{
            String[] deniedPermissions = PermissionUtils.getDeniedPermissions(PermissionUtils.getActivity(mObject),mRequestPermission);

            if(deniedPermissions.length == 0){

                PermissionUtils.executeDeniedMethod(mObject,mRequestCode);

            }else {
                ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject),
                        deniedPermissions,
                        mRequestCode);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void request(final MPermissionListener listener) {
        if(!PermissionUtils.isOverMarshmallow()){
            listener.onPermissionGranted();
        }else{
            long currKey = System.currentTimeMillis();
            PermissionUtils.addListener(currKey,listener);
            MPermissionActivity.startMPermissionActivity(PermissionUtils.getActivity(mObject), mRequestPermission, currKey);
        }
    }

    /**
     * 处理申请权限的回调
     */
    public static void requestPermissionsResult(Object object,int requestCode,
                                                String[] permissions) {

        if(PermissionUtils.hasPermissions(PermissionUtils.getActivity(object),permissions)){
            PermissionUtils.executeGrantedMethod(object,requestCode);
        }else{
            PermissionUtils.executeDeniedMethod(object,requestCode);
        }
    }

    public static void onRequestPermissionsResult(Activity activity,long listenerKey, String[] permissions, int[] grantResults) {
        MPermissionListener listener = PermissionUtils.getListener(listenerKey);
        if(PermissionUtils.isGranted(grantResults) && PermissionUtils.hasPermissions(activity,permissions)){

            listener.onPermissionGranted();

            PermissionUtils.removeListener(listenerKey);
        }else{
            listener.onPermissionDenied();

            PermissionUtils.removeListener(listenerKey);
        }
    }
}
