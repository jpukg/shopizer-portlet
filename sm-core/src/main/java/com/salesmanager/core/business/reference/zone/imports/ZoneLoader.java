package com.salesmanager.core.business.reference.zone.imports;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZoneLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZoneLoader.class);
	
	 public Map<String, List<ZoneTransient>> loadZoneConfigurations() {
		
		ObjectMapper mapper = new ObjectMapper();
		InputStream in = this.getClass().getResourceAsStream("/reference/zoneconfig.json");
		Map<String,List<ZoneTransient>> zoneConfigurations = null ;
		try {
		        zoneConfigurations = mapper.readValue(in, new TypeReference<Map<String,List<ZoneTransient>>>() { });
		} catch (Exception e) {
			LOGGER.error("Error import zones.", e);
		}
		
		return zoneConfigurations;
	 }
}
