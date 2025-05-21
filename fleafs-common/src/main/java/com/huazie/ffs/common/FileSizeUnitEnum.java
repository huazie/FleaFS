package com.huazie.ffs.common;

import java.math.BigDecimal;

/**
 * 文件大小单位枚举
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public enum FileSizeUnitEnum {

    BYTES("B", 1),
    KILOBYTES("KB", 1024),
    MEGABYTES("MB", 1024 * 1024),
    GIGABYTES("GB", 1024 * 1024 * 1024),
    TERABYTES("TB", 1024L * 1024 * 1024 * 1024);

    private final String unit;
    private final long bytes;

    FileSizeUnitEnum(String unit, long bytes) {
        this.unit = unit;
        this.bytes = bytes;
    }

    /**
     * 将字节数转换为当前单位
     *
     * @param bytes 字节数
     * @return 转换后的值（带两位小数）
     * @since 1.0.0
     */
    public double convertFromBytes(long bytes) {
        return (double) Math.round((bytes / (double) this.bytes) * 100) / 100;
    }

    /**
     * 将当前单位的值转换为字节数（支持小数单位）
     *
     * @param size 当前单位的值
     * @return 字节数
     * @throws ArithmeticException 如果结果超出 double 范围
     * @since 1.0.0
     */
    public double convertToBytes(double size) {
        BigDecimal result = BigDecimal.valueOf(size).multiply(BigDecimal.valueOf(this.bytes));
        if (result.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) > 0) {
            throw new ArithmeticException("Result exceeds maximum double value");
        }
        if (result.compareTo(BigDecimal.valueOf(-Double.MAX_VALUE)) < 0) {
            throw new ArithmeticException("Result exceeds minimum double value");
        }
        return result.doubleValue();
    }

    public long getBytes() {
        return bytes;
    }

    public String getUnit() {
        return unit;
    }
}
