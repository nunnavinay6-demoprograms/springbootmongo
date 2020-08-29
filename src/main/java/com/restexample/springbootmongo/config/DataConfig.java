package com.restexample.springbootmongo.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.restexample.springbootmongo.model.EmpData;

@Configuration
public class DataConfig {

	private List<EmpData> dataList = new ArrayList<>();

	@Autowired
	MongoTemplate mongoTemplate;

	@PostConstruct
	private void initData() {

		EmpData data = new EmpData();
		data.set_id("1111");
		data.setMailId("test1@test.com");
		data.setName("testUser1");
		data.setRole("TestRole1");

		EmpData data1 = new EmpData();
		data1.set_id("2222");
		data1.setMailId("test2@test.com");
		data1.setName("testUser2");
		data1.setRole("TestRole2");

		dataList.add(data);
		dataList.add(data1);
	}

	public List<EmpData> getDataList() {
		return Collections.unmodifiableList(dataList);
	}

	@PostConstruct
	public void insertDataToMonogo() {

		EmpData data = new EmpData();
		data.set_id("1111");
		data.setMailId("test1@test.com");
		data.setName("testUser1");
		data.setRole("TestRole1");

		mongoTemplate.createCollection(EmpData.class);

		mongoTemplate.save(data, "EmpData");
	}

}
