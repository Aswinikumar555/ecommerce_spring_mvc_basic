package com.vedantu.daos;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.vedantu.utils.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class MongoClientFactory extends AbstractMongoClientFactory {

    @Autowired
    private LogFactory logFactory;

    private final Logger logger = logFactory.getLogger(MongoClientFactory.class);

    private final String hosts = "localhost";
    private final String port = "27017";
    private final boolean useAuthentication = false;
    private final String mongoDBName = "test";
    private final int connectionsPerHost = 50;

    public MongoClientFactory() {
        super();
        initMongoOperations(hosts, port, useAuthentication, null, null, mongoDBName,
                connectionsPerHost);
        logger.info("Mongo connection created successfully, connections created: " + connectionsPerHost);
    }
}
