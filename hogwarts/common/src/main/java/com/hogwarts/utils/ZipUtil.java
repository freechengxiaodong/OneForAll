package com.hogwarts.utils;//package com.tools.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 字符串压缩|解压缩
 *
 * @author caohaihong
 * @email caohaihong@wanda.cn
 * @time 2016年12月9日 下午4:03:01
 * @description
 */
public class ZipUtil {

    private static final String UTF8_ENCODING = "UTF-8";

    public static byte[] gzip(String sourceString) throws IOException {
        if (StringUtils.isNotBlank(sourceString)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ;
            GZIPOutputStream gzip = null;
            try {
                gzip = new GZIPOutputStream(out);
                gzip.write(sourceString.getBytes(UTF8_ENCODING));
            } finally {
                if (null != gzip) {
                    gzip.close();
                }
            }
            return out.toByteArray();
        }
        return null;
    }

    /**
     * 解压缩目标字符数组
     *
     * @param compressedBytes
     * @return
     * @throws IOException
     */
    public static String gunzip(byte[] compressedBytes) throws IOException {
        String decompressedString = null;
        if (null != compressedBytes) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = null;
            GZIPInputStream ginzip = null;
            try {
                in = new ByteArrayInputStream(compressedBytes);
                ginzip = new GZIPInputStream(in);

                byte[] buffer = new byte[1024];
                int offset = -1;
                while ((offset = ginzip.read(buffer)) != -1) {
                    out.write(buffer, 0, offset);
                }
                decompressedString = out.toString();
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                if (null != ginzip) {
                    ginzip.close();
                }
                if (null != in) {
                    in.close();
                }
            }
        }
        return decompressedString;
    }
}
