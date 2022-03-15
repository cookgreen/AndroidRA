package com.android.redalert;

public class BytesAccessReader {
    private byte[] bytes;
    private long pos;

    public BytesAccessReader(byte[] bytes)
    {
        this.bytes = bytes;
        this.pos = 0;
    }

    public int read(byte[] bytesToRead, long pos)
    {
        try {
            bytesToRead = Utils.grabBytes(bytes, (int)pos, bytesToRead.length);
            this.pos = pos + bytes.length;
            return bytesToRead.length;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            //e.printStackTrace();
            return -1;
        }
    }

    public void setPos(long newPos){
        pos = newPos;
    }

    public long getPos(){
        return pos;
    }
}
