package com.vedantu.models;

import com.vedantu.enums.EntityState;
import org.springframework.data.annotation.Id;
import org.springframework.util.StringUtils;

public abstract class AbstractMongoStringIdEntity extends AbstractMongoEntity {

    @Id
    private String id;

    public AbstractMongoStringIdEntity() {
        super();
    }

    public AbstractMongoStringIdEntity(Long creationTime, String createdBy, Long lastUpdated, String lastUpdatedBy) {
        super(creationTime, createdBy, lastUpdated, lastUpdatedBy);
    }

    public AbstractMongoStringIdEntity(Long creationTime, String createdBy, Long lastUpdated, String lastUpdatedBy, EntityState entityState) {
        super(creationTime, createdBy, lastUpdated, lastUpdatedBy, entityState);
    }

    
    public AbstractMongoStringIdEntity(String id, Long creationTime,
            String createdBy, Long lastUpdated, String lastUpdatedBy) {
        super(creationTime, createdBy, lastUpdated, lastUpdatedBy);
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setEntityDefaultProperties(String callingUserId) {
        if (StringUtils.isEmpty(this.getId())) {
            this.setCreationTime(System.currentTimeMillis());
            this.setCreatedBy(callingUserId);
        }
        this.setLastUpdated(System.currentTimeMillis());
        this.setLastUpdatedBy(callingUserId);
    }

}
