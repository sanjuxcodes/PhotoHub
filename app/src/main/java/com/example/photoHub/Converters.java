package com.example.photoHub;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public static List<byte[]> fromByteArrayArray(byte[] data) {
        List<byte[]> list = new ArrayList<>();
        if (data == null || data.length == 0) return list;

        int i = 0;
        while (i < data.length) {
            int length = ((data[i] & 0xFF) << 24) | ((data[i + 1] & 0xFF) << 16)
                    | ((data[i + 2] & 0xFF) << 8) | (data[i + 3] & 0xFF);
            i += 4;
            byte[] item = Arrays.copyOfRange(data, i, i + length);
            list.add(item);
            i += length;
        }
        return list;
    }

    @TypeConverter
    public static byte[] toByteArrayArray(List<byte[]> list) {
        if (list == null || list.isEmpty()) return new byte[0];

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (byte[] item : list) {
            int length = item.length;
            stream.write((length >>> 24) & 0xFF);
            stream.write((length >>> 16) & 0xFF);
            stream.write((length >>> 8) & 0xFF);
            stream.write(length & 0xFF);
            stream.write(item, 0, item.length);
        }
        return stream.toByteArray();
    }
}
