/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vedantu.models;

import com.vedantu.enums.EntityState;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author ajith
 */
public abstract class AbstractMongoEntity {

    private Long creationTime;
    private String createdBy;
    private Long lastUpdated;
    private String lastUpdatedBy; // For tracking last updated by
    private EntityState entityState = EntityState.ACTIVE;

    public AbstractMongoEntity() {
        super();
    }

    public AbstractMongoEntity(Long creationTime, String createdBy, Long lastUpdated, String lastUpdatedBy) {
        this(creationTime, createdBy, lastUpdated, lastUpdatedBy, EntityState.ACTIVE);
    }

    public AbstractMongoEntity(Long creationTime, String createdBy, Long lastUpdated,
            String lastUpdatedBy, EntityState entityState) {
        this.creationTime = creationTime;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.entityState = entityState;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public EntityState getEntityState() {
        return entityState;
    }

    public void setEntityState(EntityState entityState) {
        this.entityState = entityState;
    }

    public abstract void setEntityDefaultProperties(String callingUserId);

    public static class Constants {

        public static final String ID = "id";
        public static final String _ID = "_id";
        public static final String CREATION_TIME = "creationTime";
        public static final String LAST_UPDATED = "lastUpdated";
        public static final String LAST_UPDATED_BY = "lastUpdatedBy";
        public static final String CREATED_BY = "createdBy";
        public static final String ENTITY_STATE = "entityState";
    }

    //reference http://howtodoinjava.com/apache-commons/how-to-override-tostring-effectively-with-tostringbuilder/
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
