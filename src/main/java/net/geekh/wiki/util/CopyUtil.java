package net.geekh.wiki.util;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 20:15
 */
public class CopyUtil {

    public static <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }

        T obj = null;
        try {
//          1.  obj = clazz.newInstance(); Java9以上不推荐使用
//           2.  使用Supplier接口：
//              Class<MyClass> clazz = MyClass.class;
//              Supplier<MyClass> supplier = clazz::newInstance;
//              MyClass instance = supplier.get();
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            obj = constructor.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BeanUtils.copyProperties(source, obj);
        return obj;
    }

    public static <T> List<T> copyList(List source, Class<T> clazz) {
        List<T> target = new ArrayList<>();
        if (!CollectionUtils.isEmpty(source)) {
            for (Object c : source) {
                T obj = copy(c, clazz);
                target.add(obj);
            }
        }
        return target;
    }
}
