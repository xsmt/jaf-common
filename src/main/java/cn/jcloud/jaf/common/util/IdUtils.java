package cn.jcloud.jaf.common.util;

import org.apache.commons.lang3.RandomUtils;

/**
 * Id工具类
 * Created by Wei Han on 2016/5/11.
 */
public class IdUtils {

    /*
     * 项目起始纪元(此处取2016-01-01:00:00:00.000)
     */
    private static final long PROJECT_EPOCH = 1451577600L;

    /*
     * 集群间计数所占位数
     */
    public static final int COUNTER_BITS = 10;
    /*
     * 实例内计数所占位数
     */
    public static final int SEQUENCE_BITS = 13;

    /*
     * 时间位移数
     */
    private static final int TIME_SHIFT = COUNTER_BITS + SEQUENCE_BITS;

    /*
     * 集群间计数位移数
     */
    private static final int COUNTER_SHIFT = SEQUENCE_BITS;

    public static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    public static final long COUNTER_MASK = ~(-1L << COUNTER_BITS);

    private IdUtils() {
    }

    /**
     * 生成伪分布式id，在并发不高的情况下，不会重复
     */
    public static long dummyDistributedLongId() {
        long timestamp = System.currentTimeMillis() / 1000;
        long counter = RandomUtils.nextLong(0, COUNTER_MASK);
        long sequence = RandomUtils.nextLong(0, SEQUENCE_MASK);
        return generateId(timestamp, counter, sequence);
    }

    public static long generateId(long timestamp, long counter, long sequence) {
        return ((timestamp - PROJECT_EPOCH) << TIME_SHIFT) | (counter << COUNTER_SHIFT) | sequence;
    }

    public static void main(String[] args) {
        System.out.println(dummyDistributedLongId());
    }
}
