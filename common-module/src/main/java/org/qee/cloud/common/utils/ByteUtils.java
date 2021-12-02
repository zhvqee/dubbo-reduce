package org.qee.cloud.common.utils;

/**
 * 大端模式，dubbo 的Bytes是小端模式
 */
public class ByteUtils {

    public static void writeByte(byte[] bytes, int localtion, int id) {
        bytes[localtion] = (byte) id;
    }

    public static int readByte(byte[] bytes, int localtion) {
        return bytes[localtion];
    }


    /**
     * @param bytes
     * @param localtion
     * @param value     例如  Ox1234
     */
    public static void writeShort(byte[] bytes, int localtion, short value) {
        bytes[localtion] = (byte) (value >>> 8); //Ox12
        bytes[localtion + 1] = (byte) value;  //ox34
    }


    /**
     * id  4字节    0X11,22,33,44
     * 大端模式
     *
     * @param bytes
     * @param localtion
     * @param id
     */
    // 0X11223344
    // 0  ,  1  ,  2  , 3
    //  11   22   33   44
    public static void writeInt(byte[] bytes, int localtion, int id) {
        bytes[localtion + 3] = (byte) id;
        bytes[localtion + 2] = (byte) (id >>> 8);
        bytes[localtion + 1] = (byte) (id >>> 16);
        bytes[localtion] = (byte) (id >>> 24);
    }

    // 0X11223344
    // 0  ,  1  ,  2  , 3
    //  11   22   33   44

    public static int byte2Int(byte[] bytes, int location) {
        return ((bytes[location + 0] & 0xFF) << 24) +
                ((bytes[location + 1] & 0xFF) << 16) +
                ((bytes[location + 2] & 0xFF) << 8) +
                ((bytes[location + 3] & 0XFF));
    }

    public static void main(String[] args) {
        byte[] header = new byte[16];
        writeInt(header, 3, 200);
        int l = byte2Int(header, 3);
        System.out.println(l);

    }


    /**
     * id  8字节    0X11,22,33,44,55,66,77,88
     *
     * @param bytes
     * @param localtion
     * @param id
     */
    public static void writeLong(byte[] bytes, int localtion, long id) {
        bytes[localtion + 7] = (byte) id;
        bytes[localtion + 6] = (byte) (id >>> 8);
        bytes[localtion + 5] = (byte) (id >>> 16);
        bytes[localtion + 4] = (byte) (id >>> 24);
        bytes[localtion + 3] = (byte) (id >>> 32);
        bytes[localtion + 2] = (byte) (id >>> 40);
        bytes[localtion + 1] = (byte) (id >>> 48);
        bytes[localtion] = (byte) (id >>> 56);
    }


    public static long byte2Long(byte[] bytes, int location) {
        return ((bytes[location + 7] & 0xFF) << 0) +
                ((bytes[location + 6] & 0xFF) << 8) +
                ((bytes[location + 5] & 0xFF) << 16) +
                ((bytes[location + 4] & 0xFF) << 24) +
                ((bytes[location + 3] & 0xFF) << 32) +
                ((bytes[location + 2] & 0xFF) << 40) +
                ((bytes[location + 1] & 0xFF) << 48) +
                ((bytes[location + 0]) << 56);
    }
}
