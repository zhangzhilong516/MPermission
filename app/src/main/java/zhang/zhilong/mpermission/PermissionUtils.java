package zhang.zhilong.mpermission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: zhangzhilong
 * @date: 2018/5/18
 * description: 处理权限请求的工具类
 */
public final class PermissionUtils {

    private PermissionUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static HashMap<Long,MPermissionListener> mListeners;

    /**
     * 判断其是不是6.0以上的版本
     * Marshmallow 棉花糖  6.0
     * @return
     */
    public static boolean isOverMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 执行成功的方法
     */
    public static void executeGrantedMethod(Object reflectObject, int requestCode) {

        Method[] methods = reflectObject.getClass().getDeclaredMethods();

        for (Method method:methods){

            MPermissionGranted succeedMethod =  method.getAnnotation(MPermissionGranted.class);

            if(succeedMethod != null){

                int methodCode = succeedMethod.requestCode();

                if(methodCode == requestCode){

                    executeMethod(reflectObject,method);

                }
            }
        }
    }

    /**
     * 反射执行该方法
     */
    private static void executeMethod(Object reflectObject, Method method) {
        try {
            method.setAccessible(true); // 允许执行私有方法
            method.invoke(reflectObject,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取没有授予的权限
     * @param object  Activity or Fragment
     * @return 没有授予过得权限
     */
    public static String[] getDeniedPermissions(Context context, String[] requestPermissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String requestPermission:requestPermissions){
            if(ContextCompat.checkSelfPermission(context, requestPermission)
                    != PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(requestPermission);
            }
        }
        return deniedPermissions.toArray(new String[deniedPermissions.size()]);
    }

    /**
     * 是否获取所有权限
     * @param object  Activity or Fragment
     * @return 没有授予过得权限
     */
    public static boolean hasPermissions(Context context, String[] requestPermissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String requestPermission:requestPermissions){
            if(ContextCompat.checkSelfPermission(context, requestPermission)
                    == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }


    /**
     * 判断一组授权结果是否为授权通过
     * @param grantResult
     * @return
     */
    public static boolean isGranted(@NonNull int... grantResult) {

        if (grantResult.length == 0) {
            return false;
        }

        for (int result : grantResult) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取Context
     * @param object
     * @return
     */
    public static Activity getActivity(Object object) {
        if(object instanceof Activity){
            return (Activity)object;
        }
        if(object instanceof Fragment){
            return ((Fragment)object).getActivity();
        }
        return null;
    }


    /**
     * 执行PermissionDenied
     * @param reflectObject
     * @param requestCode
     */
    public static void executeDeniedMethod(Object reflectObject, int requestCode) {

        Method[] methods = reflectObject.getClass().getDeclaredMethods();

        for (Method method:methods){

            MPermissionDenied failMethod =  method.getAnnotation(MPermissionDenied.class);

            if(failMethod != null){

                int methodCode = failMethod.requestCode();

                if(methodCode == requestCode){

                    executeMethod(reflectObject,method);
                }
            }
        }
    }


    public static void addListener(Long key , MPermissionListener value){
        if(mListeners == null){
            mListeners = new HashMap<>();
        }
        mListeners.put(key,value);
    }

    public static MPermissionListener getListener(Long key){
        return mListeners.get(key);
    }

    public static void removeListener(Long key){
        if(mListeners != null){

            mListeners.remove(key);

            if(mListeners.isEmpty()){
                mListeners = null;
            }
        }
    }
}
