package com.vedantu.models;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class AbstractSqlLongIdEntity extends AbstractSqlEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;


    public AbstractSqlLongIdEntity(Long id, Long creationTime, String createdBy, Long lastUpdatedTime, String lastUpdatedby,
                             Boolean enabled, String callingUserId) {
        super(creationTime, createdBy, lastUpdatedTime, lastUpdatedby,
                enabled, callingUserId);
        this.id = id;
    }

    public AbstractSqlLongIdEntity() {
        super();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setEntityDefaultProperties(String callingUserId) {
        if (this.getId() == null) {
            this.setCreationTime(System.currentTimeMillis());
            this.setCreatedBy(callingUserId);
        }
        this.setLastUpdatedTime(System.currentTimeMillis());
        this.setLastUpdatedby(callingUserId);
    }
}