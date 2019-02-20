package com.vedantu.requests;

import java.util.ArrayList;
import java.util.List;
import javax.management.relation.Role;

public abstract class AbstractFrontEndReq extends AbstractReq {

    private Long callingUserId;//this will be set by system from jwt token or by if triggered by system will be set as 0, user will not pass this ones
    private Role callingUserRole;//this will be set by system from jwt token, user will not pass this ones
    private String ipAddress;
    private String geoCountry;
    private String userAgent;
    private String appVersionCode;
    private String appDeviceId;

    public AbstractFrontEndReq() {
        super();
    }

    public String getIpAddress() {

        return ipAddress;
    }

    public String getGeoCountry() {
        return geoCountry;
    }

    public Long getCallingUserId() {

        return callingUserId;
    }

    public void setCallingUserId(Long callingUserId) {

        this.callingUserId = callingUserId;
    }

    public Role getCallingUserRole() {
        return callingUserRole;
    }

    public void setCallingUserRole(Role callingUserRole) {
        this.callingUserRole = callingUserRole;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setGeoCountry(String geoCountry) {
        this.geoCountry = geoCountry;
    }


    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppDeviceId() {
        return appDeviceId;
    }

    public void setAppDeviceId(String appDeviceId) {
        this.appDeviceId = appDeviceId;
    }

    @Override
    protected List<String> collectVerificationErrors() {
        return new ArrayList<>();
    }

    public static class Constants {

        public static final String CALLING_USER_ID = "callingUserId";
        public static final String CALLING_USER_ROLE = "callingUserRole";
        public static final String REQUEST_SOURCE = "requestSource";
        public static final String GEO_COUNTRY = "geoCountry";
        public static final String IP_ADDRESS = "ipAddress";
        public static final String USER_ID = "userId";
        public static final Long CALLING_USER_ID_SYSTEM = 0l;
        public static final String CALLING_USER_ID_SYSTEM_DB = "SYSTEM";
    }

}
