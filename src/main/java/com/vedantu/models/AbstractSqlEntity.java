package com.vedantu.models;

import javax.persistence.MappedSuperclass;
import org.apache.commons.lang.builder.ToStringBuilder;

@MappedSuperclass
public abstract class AbstractSqlEntity {

    public Long creationTime;
    public String createdBy;
    public Long lastUpdatedTime;
    public String lastUpdatedby;
    private Boolean enabled=Boolean.TRUE;
    private String callingUserId;

    public AbstractSqlEntity() {
        super();
    }

    public AbstractSqlEntity(Long creationTime, String createdBy, Long lastUpdatedTime, String lastUpdatedby,
                             Boolean enabled, String callingUserId) {
        super();
        this.creationTime = creationTime;
        this.createdBy = createdBy;
        this.lastUpdatedTime = lastUpdatedTime;
        this.lastUpdatedby = lastUpdatedby;
        if(enabled != null){
        	this.enabled = enabled;
        }        
        this.callingUserId = callingUserId;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getLastUpdatedby() {
        return lastUpdatedby;
    }

    public void setLastUpdatedby(String lastUpdatedby) {
        this.lastUpdatedby = lastUpdatedby;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getCallingUserId() {
        return callingUserId;
    }

    public void setCallingUserId(String callingUserId) {
        this.callingUserId = callingUserId;
    }

    public abstract void setEntityDefaultProperties(String callingUserId);

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class Constants {
        public static final String ID = "id";
        public static final String CREATION_TIME = "creationTime";
        public static final String CREATED_BY = "createdBy";
        public static final String LAST_UPDATED_TIME = "lastUpdatedTime";
        public static final String LAST_UPDATED_BY = "lastUpdatedby";
        public static final String ENABLED = "enabled";
    }

}