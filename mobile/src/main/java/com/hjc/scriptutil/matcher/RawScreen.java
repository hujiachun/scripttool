package com.hjc.scriptutil.matcher;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class RawScreen {

    public final int width;
    public final int height;
    private byte[] raws;

    public RawScreen(byte[] raw) {

        this.width = ((raw[1] & 0xff) << 8) | (raw[0] & 0xff);
        this.height = ((raw[5] & 0xff) << 8) | (raw[4] & 0xff);

        if (raw.length == width * height * 4 + 12) {
            this.raws = raw;

        } else {

            this.raws = raw;
            throw new RuntimeException("数据长度不能通过验证");
        }

    }

    /**
     * 截图原始byte数据，包含12字节的宽高深度前缀
     *
     * @return
     */
    public byte[] raw() {
        return raws;
    }

    /**
     * 将屏幕按照指定的缩放比例保存为BMP
     *
     * @param scale 缩放比例，小于1则缩小
     * @return
     */
    public Bitmap bitmap(float scale) {
        Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        bmp.copyPixelsFromBuffer(ByteBuffer.wrap(Arrays.copyOfRange(this.raws, 12, this.raws.length)));
        if (scale > 0) {
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        }
        return bmp;
    }
}
