package com.android.redalert;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Utils {

    public static byte[] grabBytes(byte[] original, int offset, int length)
    {
        byte[] newBytes = new byte[length];
        int index = 0;
        int dstIndx = offset + length;
        for(int i = offset; i < dstIndx; i++)
        {
            newBytes[index] = original[i];
            index++;
        }
        return newBytes;
    }

    public static ByteBuffer readRemainingStream(InputStream inputStream)
    {
        ArrayList<ByteBuffer> bytes = new ArrayList<>();
        int size = 0;
        while (true) {
            byte[] byteArr = new byte[1024];
            int read = -1;
            ByteBuffer bytedata = null;
            try {
                read = inputStream.read(byteArr);
                bytedata = ByteBuffer.wrap(byteArr);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (read > 0) {
                size += read;
                bytes.add((ByteBuffer)bytedata.flip());
            }
            if (read != 1024) {
                break;
            }
        }

        ByteBuffer data = ByteBuffer.allocate(size);
        for (ByteBuffer bytedata: bytes) {
            data.put(bytedata);
        }
        data.rewind();
        return data;
    }
}
