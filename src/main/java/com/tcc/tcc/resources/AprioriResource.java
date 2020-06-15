package com.tcc.tcc.resources;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToNominal;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.tcc.tcc.dto.AttributesDTO;
import com.tcc.tcc.dto.ResultDTO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/data")
public class AprioriResource {
	
	@RequestMapping(value= "/{collection}", method = RequestMethod.GET)
	public ResponseEntity<List<AttributesDTO>> insert(@PathVariable String collection) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("aia-social");
		MongoCollection<Document> mongoCollection = db.getCollection(collection);
		
		Document doc = mongoCollection.find().limit(1).first();
		
		List<AttributesDTO> listDto = new ArrayList<AttributesDTO>();
		
		if(doc != null) {
			int id = 0;
			for(String key : doc.keySet()) {
				listDto.add(new AttributesDTO(id, true, key, ""));	
				id ++;
			}	
		}			
		
		return ResponseEntity.ok().body(listDto);	
	}
	
	@RequestMapping(value="/process", method = RequestMethod.POST)
	public ResponseEntity<ResultDTO> attributes(@RequestBody List<AttributesDTO> attributes) {
		
		String fields = "";
		String filters = "";
		
		for(AttributesDTO dto : attributes) {
			if(dto.isUse()) {
				fields += "," + dto.getName();
			}
			
			if(dto.getCondition() != "") {
				filters += "," + dto.getName() + ":\"" + dto.getCondition() + "\"";
			}
		}
		
		fields = fields.replaceFirst("," , "");
		if(filters.length() > 0) {
			filters = "{" + filters.replaceFirst("," , "") + "}";
		}
		
		return ResponseEntity.ok().body(new ResultDTO(fields, filters));
	}
	
	@RequestMapping(value="/apriori/{filename}", method = RequestMethod.GET)
	public ResponseEntity<String> process(@PathVariable String filename) throws Exception {
		
		File file = new File("/home/lucas/Documentos/bases_dados/processed/" + filename);
		
		CSVLoader loader = new CSVLoader();
		loader.setSource(file);
		Instances data = loader.getDataSet();
		 
		NumericToNominal convertNumeric = new NumericToNominal();
		
		convertNumeric.setInputFormat(data);     
        Instances processedData = Filter.useFilter(data, convertNumeric);
		
        Apriori apriori = new Apriori();
        apriori.setClassIndex(processedData.classIndex());
        apriori.buildAssociations(processedData);
        
        String result = String.valueOf(apriori);
        
        file.delete();
		
		return ResponseEntity.ok().body(result);
	}
}
