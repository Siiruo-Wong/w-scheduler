package com.siiruo.wscheduler.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by siiruo wong on 2020/1/13.
 */
public final class WSchedulerBannerVersion {
    private static final Logger LOGGER = LoggerFactory.getLogger(WSchedulerBannerVersion.class);
    private static final StringBuilder BANNER=new StringBuilder();
    private static final String W_SCHEDULER = " :: W-Scheduler :: ";
    static {
        BANNER.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\r\n")
                .append("  **** **** ****          ******               ****                       ****             ****                     \r\n")
                .append("  *  * *  * *  *         *     *               *  *                       *  *             *  *                     \r\n")
                .append("  *  * *  * *  *         *    *        ****    *  *****      ****       ***  *  **** ****  *  *     ****      ****  \r\n")
                .append("  *  * *  * *  *  *****    *    *    *      *  *       *   *  **  *   *   *  *  *  * *  *  *  *   *  **  *  *    *  \r\n")
                .append("  *  * *  * *  *  *****     *    *  *   ***    *   *   *  *   ***    *   *   *  *  * *  *  *  *  *   ***    *  *    \r\n")
                .append("   *  *  * *  *          *     *     *      *  *  * *  *   *      *   *     *   *       *  *  *   *      *  *  *    \r\n")
                .append("     * *  * *            ******        ****    **** ****     ****       ***      * * *     ****     ****    ****    \r\n")
                .append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
    }
    private WSchedulerBannerVersion(){}

    public static String getVersion() {
        Package pkg = WSchedulerBannerVersion.class.getPackage();
        return (pkg != null) ? pkg.getImplementationVersion() : null;
    }

    public static void print(){
        String version = getVersion();
        version = (version != null) ? " (v" + version + ")" : "";
        StringBuilder padding = new StringBuilder();
        //BANNER first row length =116
        while (padding.length() < 116 - (version.length() + W_SCHEDULER.length())) {
            padding.append(" ");
        }
        LOGGER.info(String.format("\r\n\r\n%s%s%s%s\r\n",BANNER.toString(),W_SCHEDULER,padding.toString(),version));
    }
}
