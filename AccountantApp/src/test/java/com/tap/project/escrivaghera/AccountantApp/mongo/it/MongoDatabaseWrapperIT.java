package com.tap.project.escrivaghera.AccountantApp.mongo.it;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.common.AbstractMongoDatabaseWrapperTest;

public class MongoDatabaseWrapperIT extends AbstractMongoDatabaseWrapperTest {

	@Override
	public MongoClient createMongoClient() throws UnknownHostException {
		MongoClient mongoClient=new MongoClient(/*"192.168.99.100:27017"*/);
		return mongoClient;
	}

}
