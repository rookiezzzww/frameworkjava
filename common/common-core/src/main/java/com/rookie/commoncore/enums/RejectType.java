package com.rookie.commoncore.enums;


import lombok.Getter;

/**
 * 拒绝策略枚举
 */
@Getter
public enum RejectType {

    /**
     * AbortPolicy策略 枚举值：1，拒绝任务并抛异常
     */
    AbortPolicy(1),

    /**
     * CallerRunsPolicy策略 枚举值：2，通常由提交任务的线程自己执行
     */
    CallerRunsPolicy(2),

    /**
     * DiscardOldestPolicy策略 枚举值：3，丢弃最旧的任务
     */
    DiscardOldestPolicy(3),

    /**
     * DiscardPolicy策略 枚举值：4，直接丢弃任务
     */
    DiscardPolicy(4);


    private Integer value;

    RejectType(Integer value) {
        this.value = value;
    }
}
