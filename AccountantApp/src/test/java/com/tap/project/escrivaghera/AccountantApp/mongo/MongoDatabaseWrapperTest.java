package com.tap.project.escrivaghera.AccountantApp.mongo;

import java.net.UnknownHostException;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.tap.project.escrivaghera.AccountantApp.common.AbstractMongoDatabaseWrapperTest;

public class MongoDatabaseWrapperTest extends AbstractMongoDatabaseWrapperTest {

	@Override
	public MongoClient createMongoClient() throws UnknownHostException {
		Fongo fongo = new Fongo("Server");
		MongoClient mongoClient = fongo.getMongo();
		return mongoClient;
	}
}
