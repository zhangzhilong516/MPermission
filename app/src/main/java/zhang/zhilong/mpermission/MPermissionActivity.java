/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zhang.zhilong.mpermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.WindowManager;

/**
 * @author: zhangzhilong
 * @date: 2018/7/1
 * @des: 授权Activity
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public final class MPermissionActivity extends Activity {

    private static final String REQUEST_PERMISSIONS = "request_permissions";
    private static final String REQUEST_PERMISSIONS_LISTENER = "request_permissions_listener";

    private long mListenerKey;

    public static void startMPermissionActivity(Context context, String[] permissions, long listenerKey) {
        Intent intent = new Intent(context, MPermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(REQUEST_PERMISSIONS, permissions);
        intent.putExtra(REQUEST_PERMISSIONS_LISTENER, listenerKey);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        String[] permissions = getIntent().getStringArrayExtra(REQUEST_PERMISSIONS);
        mListenerKey = getIntent().getLongExtra(REQUEST_PERMISSIONS_LISTENER,0);

        requestPermissions(permissions,1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        MPermission.onRequestPermissionsResult(this,mListenerKey,permissions,grantResults);

        finish();
    }
}
