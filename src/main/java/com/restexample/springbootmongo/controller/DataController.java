package com.restexample.springbootmongo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.result.DeleteResult;
import com.restexample.springbootmongo.config.DataConfig;
import com.restexample.springbootmongo.model.EmpData;

@RestController
public class DataController {

	public static Logger log = LoggerFactory.getLogger(DataController.class);

	@Autowired
	DataConfig config;

	@Autowired
	MongoTemplate mongoTemplate;

	@GetMapping(value = "/getData", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<EmpData> getData() {
		//log.info("Inside getData");
		System.out.println("inside ");
		return config.getDataList();
	}
	

	@GetMapping(value = "/heathcheck")
	public String getHeathStats()
	{
		return "success";
	}
	
	@GetMapping(value = "/getEmployees", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<EmpData> getEmpData() {
		log.info("Inside getEmpData");
		List<EmpData> data = mongoTemplate.findAll(EmpData.class);
		return data;
	}

	@PostMapping(value = "/addEmp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public EmpData addEmpData(@RequestBody EmpData empData) {
		log.info("Inserting data: {}", empData);
		EmpData data = new EmpData();
		if (empData != null) {
			try {
				data = mongoTemplate.insert(empData, "EmpData");
			} catch (Exception e) {
				data.setMesg(e.getMessage());
			}
		} else {
			data.setMailId("Data is invalid");
		}
		return data;
	}

	@DeleteMapping(value = "/{id}")
	public EmpData delete(@PathVariable String id) {
		log.info("Inside Delete data: {}", id);
		EmpData data = new EmpData();
		if (id != null && !id.isEmpty()) {
			try {

				Query query = new Query();
				query.addCriteria(Criteria.where("_id").is(id));
				DeleteResult delData = mongoTemplate.remove(query, "EmpData");
				long count = delData.getDeletedCount();
				if (count > 0) {
					data.setMesg("EmpData deleted successFully for Id :" + id);
				} else {
					data.setMesg("No Data found for Id :" + id);
				}
			} catch (Exception e) {
				data.setMesg(e.getMessage());
			}
		} else {
			data.setMesg("Invalid Id");
		}
		return data;
	}

	@GetMapping(value = "/getEmployee/{id}")
	public EmpData getEmployeeData(@PathVariable String id) {
		log.error("Inside Employee data: {}", id);
		EmpData data = new EmpData();
		if (id != null && !id.isEmpty()) {
			try {

				Query query = new Query();
				query.addCriteria(Criteria.where("_id").is(id));
				EmpData empData = mongoTemplate.findOne(query, EmpData.class, "EmpData");
				if (empData != null) {
					return empData;
				} else {
					data.setMesg("No Data found for Id :" + id);
				}
			} catch (Exception e) {
				data.setMesg(e.getMessage());
			}
		} else {
			data.setMesg("Invalid Id");
		}
		return data;
	}
}
