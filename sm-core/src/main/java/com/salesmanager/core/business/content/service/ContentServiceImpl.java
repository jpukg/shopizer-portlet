package com.salesmanager.core.business.content.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.salesmanager.core.business.content.dao.ContentDao;
import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.content.model.content.ContentType;
import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.content.model.content.OutputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.modules.cms.content.ContentFileManager;
import com.salesmanager.core.modules.cms.content.StaticContentFileManager;

@Service( "contentService" )
public class ContentServiceImpl
    extends SalesManagerEntityServiceImpl<Long, Content>
    implements ContentService
{

    private static final Logger LOG = LoggerFactory.getLogger( ContentServiceImpl.class );

    private final ContentDao contentDao;

    @Autowired
    ContentFileManager contentFileManager;
    
    @Autowired
    StaticContentFileManager staticContentFileManager;

    @Autowired
    public ContentServiceImpl( final ContentDao contentDao )
    {
        super( contentDao );

        this.contentDao = contentDao;
    }

    @Override
    public List<Content> listByType( final ContentType contentType, final MerchantStore store, final Language language )
        throws ServiceException
    {

        return contentDao.listByType( contentType, store, language );
    }
    
    @Override
    public List<Content> listByType( final ContentType contentType, final MerchantStore store )
    throws ServiceException
    {

    return contentDao.listByType( contentType, store );
    }

    @Override
    public List<Content> listByType( final List<ContentType> contentType, final MerchantStore store, final Language language )
        throws ServiceException
    {

    	List<String> contentTypes = new ArrayList<String>();
    	for(ContentType type : contentType) {
    		contentTypes.add(type.name());
    	}
        return contentDao.listByType( contentTypes, store, language );
    }
    
    @Override
    public List<Content> listByType( final List<ContentType> contentType, final MerchantStore store )
    throws ServiceException
    {

		List<String> contentTypes = new ArrayList<String>();
		for(ContentType type : contentType) {
			contentTypes.add(type.name());
		}
	    return contentDao.listByType( contentTypes, store );
    }

    @Override
    public Content getByCode( final String code, final MerchantStore store )
        throws ServiceException
    {

        return contentDao.getByCode( code, store );

    }

    @Override
    public void saveOrUpdate( final Content content )
        throws ServiceException
    {

        // save or update (persist and attach entities
        if ( content.getId() != null && content.getId() > 0 )
        {
            super.update( content );
        }
        else
        {
            super.save( content );
        }

    }

    @Override
    public Content getByCode( final String code, final MerchantStore store, final Language language )
        throws ServiceException
    {
        return contentDao.getByCode( code, store, language );
    }

    /**
     * Method responsible for adding content file for given merchant store in underlying Infinispan tree
     * cache. It will take  {@link InputContentFile} and will store file for given merchant store according to its type.
     * 
     * @param merchantStoreCode Merchant store
     * @param contentFile {@link InputContentFile} being stored
     * @throws ServiceException service exception
     */
    @Override
    public void addContentFile( final String merchantStoreCode, final InputContentFile contentFile )
        throws ServiceException
    {
        Assert.notNull( merchantStoreCode, "Merchant store Id can not be null" );
        Assert.notNull( contentFile, "InputContentFile image can not be null" );
        Assert.notNull( contentFile.getFileContentType(), "InputContentFile.fileContentType can not be null" );
        
        if(contentFile.getFileContentType().name().equals(FileContentType.IMAGE.name())
        		|| contentFile.getFileContentType().name().equals(FileContentType.STATIC_FILE.name())) {
        	addFile(merchantStoreCode,contentFile);
        } else {
        	addImage(merchantStoreCode,contentFile);
        }
        
       

    }
    
    @Override
    public void addLogo( final String merchantStoreCode, InputContentFile cmsContentImage )
    throws ServiceException {
    	
    	
		    Assert.notNull( merchantStoreCode, "Merchant store Id can not be null" );
		    Assert.notNull( cmsContentImage, "CMSContent image can not be null" );

		    
		    cmsContentImage.setFileContentType(FileContentType.LOGO);
		    addImage(merchantStoreCode,cmsContentImage);
		   

    }
    
    @Override
    public void addOptionImage( final String merchantStoreCode, InputContentFile cmsContentImage )
    throws ServiceException {
    	
    	
		    Assert.notNull( merchantStoreCode, "Merchant store Id can not be null" );
		    Assert.notNull( cmsContentImage, "CMSContent image can not be null" );
		    cmsContentImage.setFileContentType(FileContentType.PROPERTY);
		    addImage(merchantStoreCode,cmsContentImage);
		   

    }
    
    
    private void addImage(final String merchantStoreCode, InputContentFile contentImage ) throws ServiceException {
    	
    	try
	    {
	        LOG.info( "Adding content image for merchant id {}", merchantStoreCode);
	        contentFileManager.addImage( merchantStoreCode, contentImage );
	        
	    } catch ( Exception e )
		 {
		        LOG.error( "Error while trying to convert input stream to buffered image", e );
		        throw new ServiceException( e );
		
		 } finally {
			 
			 try {
				if(contentImage.getFile()!=null) {
					contentImage.getFile().close();
				}
			} catch (Exception ignore) {}
			 
		 }

    }
    
    private void addFile(final String merchantStoreCode, InputContentFile contentImage ) throws ServiceException {
    	
    	try
	    {
	        LOG.info( "Adding content file for merchant id {}", merchantStoreCode);
	        staticContentFileManager.addStaticFile(merchantStoreCode, contentImage);
	        
	    } catch ( Exception e )
		 {
		        LOG.error( "Error while trying to convert input stream to buffered image", e );
		        throw new ServiceException( e );
		
		 } finally {
			 
			 try {
				if(contentImage.getFile()!=null) {
					contentImage.getFile().close();
				}
			} catch (Exception ignore) {}
		 }

    }

    

    
    






    

    


    /**
     * Method responsible for adding list of content images for given merchant store in underlying Infinispan tree
     * cache. It will take list of {@link CMSContentImage} and will store them for given merchant store.
     * 
     * @param merchantStoreCode Merchant store
     * @param contentImagesList list of {@link CMSContentImage} being stored
     * @throws ServiceException service exception
     */
	@Override
	public void addContentFiles(String merchantStoreCode,
			List<InputContentFile> contentFilesList) throws ServiceException {
		
        Assert.notNull( merchantStoreCode, "Merchant store ID can not be null" );
        Assert.notEmpty( contentFilesList, "File list can not be empty" );
        LOG.info( "Adding total {} images for given merchant",contentFilesList.size() );
		
        LOG.info( "Adding content images for merchant...." );
        //contentFileManager.addImages( merchantStoreCode, contentImagesList );
        staticContentFileManager.addStaticFiles(merchantStoreCode, contentFilesList);
        
        try {
			for(InputContentFile file : contentFilesList) {
				if(file.getFile()!=null) {
					file.getFile().close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
    /**
     * Method to remove given content image.Images are stored in underlying system based on there name.
     * Name will be used to search given image for removal
     * @param contentImage
     * @param merchantStoreCode merchant store
     * @throws ServiceException
     */
	@Override
	public void removeFile(String merchantStoreCode,
			FileContentType fileContentType, String fileName)
			throws ServiceException {
        Assert.notNull( merchantStoreCode, "Merchant Store Id can not be null" );
        Assert.notNull( fileContentType, "Content file type can not be null" );
        Assert.notNull( fileName, "Content Image type can not be null" );
        
        
        //check where to remove the file
        if(fileContentType.name().equals(FileContentType.IMAGE.name())
        		|| fileContentType.name().equals(FileContentType.STATIC_FILE.name())) {
        	staticContentFileManager.removeStaticContent(merchantStoreCode, fileContentType, fileName);
        } else {
        	contentFileManager.removeImage( merchantStoreCode, fileContentType, fileName );
        }
		
	}

    /**
     * Method to remove all images for a given merchant.It will take merchant store as an input and will
     * remove all images associated with given merchant store.
     * 
     * @param merchantStoreCode
     * @throws ServiceException
     */
	@Override
	public void removeFiles(String merchantStoreCode) throws ServiceException {
        Assert.notNull( merchantStoreCode, "Merchant Store Id can not be null" );
        

        
        contentFileManager.removeImages( merchantStoreCode );
        staticContentFileManager.removeStaticContents(merchantStoreCode);
		
	}

	
    /**
     * Implementation for getContentImage method defined in {@link ContentService} interface. Methods will return
     * Content image with given image name for the Merchant store or will return null if no image with given name found
     * for requested Merchant Store in Infinispan tree cache.
     * 
     * @param store Merchant merchantStoreCode
     * @param imageName name of requested image
     * @return {@link OutputContentImage}
     * @throws ServiceException
     */
	@Override
	public OutputContentFile getContentFile(String merchantStoreCode,
			FileContentType fileContentType, String fileName)
			throws ServiceException {
        Assert.notNull( merchantStoreCode, "Merchant store ID can not be null" );
        Assert.notNull( fileName, "File name can not be null" );
        
        if(fileContentType.name().equals(FileContentType.IMAGE.name())
        		|| fileContentType.name().equals(FileContentType.STATIC_FILE.name())) {
        	return staticContentFileManager.getStaticContentData(merchantStoreCode, fileContentType, fileName);
        	
        } else {
        	return contentFileManager.getImage( merchantStoreCode, fileName, fileContentType );
        }
        

	}

    /**
     * Implementation for getContentImages method defined in {@link ContentService} interface. Methods will return list of all
     * Content image associated with given  Merchant store or will return empty list if no image is associated with
     * given Merchant Store in Infinispan tree cache.
     * 
     * @param merchantStoreId Merchant store
     * @return list of {@link OutputContentImage}
     * @throws ServiceException
     */
	@Override
	public List<OutputContentFile> getContentFiles(String merchantStoreCode,
			FileContentType fileContentType) throws ServiceException {
        Assert.notNull( merchantStoreCode, "Merchant store Id can not be null" );
        return staticContentFileManager.getStaticContentData(merchantStoreCode, fileContentType);
	}

    /**
     * Returns the image names for a given merchant and store
     * @param merchantStoreCode
     * @param imageContentType
     * @return images name list
     * @throws ServiceException
     */
	@Override
	public List<String> getContentFilesNames(String merchantStoreCode,
			FileContentType fileContentType) throws ServiceException {
        Assert.notNull( merchantStoreCode, "Merchant store Id can not be null" );
        
        if(fileContentType.name().equals(FileContentType.IMAGE.name())
        		|| fileContentType.name().equals(FileContentType.STATIC_FILE.name())) {
        	return staticContentFileManager.getStaticContentDataName(merchantStoreCode, fileContentType);
        } else {
        	return contentFileManager.getImageNames(merchantStoreCode, fileContentType);
        }
	}

    

}
