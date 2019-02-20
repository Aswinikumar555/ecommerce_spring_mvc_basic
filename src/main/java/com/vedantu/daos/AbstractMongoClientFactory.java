package com.vedantu.daos;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.vedantu.utils.LogFactory;
import javax.annotation.PreDestroy;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractMongoClientFactory {

    @Autowired
    private LogFactory logFactory;

    private final Logger logger = logFactory.getLogger(AbstractMongoClientFactory.class);

    private MongoOperations mongoOperations = null;
    private MongoClient mongoClient = null;
//
//    private String hosts;
//    private String port;
//    private boolean useAuthentication;
//    private String mongoUsername;
//    private String mongoPassword;
//    private String mongoDBName;
//    private int connectionsPerHost;

    public AbstractMongoClientFactory() {
        super();
    }

    public final MongoOperations getMongoOperations() {
        return mongoOperations;
    }

    protected final void initMongoOperations(String hosts, String port, boolean useAuthentication,
            String mongoUsername, String mongoPassword, String mongoDBName, int connectionsPerHost) {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        MongoClientOptions options = builder.connectionsPerHost(connectionsPerHost).build();
        try {

            List<ServerAddress> seeds = new ArrayList<>();

            if (hosts != null && !hosts.isEmpty() && port != null && !port.isEmpty()) {
                String[] hostArray = hosts.split(",");
                for (String host : hostArray) {
                    seeds.add(new ServerAddress(host, Integer.parseInt(port)));
                }
            }

            if (useAuthentication) {
                List<MongoCredential> credentials = new ArrayList<>();
                credentials.add(MongoCredential.createScramSha1Credential(mongoUsername,
                        mongoDBName,
                        mongoPassword.toCharArray()));
                mongoClient = new MongoClient(seeds, credentials, options);
            } else {
                mongoClient = new MongoClient(seeds, options);
            }
            MongoTemplate mongoTemp = new MongoTemplate(mongoClient, mongoDBName);
         //   mongoTemp.setReadPreference(ReadPreference.secondaryPreferred());
            this.mongoOperations = (MongoOperations) mongoTemp;
            logger.info("MongoClientFactory is initialized");
        } catch (Exception e) {
            logger.error("Error connecting to mongo", e);
        }
    }

    @PreDestroy
    public void cleanUp() {
        try {
            if(mongoClient!=null){
                mongoClient.close();
            }
        } catch (Exception e) {
            logger.error("Error in closing mongo connection ", e);
        }
    }
}
