package com.salesmanager.web.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.web.admin.entity.web.Menu;


public class AdminFilter extends HandlerInterceptorAdapter {
	

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminFilter.class);
	
	public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
		
		CacheUtils cache = CacheUtils.getInstance();
		//Map<String,Menu> menus = (Map) request.getSession().getAttribute("MENUMAP");
		@SuppressWarnings("unchecked")
		Map<String,Menu> menus = (Map) cache.getFromCache("MENUMAP");
		

		

		if(menus==null) {
			
			InputStream in = null;
		
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			try {
				
				in =
					(InputStream) this.getClass().getClassLoader().getResourceAsStream("admin/menu.json");
				
				
				Map<String,Object> data = mapper.readValue(in, Map.class);
				
				
				Menu currentMenu = null;
				
				menus = new LinkedHashMap<String,Menu>();
				List objects = (List)data.get("menus");
				for(Object object : objects) {
					
					Menu m = getMenu(object);
					menus.put(m.getCode(),m);
					
				}
				
				
				cache.putInCache(menus,"MENUMAP");
				//request.getSession().setAttribute("MENUMAP",menus);
				

			

			} catch (JsonParseException e) {
				LOGGER.error("Error while creating menu", e);
			} catch (JsonMappingException e) {
				LOGGER.error("Error while creating menu", e);
			} catch (IOException e) {
				LOGGER.error("Error while creating menu", e);
			} finally {
				if(in !=null) {
					try {
						in.close();
					} catch (Exception ignore) {
						// TODO: handle exception
					}
				}
			}
		
		} 
		
		
		List<Menu> list = new ArrayList<Menu>(menus.values());

		request.setAttribute("MENULIST", list);
		request.setAttribute("MENUMAP", menus);
		
		
		return true;
	}
	
	
	private Menu getMenu(Object object) {
		
		Map o = (Map)object;
		Map menu = (Map)o.get("menu");
		
		Menu m = new Menu();
		m.setCode((String)menu.get("code"));
		
		
		m.setUrl((String)menu.get("url"));
		m.setIcon((String)menu.get("icon"));
		m.setRoles((List)menu.get("roles"));
		
		List menus = (List)menu.get("menus");
		if(menus!=null) {
			for(Object oo : menus) {
				
				Menu mm = getMenu(oo);
				m.getMenus().add(mm);
			}
			
		}
		
		return m;
		
	}

}
