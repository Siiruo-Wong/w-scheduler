package com.siiruo.wscheduler.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by siiruo wong on 2020/1/20.
 */
public final class CompressUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(CompressUtil.class);
    private static final String CHAREST_NAME="UTF-8";
    private CompressUtil(){}

    public static String compress(String value) {
        if (StringUtil.isEmpty(value)) {
            return value;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(value.getBytes(CHAREST_NAME));
        } catch (IOException e) {

        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {

                }
            }
        }
        return new sun.misc.BASE64Encoder().encode(out.toByteArray());
    }


    public static String decompress(String value) {
        if (StringUtil.isBlank(value)) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = null;
        GZIPInputStream g_input = null;

        String result = null;
        try {
            input = new ByteArrayInputStream(new sun.misc.BASE64Decoder().decodeBuffer(value));
            g_input = new GZIPInputStream(input);
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = g_input.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            result = out.toString(CHAREST_NAME);
        } catch (IOException e) {

        } finally {
            if (g_input != null) {
                try {
                    g_input.close();
                } catch (IOException e) {
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

}
