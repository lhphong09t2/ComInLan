package com.onballgroup.cominlan;

/**
 * Created by Phong on 4/26/2016.
 */
public class CConstant {
    public static final String DomainId = "ComInLan";

    public static final  int[] UdpListenerPort = {55176, 23435, 34523, 45349};
    public static final int UdpPacketSizeInByte = 4096;

    public static final int AdvertisingPeriod = 5000;
    public static final int ClientRefreshPeriod = 5000;

    public static final int ServerCleanupPeriod = AdvertisingPeriod + 1000;
    public static final int ClientCleanupPeriod = ClientRefreshPeriod + 1000;
}
