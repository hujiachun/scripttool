package com.hjc.scriptutil.matcher;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ScreenUtil {

    /**
     * 截图当前屏幕为原始RGB对象
     *
     * @return
     */
    public static RawScreen rawScreenshot() {

        try {

            InputStream raw = Runtime.getRuntime().exec("screencap").getInputStream();
            ByteArrayOutputStream ous = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 100];
            int read = 0;
            while ((read = raw.read(buffer, 0, buffer.length)) != -1) {
                ous.write(buffer, 0, read);
            }
            buffer = ous.toByteArray();

            return new RawScreen(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 取屏幕的第row行RGBA颜色值,只适用于竖屏
     *
     * @param raw 屏幕RGB对象
     * @param row 行下标
     * @return
     */
    public static byte[] rawColor(RawScreen raw, int row) {
        if (raw != null) {
            int starts = row * raw.width * 4 + 12;
            int ends = starts + raw.width * 4;
            return Arrays.copyOfRange(raw.raw(), starts, ends);
        }
        return null;
    }

    /**
     * 获取定制RawScreen的原始RGBA数据
     *
     * @param raw RawScreen对象
     * @return
     */
    public static byte[] buffer(RawScreen raw) {
        if (raw != null) {
            return raw.raw();
        }
        return null;
    }
}
