package zhang.zhilong.mpermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zhangzhilong
 * @date: 2018/5/18
 * @des: 拒绝权限的注解
 */
@Target(ElementType.METHOD)// 放在什么位置  ElementType.METHOD 方法上面
@Retention(RetentionPolicy.RUNTIME)// 是编译时检测 还是 运行时检测
public @interface MPermissionGranted {
    int requestCode();// 请求码
}
