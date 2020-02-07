package com.siiruo.wscheduler.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by siiruo wong on 2020/2/3.
 */
public final class NetUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtil.class);

    private NetUtil(){}

    public static InetAddress getInetAddress() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
            InetAddress address = validAddress(inetAddress);
            if (address != null) {
                return address;
            }
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (null == interfaces) {
                return inetAddress;
            }
            for (;interfaces.hasMoreElements();){
                try {
                    NetworkInterface network = interfaces.nextElement();
                    if (network.isVirtual() || network.isLoopback() || !network.isUp()) {
                        continue;
                    }
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    for (;addresses.hasMoreElements();) {
                        try {
                            InetAddress address = validAddress(addresses.nextElement());
                            if (address != null) {
                                try {
                                    if(address.isReachable(500)||isReachable(address)){
                                        return address;
                                    }
                                } catch (IOException e) {
                                    LOGGER.warn(e.getMessage(), e);
                                }
                            }
                        } catch (Throwable e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                } catch (Throwable e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
        return inetAddress;
    }

    private static InetAddress validAddress(InetAddress address) {
        if ((address instanceof Inet6Address)&&isOnIPV6()) {
            return convertIPV6Address((Inet6Address) address);
        }
        if (validIPV4Address(address)) {
            return address;
        }
        return null;
    }

    /**
     * Whether ipv6 is enabled
     * @return
     */
    private static boolean isOnIPV6() {
        return Boolean.getBoolean("java.net.preferIPv6Addresses");
    }

    /**
     * Whether it is valid ipv4
     * @param address
     * @return
     */
    private static boolean validIPV4Address(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }
        String name = address.getHostAddress();
        return name != null && !"0.0.0.0".equals(name) && !"127.0.0.1".equals(name) && !"localhost".equalsIgnoreCase(name);
    }

    /**
     * Convert IPV6 formatted network card to IPV6 formatted scope id
     * @param address Inet6Address
     * @return
     */
    private static InetAddress convertIPV6Address(Inet6Address address) {
        String address$1 = address.getHostAddress();
        int index = address$1.lastIndexOf('%');
        if (index > 0) {
            try {
                return InetAddress.getByName(address$1.substring(0, index) + '%' + address.getScopeId());
            } catch (UnknownHostException e) {
                LOGGER.debug("an exception occurs when converting ipv6 address.", e);
            }
        }
        return address;//original address
    }

    /**
     * Whether the host is reachable
     * see: https://stackoverflow.com/questions/9922543/why-does-inetaddress-isreachable-return-false-when-i-can-ping-the-ip-address
     * @param address
     * @return
     */
    private static boolean isReachable(InetAddress address){
        try {
            boolean isWindows=isWindows();
            if (address instanceof Inet6Address) {
                address=convertIPV6Address((Inet6Address)address);
            }
            Process p1 = Runtime.getRuntime().exec(String.format("ping -%s 1 %s",isWindows?"n":"c",address.getHostAddress()));
            return p1.waitFor()==0;
        } catch (IOException e) {
            LOGGER.debug("an IOException exception occurs when testing reachable.", e);
        } catch (InterruptedException e) {
            LOGGER.debug("an InterruptedException exception occurs when testing reachable.", e);
        }
        return false;
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") > -1;
    }


}
