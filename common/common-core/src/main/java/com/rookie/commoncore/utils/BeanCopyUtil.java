package com.rookie.commoncore.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BeanCopyUtil extends BeanUtils{

    /**
     * 集合数据的拷贝
     * @param sources 数据源类
     * @param target 目标类::new(eg: UserVO::new)
     * @return 拷贝结果
     * @param <S> 源目标类型
     * @param <T> 目标对象类型
     * 复制单个对象可以传 T target；复制列表时，这种实现需要传入“创建目标对象的方法：Supplier<T>
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
        }

        return list;
    }

}
