package com.salesmanager.web.files;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.generic.exception.ServiceException;

@Controller
public class FilesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FilesController.class);
	

	
	@Autowired
	private ContentService contentService;
	

	/**
	 * Serves static files (css, js ...) the repository is a single node by merchant
	 * @param storeCode
	 * @param imageName
	 * @param extension
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 */
	@RequestMapping("/files/{storeCode}/{fileName}.{extension}")
	public void  downloadFile(@PathVariable final String storeCode, @PathVariable final String fileName, @PathVariable final String extension, HttpServletRequest request, HttpServletResponse response) throws IOException, ServiceException {

		// example -> /static/mystore/CONTENT/myImage.png
		
		ImageContentType imgType = null;
		
		// needs to query the new API
		OutputContentImage image =contentService.getContentImage(storeCode, imgType, new StringBuilder().append(fileName).append(".").append(extension).toString());
		
/*		try {
		      // get your file as InputStream
		      InputStream is = ...;
		      // copy it to response's OutputStream
		      IOUtils.copy(is, response.getOutputStream());
		      response.setContentType("application/pdf");      
		      response.setHeader("Content-Disposition", "attachment; filename=somefile.pdf"); 
		      response.flushBuffer();
		    } catch (IOException ex) {
		      log.info("Error writing file to output stream. Filename was '" + fileName + "'");
		      throw new RuntimeException("IOError writing file to output stream");
		    }

		}*/
		


	}
	
	/**
	 * Serves product download files
	 * @param storeCode
	 * @param productId
	 * @param fileName
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/files/{storeCode}/{orderId}/{productId}/{fileName}.{extension}")
	public void printImage(@PathVariable final String storeCode, @PathVariable final Long orderId, @PathVariable final Long productId, @PathVariable final String fileName, @PathVariable final String extension, HttpServletRequest request, HttpServletResponse response) throws IOException {

		// product image
		// example -> /files/mystore/12345/120/product1.zip
		
		//TODO role customer, verify the order has the requested product to download
		
		//Need to query the files CMS for merchant and product ( not the order )
		
		ImageContentType imgType = null;
		

		
		OutputContentImage image = null;
		//try {
			//image = productImageService.getProductImage(storeCode, productId, new StringBuilder().append(imageName).append(".").append(extension).toString());
		//} catch (ServiceException e) {
			//LOGGER.error("Cannot retrieve image " + imageName, e);
		//}
		/*		try {
	      // get your file as InputStream
	      InputStream is = ...;
	      // copy it to response's OutputStream
	      IOUtils.copy(is, response.getOutputStream());
	      response.setContentType("application/pdf");      
	      response.setHeader("Content-Disposition", "attachment; filename=somefile.pdf"); 
	      response.flushBuffer();
	    } catch (IOException ex) {
	      log.info("Error writing file to output stream. Filename was '" + fileName + "'");
	      throw new RuntimeException("IOError writing file to output stream");
	    }

	}*/

	}

}