package com.salesmanager.test.isolated;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.image.ProductImageDescription;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionValueService;
import com.salesmanager.core.business.catalog.product.service.availability.ProductAvailabilityService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.util.EntityManagerUtils;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.init.service.InitializationDatabase;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.service.ModuleConfigurationService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.modules.cms.common.CMSContentImage;
import com.salesmanager.test.core.SalesManagerCoreTestExecutionListener;

@ContextConfiguration( locations = { "classpath:spring/test-spring-context.xml" } )
@RunWith( SpringJUnit4ClassRunner.class )
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, SalesManagerCoreTestExecutionListener.class } )
public class IsolatedTestCase
{

    private static final Logger log = Logger.getLogger( IsolatedTestCase.class );

    private static final Date date = new Date( System.currentTimeMillis() );

    @Autowired
    private EntityManagerUtils entityManagerUtils;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected ProductPriceService productPriceService;

    @Autowired
    protected ProductAttributeService productAttributeService;

    @Autowired
    protected ProductOptionService productOptionService;

    @Autowired
    protected ProductOptionValueService productOptionValueService;

    @Autowired
    protected ProductAvailabilityService productAvailabilityService;

    @Autowired
    protected ProductImageService productImageService;

    @Autowired
    protected ContentService contentService;

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected MerchantStoreService merchantService;

    @Autowired
    protected ProductTypeService productTypeService;

    @Autowired
    protected LanguageService languageService;

    @Autowired
    protected CountryService countryService;

    @Autowired
    protected ZoneService zoneService;

    @Autowired
    protected CustomerService customerService;

    @Autowired
    protected ManufacturerService manufacturerService;

    @Autowired
    protected CurrencyService currencyService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected GroupService groupService;

    @Autowired
    protected PermissionService permissionService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected InitializationDatabase initializationDatabase;

    @Autowired
    protected ModuleConfigurationService moduleConfigurationService;
    


    // @Autowired
    protected TestSupportFactory testSupportFactory;

    @Test
    public void test1CreateReferences()
        throws ServiceException
    {

        initializationDatabase.populate( "TEST" );

        /*
         * Date date = new Date(System.currentTimeMillis()); Language en = new Language(); en.setCode("en");
         * languageService.create(en); Language fr = new Language(); fr.setCode("fr"); languageService.create(fr); //
         * create country Country ca = new Country(); ca.setIsoCode("CA"); CountryDescription caden = new
         * CountryDescription(); caden.setCountry(ca); caden.setLanguage(en); caden.setName("Canada");
         * caden.setDescription("Canada Country"); CountryDescription cadfr = new CountryDescription();
         * cadfr.setCountry(ca); cadfr.setLanguage(fr); cadfr.setName("Canada"); cadfr.setDescription("Pays Canada");
         * List<CountryDescription> descriptionsca = new ArrayList<CountryDescription>(); descriptionsca.add(caden);
         * descriptionsca.add(cadfr); ca.setDescriptions(descriptionsca); countryService.create(ca); // create a
         * currency Currency currency = new Currency();
         * currency.setCurrency(java.util.Currency.getInstance(Locale.CANADA)); currency.setSupported(true);
         * currencyService.create(currency); // create a merchant MerchantStore store = new MerchantStore();
         * store.setCountry(ca); store.setCurrency(currency); store.setDefaultLanguage(en);
         * store.setInBusinessSince(date); store.setStorename("default store"); store.setStorephone("888-888-8888");
         * store.setCode(MerchantStore.DEFAULT_STORE); store.setStorecity("My city");
         * store.setStorepostalcode("H2H-2H2"); store.setStoreEmailAddress("test@test.com");
         * merchantService.create(store); ProductType generalType = new ProductType();
         * generalType.setCode(ProductType.GENERAL_TYPE); productTypeService.create(generalType);
         */

    }

    @Test
    public void testSearchProduct()
        throws ServiceException
    {

        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        final Language en = languageService.getByCode( "en" );

        final ProductCriteria criteria = new ProductCriteria();
        criteria.setStartIndex( 0 );
        criteria.setMaxCount( 75 );

        final Set<Long> categoryIds = new HashSet<Long>();
        categoryIds.add( 1L );
        categoryIds.add( 2L );
        categoryIds.add( 3L );
        categoryIds.add( 5L );
        categoryIds.add( 4L );

        criteria.setAvailable( new Boolean( false ) );

        criteria.setCategoryIds( categoryIds );

        final ProductList l = productService.listByStore( store, en, criteria );

        System.out.println( "done" );

    }

    @Test
    public void testGetMerchant()
        throws ServiceException
    {

        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        System.out.println( "done" );

    }

    @Test
    public void testCreateProductWithImage()
        throws ServiceException, IOException
    {

        final Language en = languageService.getByCode( "en" );
        final Language fr = languageService.getByCode( "fr" );
        final Country ca = countryService.getByCode( "CA" );
        final Currency currency = currencyService.getByCode( "CAD" );
        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        final ProductType generalType = productTypeService.getProductType( ProductType.GENERAL_TYPE );

        final Category book = categoryService.getByCode( store, "book" );
        final Product product = productService.getById( 1L );

        /*
         * product.setMerchantSore(store); try { EmbeddedCacheManager manager = new
         * DefaultCacheManager("cms/infinispan_configuration.xml");
         * //manager.getDefaultCacheConfiguration().invocationBatching().enabled(); Cache defaultCache =
         * manager.getCache("DataRepository"); defaultCache.getCacheConfiguration().invocationBatching().enabled();
         * TreeCacheFactory f = new TreeCacheFactory(); TreeCache treeCache = f.createTreeCache(defaultCache); Fqn
         * johnFqn = Fqn.fromString("persons/john"); Node<String, Object> john = treeCache.getRoot().getChild(johnFqn);
         * if(john==null) { treeCache.getRoot().addChild(johnFqn); } byte[] bytes2 = (byte[])
         * john.get("JAP-LETTER.jpg"); if(bytes2==null) { //john.put("surname", "Smith"); File file1 = new
         * File("/Users/csamson777/Documents/csti/JAP-LETTER.jpg"); if (!file1.exists()|| !file1.canRead()) { throw new
         * ServiceException("Can't read" + file1.getAbsolutePath()); } InputStream input = new BufferedInputStream(new
         * FileInputStream(file1)); ByteArrayOutputStream out = new ByteArrayOutputStream(); IOUtils.copy(input, out);
         * //input. byte[] bytes = out.toByteArray(); john.put("JAP-LETTER.jpg", bytes); bytes2 = (byte[])
         * john.get("JAP-LETTER.jpg"); ByteArrayInputStream bis = new ByteArrayInputStream(bytes2);
         * ByteArrayOutputStream bos = new ByteArrayOutputStream(); IOUtils.copy(bis, bos); } InputStream in = new
         * ByteArrayInputStream(bytes2); BufferedImage bImageFromConvert = ImageIO.read(in);
         * ImageIO.write(bImageFromConvert, "jpg", new File( "/Users/csamson777/Documents/csti/JAP-LETTER-2.jpg"));
         * System.out.println("done"); Fqn personsFqn = Fqn.fromString("persons"); Fqn johnFqn =
         * Fqn.fromRelative(personsFqn, Fqn.fromString("john")); Node<String, Object> john =
         * treeCache.getRoot().addChild(johnFqn); john.put("surname", "Smith"); Node<String, Object> john = ... Node
         * persons = john.getParent(); // Set<Node<String, Object>> personsChildren = persons.getChildren(); } catch
         * (Exception e) { e.printStackTrace(); }
         */

        final File file1 = new File( "/Users/csamson777/Documents/csti/cocoacart/ktm.png" );
        if ( !file1.exists() || !file1.canRead() )
        {
            throw new ServiceException( "Can't read" + file1.getAbsolutePath() );
        }

        // FileInputStream is1 = new FileInputStream(file1);
        // FileInputStream is2 = new FileInputStream(file2);

        final byte[] is = IOUtils.toByteArray( new FileInputStream( file1 ) );
        final ByteArrayInputStream is1 = new ByteArrayInputStream( is );

        final ProductImage productImage1 = new ProductImage();
        productImage1.setDefaultImage( true );
        productImage1.setProductImage( file1.getName() );

        final ProductImageDescription desc1 = new ProductImageDescription();
        desc1.setLanguage( en );
        desc1.setAltTag( "ALT IMAGE 1 en" );
        desc1.setName( "A beautifill Thing" );
        desc1.setProductImage( productImage1 );

        final ProductImageDescription desc2 = new ProductImageDescription();
        desc2.setLanguage( fr );
        desc2.setAltTag( "ALT IMAGE 1 fr" );
        desc2.setName( "Superbe chose" );
        desc2.setProductImage( productImage1 );

        final List image1descriptions = new ArrayList();
        image1descriptions.add( desc1 );
        image1descriptions.add( desc2 );

        productImage1.setDescriptions( image1descriptions );
        productImage1.setImage( is1 );

        productImageService.addProductImage( product, productImage1 );

    }

    @Test
    public void createProduct()
        throws Exception
    {

        final Language en = languageService.getByCode( "en" );
        final Language fr = languageService.getByCode( "fr" );
        final Country ca = countryService.getByCode( "CA" );
        final Currency currency = currencyService.getByCode( "CAD" );
        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        final ProductType generalType = productTypeService.getProductType( ProductType.GENERAL_TYPE );

        // Category book = categoryService.getByCode(store, "book");

        // Manufacturer manufacturer = manufacturerService.getById(1L);

        // PRODUCT 1

        final Product product = new Product();
        product.setProductHeight( new BigDecimal( 4 ) );
        product.setProductLength( new BigDecimal( 3 ) );
        product.setProductWidth( new BigDecimal( 1 ) );
        product.setSku( "XYZTEST" );
        // product.setManufacturer(manufacturer);
        product.setType( generalType );
        product.setMerchantSore( store );

        // Product description
        final ProductDescription description = new ProductDescription();
        description.setName( "Test with image" );
        description.setLanguage( en );
        description.setProduct( product );

        product.getDescriptions().add( description );
        // product.getCategories().add(book);

        final Set<ProductAvailability> availabilities = new HashSet<ProductAvailability>();

        final ProductPrice dprice = new ProductPrice();
        dprice.setDefaultPrice( true );
        dprice.setProductPriceAmount( new BigDecimal( 29.99 ) );

        final Set<ProductPrice> prices = new HashSet<ProductPrice>();
        prices.add( dprice );

        // Availability
        final ProductAvailability availability = new ProductAvailability();
        availability.setProductDateAvailable( date );
        availability.setProductQuantity( 100 );
        availability.setRegion( "*" );

        availability.setPrices( prices );

        availabilities.add( availability );

        product.setAvailabilities( availabilities );

        final File file1 = new File( "/Users/csamson777/Documents/csti/cocoacart/ktm.png" );
        if ( !file1.exists() || !file1.canRead() )
        {
            throw new ServiceException( "Can't read" + file1.getAbsolutePath() );
        }

        // FileInputStream is1 = new FileInputStream(file1);

        final byte[] is = IOUtils.toByteArray( new FileInputStream( file1 ) );
        final ByteArrayInputStream is1 = new ByteArrayInputStream( is );

        final ProductImage productImage = new ProductImage();
        productImage.setDefaultImage( true );
        productImage.setProductImage( file1.getName() );

        final ProductImageDescription desc1 = new ProductImageDescription();
        desc1.setLanguage( en );
        desc1.setAltTag( "ALT IMAGE 1 en" );
        desc1.setName( "Product image" );

        final ProductImageDescription desc2 = new ProductImageDescription();
        desc2.setLanguage( fr );
        desc2.setAltTag( "ALT IMAGE 1 fr" );
        desc2.setName( "Image du produit" );

        final List<ProductImageDescription> imagedescriptions = new ArrayList<ProductImageDescription>();
        imagedescriptions.add( desc1 );
        imagedescriptions.add( desc2 );

        productImage.setDescriptions( imagedescriptions );
        productImage.setImage( is1 );

        product.getImages().add( productImage );

        productService.saveOrUpdate( product );

    }

    @Test
    public void createContentImage()
        throws ServiceException, FileNotFoundException, IOException
    {

        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        final File file1 = new File( "/Umesh/contentimage/test_image1.jpg" );

        if ( !file1.exists() || !file1.canRead() )
        {
            throw new ServiceException( "Can't read" + file1.getAbsolutePath() );
        }

        final byte[] is = IOUtils.toByteArray( new FileInputStream( file1 ) );
        final ByteArrayInputStream inputStream = new ByteArrayInputStream( is );
        final CMSContentImage cmsContentImage = new CMSContentImage();
        cmsContentImage.setImageName( "demoCmsImage" );
        cmsContentImage.setFile( inputStream );
        contentService.addContentImage( store, cmsContentImage );
        
        final CMSContentImage cmsContentImage1 = new CMSContentImage();
        cmsContentImage1.setImageName( "demoCmsImage2" );
        cmsContentImage1.setFile( inputStream );
        contentService.addContentImage( store, cmsContentImage1 );

    }
    
    @Test
    public void createContentImages()
        throws ServiceException, FileNotFoundException, IOException
    {

        final List<CMSContentImage> contentImagesList=new ArrayList<CMSContentImage>();
        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        final File file1 = new File( "/Umesh/contentimage/destination.png" );

        if ( !file1.exists() || !file1.canRead() )
        {
            throw new ServiceException( "Can't read" + file1.getAbsolutePath() );
        }

        final byte[] is = IOUtils.toByteArray( new FileInputStream( file1 ) );
        final ByteArrayInputStream inputStream = new ByteArrayInputStream( is );
        final CMSContentImage cmsContentImage = new CMSContentImage();
        cmsContentImage.setImageName( "demoCmsImage3" );
        cmsContentImage.setFile( inputStream );
        contentImagesList.add( cmsContentImage);
        
        final CMSContentImage cmsContentImage1 = new CMSContentImage();
        cmsContentImage1.setImageName( "demoCmsImage4" );
        cmsContentImage1.setFile( inputStream );
        
        contentImagesList.add( cmsContentImage1);
        
        contentService.addContentImages( store, contentImagesList );

    }

    @Test
    public void getContentImage()
        throws ServiceException, FileNotFoundException, IOException
    {

        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        final String imageName = "demoCmsImage";

        final OutputContentImage outputContentImage = contentService.getContentImage( store, imageName );
        //final OutputContentImage outputContentImage = contentService.getContentImage( store, "" );
        System.out.println( outputContentImage.getImage() );
        System.out.println( outputContentImage.getImageName() );

    }
    
    @Test
    public void getAllContentImages() throws ServiceException{
        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        final List<OutputContentImage> contentImagesList= contentService.getContentImages( store, null );
        if(CollectionUtils.isNotEmpty( contentImagesList )){
            System.out.println("Total " + contentImagesList.size()+ " Images found");
           for(final OutputContentImage outputContentImage :contentImagesList){
               System.out.println(outputContentImage.getImageName());
           }
        }
        else{
            System.out.println("No image found for given merchant store");
        }
    }
    
    @Test
    public void removeAllContentImages() throws ServiceException{
        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        contentService.removeImages( store );
    }
    

    @Test
    public void testGetImages()
        throws ServiceException
    {

        final Product product = productService.getById( 1L );

        final List<OutputContentImage> images = productImageService.getProductImages( product );

        for ( final OutputContentImage image : images )
        {

            System.out.println( image.getImageName() );
            System.out.println( image.getImageContentType() );
        }

    }

    @Test
    public void test2CreateProducts()
        throws ServiceException
    {

        final Language en = languageService.getByCode( "en" );
        final Language fr = languageService.getByCode( "fr" );
        final Country ca = countryService.getByCode( "CA" );
        final Currency currency = currencyService.getByCode( "CAD" );
        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        final ProductType generalType = productTypeService.getProductType( ProductType.GENERAL_TYPE );

        final Category book = new Category();
        book.setMerchantSore( store );
        book.setCode( "book" );

        final CategoryDescription bookEnglishDescription = new CategoryDescription();
        bookEnglishDescription.setName( "Book" );
        bookEnglishDescription.setCategory( book );
        bookEnglishDescription.setLanguage( en );

        final CategoryDescription bookFrenchDescription = new CategoryDescription();
        bookFrenchDescription.setName( "Livre" );
        bookFrenchDescription.setCategory( book );
        bookFrenchDescription.setLanguage( fr );

        final List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();
        descriptions.add( bookEnglishDescription );
        descriptions.add( bookFrenchDescription );

        book.setDescriptions( descriptions );

        categoryService.create( book );

        final Category music = new Category();
        music.setMerchantSore( store );
        music.setCode( "music" );

        final CategoryDescription musicEnglishDescription = new CategoryDescription();
        musicEnglishDescription.setName( "Music" );
        musicEnglishDescription.setCategory( music );
        musicEnglishDescription.setLanguage( en );

        final CategoryDescription musicFrenchDescription = new CategoryDescription();
        musicFrenchDescription.setName( "Musique" );
        musicFrenchDescription.setCategory( music );
        musicFrenchDescription.setLanguage( fr );

        final List<CategoryDescription> descriptions2 = new ArrayList<CategoryDescription>();
        descriptions2.add( musicEnglishDescription );
        descriptions2.add( musicFrenchDescription );

        music.setDescriptions( descriptions2 );

        categoryService.create( music );

        final Category novell = new Category();
        novell.setMerchantSore( store );
        novell.setCode( "novell" );

        final CategoryDescription novellEnglishDescription = new CategoryDescription();
        novellEnglishDescription.setName( "Novell" );
        novellEnglishDescription.setCategory( novell );
        novellEnglishDescription.setLanguage( en );

        final CategoryDescription novellFrenchDescription = new CategoryDescription();
        novellFrenchDescription.setName( "Roman" );
        novellFrenchDescription.setCategory( novell );
        novellFrenchDescription.setLanguage( fr );

        final List<CategoryDescription> descriptions3 = new ArrayList<CategoryDescription>();
        descriptions3.add( novellEnglishDescription );
        descriptions3.add( novellFrenchDescription );

        novell.setDescriptions( descriptions3 );

        novell.setParent( book );

        categoryService.create( novell );
        categoryService.addChild( book, novell );

        final Category tech = new Category();
        tech.setMerchantSore( store );
        tech.setCode( "tech" );

        final CategoryDescription techEnglishDescription = new CategoryDescription();
        techEnglishDescription.setName( "Technology" );
        techEnglishDescription.setCategory( tech );
        techEnglishDescription.setLanguage( en );

        final CategoryDescription techFrenchDescription = new CategoryDescription();
        techFrenchDescription.setName( "Technologie" );
        techFrenchDescription.setCategory( tech );
        techFrenchDescription.setLanguage( fr );

        final List<CategoryDescription> descriptions4 = new ArrayList<CategoryDescription>();
        descriptions4.add( techFrenchDescription );
        descriptions4.add( techFrenchDescription );

        tech.setDescriptions( descriptions4 );

        tech.setParent( book );

        categoryService.create( tech );
        categoryService.addChild( book, tech );

        final Category fiction = new Category();
        fiction.setMerchantSore( store );
        fiction.setCode( "fiction" );

        final CategoryDescription fictionEnglishDescription = new CategoryDescription();
        fictionEnglishDescription.setName( "Fiction" );
        fictionEnglishDescription.setCategory( fiction );
        fictionEnglishDescription.setLanguage( en );

        final CategoryDescription fictionFrenchDescription = new CategoryDescription();
        fictionFrenchDescription.setName( "Sc Fiction" );
        fictionFrenchDescription.setCategory( fiction );
        fictionFrenchDescription.setLanguage( fr );

        final List<CategoryDescription> fictiondescriptions = new ArrayList<CategoryDescription>();
        fictiondescriptions.add( fictionEnglishDescription );
        fictiondescriptions.add( fictionFrenchDescription );

        fiction.setDescriptions( fictiondescriptions );

        fiction.setParent( novell );

        categoryService.create( fiction );
        categoryService.addChild( book, fiction );

        // Add products
        // ProductType generalType = productTypeService.

        final Manufacturer oreilley = new Manufacturer();
        oreilley.setMerchantSore( store );

        final ManufacturerDescription oreilleyd = new ManufacturerDescription();
        oreilleyd.setLanguage( en );
        oreilleyd.setName( "O\'reilley" );
        oreilleyd.setManufacturer( oreilley );
        oreilley.getDescriptions().add( oreilleyd );

        manufacturerService.create( oreilley );

        final Manufacturer packed = new Manufacturer();
        packed.setMerchantSore( store );

        final ManufacturerDescription packedd = new ManufacturerDescription();
        packedd.setLanguage( en );
        packedd.setManufacturer( packed );
        packedd.setName( "Packed publishing" );
        packed.getDescriptions().add( packedd );

        manufacturerService.create( packed );

        final Manufacturer novells = new Manufacturer();
        novells.setMerchantSore( store );

        final ManufacturerDescription novellsd = new ManufacturerDescription();
        novellsd.setLanguage( en );
        novellsd.setManufacturer( novells );
        novellsd.setName( "Novells publishing" );
        novells.getDescriptions().add( novellsd );

        manufacturerService.create( novells );

        // PRODUCT 1

        final Product product = new Product();
        product.setProductHeight( new BigDecimal( 4 ) );
        product.setProductLength( new BigDecimal( 3 ) );
        product.setProductWidth( new BigDecimal( 1 ) );
        product.setSku( "TB12345" );
        product.setManufacturer( oreilley );
        product.setType( generalType );
        product.setMerchantSore( store );

        // Product description
        ProductDescription description = new ProductDescription();
        description.setName( "Spring in Action" );
        description.setLanguage( en );
        description.setProduct( product );

        product.getDescriptions().add( description );

        product.getCategories().add( tech );

        productService.create( product );

        // Availability
        final ProductAvailability availability = new ProductAvailability();
        availability.setProductDateAvailable( date );
        availability.setProductQuantity( 100 );
        availability.setRegion( "*" );
        availability.setProduct( product );// associate with product

        productAvailabilityService.create( availability );

        final ProductPrice dprice = new ProductPrice();
        dprice.setDefaultPrice( true );
        dprice.setProductPriceAmount( new BigDecimal( 29.99 ) );
        dprice.setProductPriceAvailability( availability );

        ProductPriceDescription dpd = new ProductPriceDescription();
        dpd.setName( "Base price" );
        dpd.setProductPrice( dprice );
        dpd.setLanguage( en );

        dprice.getDescriptions().add( dpd );

        productPriceService.create( dprice );

        // PRODUCT 2

        final Product product2 = new Product();
        product2.setProductHeight( new BigDecimal( 4 ) );
        product2.setProductLength( new BigDecimal( 3 ) );
        product2.setProductWidth( new BigDecimal( 1 ) );
        product2.setSku( "TB2468" );
        product2.setManufacturer( packed );
        product2.setType( generalType );
        product2.setMerchantSore( store );

        // Product description
        description = new ProductDescription();
        description.setName( "This is Node.js" );
        description.setLanguage( en );
        description.setProduct( product2 );

        product2.getDescriptions().add( description );

        product2.getCategories().add( tech );
        productService.create( product2 );

        // Availability
        final ProductAvailability availability2 = new ProductAvailability();
        availability2.setProductDateAvailable( date );
        availability2.setProductQuantity( 100 );
        availability2.setRegion( "*" );
        availability2.setProduct( product2 );// associate with product

        productAvailabilityService.create( availability2 );

        final ProductPrice dprice2 = new ProductPrice();
        dprice2.setDefaultPrice( true );
        dprice2.setProductPriceAmount( new BigDecimal( 39.99 ) );
        dprice2.setProductPriceAvailability( availability2 );

        dpd = new ProductPriceDescription();
        dpd.setName( "Base price" );
        dpd.setProductPrice( dprice2 );
        dpd.setLanguage( en );

        dprice2.getDescriptions().add( dpd );

        productPriceService.create( dprice2 );

        // PRODUCT 3

        final Product product3 = new Product();
        product3.setProductHeight( new BigDecimal( 4 ) );
        product3.setProductLength( new BigDecimal( 3 ) );
        product3.setProductWidth( new BigDecimal( 1 ) );
        product3.setSku( "NB1111" );
        product3.setManufacturer( packed );
        product3.setType( generalType );
        product3.setMerchantSore( store );

        // Product description
        description = new ProductDescription();
        description.setName( "A nice book for you" );
        description.setLanguage( en );
        description.setProduct( product3 );

        product3.getDescriptions().add( description );

        product3.getCategories().add( novell );
        productService.create( product3 );

        // Availability
        final ProductAvailability availability3 = new ProductAvailability();
        availability3.setProductDateAvailable( date );
        availability3.setProductQuantity( 100 );
        availability3.setRegion( "*" );
        availability3.setProduct( product3 );// associate with product

        productAvailabilityService.create( availability3 );

        final ProductPrice dprice3 = new ProductPrice();
        dprice3.setDefaultPrice( true );
        dprice3.setProductPriceAmount( new BigDecimal( 19.99 ) );
        dprice3.setProductPriceAvailability( availability3 );

        dpd = new ProductPriceDescription();
        dpd.setName( "Base price" );
        dpd.setProductPrice( dprice3 );
        dpd.setLanguage( en );

        dprice3.getDescriptions().add( dpd );

        productPriceService.create( dprice3 );

        // PRODUCT 4

        final Product product4 = new Product();
        product4.setProductHeight( new BigDecimal( 4 ) );
        product4.setProductLength( new BigDecimal( 3 ) );
        product4.setProductWidth( new BigDecimal( 1 ) );
        product4.setSku( "SF333345" );
        product4.setManufacturer( packed );
        product4.setType( generalType );
        product4.setMerchantSore( store );

        // Product description
        description = new ProductDescription();
        description.setName( "Battle of the worlds" );
        description.setLanguage( en );
        description.setProduct( product4 );

        product4.getDescriptions().add( description );

        product4.getCategories().add( fiction );
        productService.create( product4 );

        // Availability
        final ProductAvailability availability4 = new ProductAvailability();
        availability4.setProductDateAvailable( date );
        availability4.setProductQuantity( 100 );
        availability4.setRegion( "*" );
        availability4.setProduct( product4 );// associate with product

        productAvailabilityService.create( availability4 );

        final ProductPrice dprice4 = new ProductPrice();
        dprice4.setDefaultPrice( true );
        dprice4.setProductPriceAmount( new BigDecimal( 18.99 ) );
        dprice4.setProductPriceAvailability( availability4 );

        dpd = new ProductPriceDescription();
        dpd.setName( "Base price" );
        dpd.setProductPrice( dprice4 );
        dpd.setLanguage( en );

        dprice4.getDescriptions().add( dpd );

        productPriceService.create( dprice4 );

        // PRODUCT 5

        final Product product5 = new Product();
        product5.setProductHeight( new BigDecimal( 4 ) );
        product5.setProductLength( new BigDecimal( 3 ) );
        product5.setProductWidth( new BigDecimal( 1 ) );
        product5.setSku( "SF333346" );
        product5.setManufacturer( packed );
        product5.setType( generalType );
        product5.setMerchantSore( store );

        // Product description
        description = new ProductDescription();
        description.setName( "Battle of the worlds 2" );
        description.setLanguage( en );
        description.setProduct( product5 );

        product5.getDescriptions().add( description );

        product5.getCategories().add( fiction );
        productService.create( product5 );

        // Availability
        final ProductAvailability availability5 = new ProductAvailability();
        availability5.setProductDateAvailable( date );
        availability5.setProductQuantity( 100 );
        availability5.setRegion( "*" );
        availability5.setProduct( product5 );// associate with product

        productAvailabilityService.create( availability5 );

        final ProductPrice dprice5 = new ProductPrice();
        dprice5.setDefaultPrice( true );
        dprice5.setProductPriceAmount( new BigDecimal( 18.99 ) );
        dprice5.setProductPriceAvailability( availability5 );

        dpd = new ProductPriceDescription();
        dpd.setName( "Base price" );
        dpd.setProductPrice( dprice5 );
        dpd.setLanguage( en );

        dprice5.getDescriptions().add( dpd );

        productPriceService.create( dprice5 );

        // PRODUCT 6

        final Product product6 = new Product();
        product6.setProductHeight( new BigDecimal( 4 ) );
        product6.setProductLength( new BigDecimal( 3 ) );
        product6.setProductWidth( new BigDecimal( 1 ) );
        product6.setSku( "LL333444" );
        product6.setManufacturer( packed );
        product6.setType( generalType );
        product6.setMerchantSore( store );

        // Product description
        description = new ProductDescription();
        description.setName( "Life book" );
        description.setLanguage( en );
        description.setProduct( product6 );

        product6.getDescriptions().add( description );

        product6.getCategories().add( novell );
        productService.create( product6 );

        // Availability
        final ProductAvailability availability6 = new ProductAvailability();
        availability6.setProductDateAvailable( date );
        availability6.setProductQuantity( 100 );
        availability6.setRegion( "*" );
        availability6.setProduct( product6 );// associate with product

        productAvailabilityService.create( availability6 );

        final ProductPrice dprice6 = new ProductPrice();
        dprice6.setDefaultPrice( true );
        dprice6.setProductPriceAmount( new BigDecimal( 18.99 ) );
        dprice6.setProductPriceAvailability( availability6 );

        dpd = new ProductPriceDescription();
        dpd.setName( "Base price" );
        dpd.setProductPrice( dprice6 );
        dpd.setLanguage( en );

        dprice6.getDescriptions().add( dpd );

        productPriceService.create( dprice6 );

    }

    @Test
    public void test3CreateUser()
        throws ServiceException
    {

		  final MerchantStore store = merchantService.getMerchantStore(MerchantStore.DEFAULT_STORE);

		  final Group gsuperadmin = new Group("SUPERADMIN");
		  final Group gadmin = new Group("ADMIN");
		  final Group gcatalogue = new Group("GROUP_CATALOGUE");
		  final Group gstore = new Group("GROUP_STORE");
		  final Group gorder = new Group("GROUP_ORDER");

		  groupService.create(gsuperadmin);
		  groupService.create(gadmin);
		  groupService.create(gcatalogue);
		  groupService.create(gstore);
		  groupService.create(gorder);

		  groupService.create(gsuperadmin);
		  groupService.create(gadmin);
		  groupService.create(gcatalogue);
		  groupService.create(gstore);
		  groupService.create(gorder);
		  
		  final Permission auth = new Permission("AUTH");//Authenticated
		  auth.getGroups().add(gsuperadmin);
		  auth.getGroups().add(gadmin);
		  permissionService.create(auth);
		  
		  final Permission categories = new Permission("CATEGORIES");
		  categories.getGroups().add(gsuperadmin);
		  categories.getGroups().add(gadmin);
		  permissionService.create(categories);
		  
		  final Permission products = new Permission("PRODUCTS");
		  products.getGroups().add(gsuperadmin);
		  products.getGroups().add(gadmin);
		  permissionService.create(products);
		  
		  final Permission attributes = new Permission("ATTRIBUTES");
		  attributes.getGroups().add(gsuperadmin);
		  permissionService.create(attributes);
		  
		  final Permission featured = new Permission("FEATURED");
		  featured.getGroups().add(gsuperadmin);
		  permissionService.create(featured);
		  
		  final Permission order = new Permission("ORDER");
		  order.getGroups().add(gsuperadmin);
		  permissionService.create(order);
		  
		  final Permission content = new Permission("CONTENT");
		  content.getGroups().add(gsuperadmin);
		  permissionService.create(content);
		  final Permission pstore = new Permission("STORE");
		  pstore.getGroups().add(gsuperadmin);
		  permissionService.create(pstore);
		  
		  final Permission tax = new Permission("TAX");
		  tax.getGroups().add(gsuperadmin);
		  permissionService.create(tax);
		  final Permission payment = new Permission("PAYMENT");
		  payment.getGroups().add(gsuperadmin);
		  permissionService.create(payment);
		  final Permission shipping = new Permission("SHIPPING");
		  shipping.getGroups().add(gsuperadmin);
		  permissionService.create(shipping);
		  


    }

    @Test
    public void testGetProduct()
        throws ServiceException
    {

        final Language language = languageService.getByCode( "en" );

        final Locale locale = new Locale( "en", "CA" );


        final Product p = productService.getById(50L);

        //final Product p = productService.getProductForLocale( 1L, language, locale );


        if ( p != null )
        {

            System.out.println( p.getDescriptions().size() );

        }

    }

    @Test
    public void testGetProducts()
        throws ServiceException
    {
        final Language language = languageService.getByCode( "en" );
        final Locale locale = new Locale( "en", "CA" );

        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );

        final Category category = categoryService.getByCode( store, "book" );

        final int nrOfIterations = 1;

        for ( int i = 1; i <= nrOfIterations; i++ )
        {
            final List<Product> products = productService.getProductsForLocale( category, language, locale );
            for ( final Product product : products )
            {
                log.info( MessageFormat.format( "product found:{0}:iteration{1}", product.getId(), i ) );
            }
        }
    }

    @Test
    public void testGetProductsByCategories()
        throws ServiceException
    {
        final Language language = languageService.getByCode( "en" );
        final Locale locale = new Locale( "en", "CA" );
        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );

        // Category category = categoryService.getByCode(store, "novell");

        final Category category = categoryService.getByCode( store, "book" );

        categoryService.delete( category );

        System.out.println( "done" );

        /*
         * List<Long> ids = new ArrayList<Long>(); ids.add(1L); ids.add(2L); ids.add(3L); List<Product> products =
         * productService.getProducts(ids); System.out.println(products.size());
         */

    }

    @Test
    public void testCategory()
        throws ServiceException
    {

        /**
         * Creates a category hierarchy Music Books Novell Science-Fiction Technology Business
         */

        final Language en = languageService.getByCode( "en" );
        final Language fr = languageService.getByCode( "fr" );
        final Country ca = countryService.getByCode( "CA" );
        final Currency currency = currencyService.getByCode( "CAD" );
        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );

        final Category book = new Category();
        book.setDepth( 0 );
        book.setLineage( "/" );
        book.setMerchantSore( store );
        book.setCode( "book" );

        final CategoryDescription bookEnglishDescription = new CategoryDescription();
        bookEnglishDescription.setName( "Book" );
        bookEnglishDescription.setCategory( book );
        bookEnglishDescription.setLanguage( en );

        final CategoryDescription bookFrenchDescription = new CategoryDescription();
        bookFrenchDescription.setName( "Livre" );
        bookFrenchDescription.setCategory( book );
        bookFrenchDescription.setLanguage( fr );

        final List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();
        descriptions.add( bookEnglishDescription );
        descriptions.add( bookFrenchDescription );

        book.setDescriptions( descriptions );

        categoryService.create( book );

        final Category music = new Category();
        music.setDepth( 0 );
        music.setLineage( "/" );
        music.setMerchantSore( store );
        music.setCode( "music" );

        final CategoryDescription musicEnglishDescription = new CategoryDescription();
        musicEnglishDescription.setName( "Music" );
        musicEnglishDescription.setCategory( music );
        musicEnglishDescription.setLanguage( en );

        final CategoryDescription musicFrenchDescription = new CategoryDescription();
        musicFrenchDescription.setName( "Musique" );
        musicFrenchDescription.setCategory( music );
        musicFrenchDescription.setLanguage( fr );

        final List<CategoryDescription> descriptions2 = new ArrayList<CategoryDescription>();
        descriptions2.add( musicEnglishDescription );
        descriptions2.add( musicFrenchDescription );

        music.setDescriptions( descriptions2 );

        categoryService.create( music );

        final Category novell = new Category();
        novell.setDepth( 1 );
        novell.setLineage( "/" + book.getId() + "/" );
        novell.setMerchantSore( store );
        novell.setCode( "novell" );

        final CategoryDescription novellEnglishDescription = new CategoryDescription();
        novellEnglishDescription.setName( "Novell" );
        novellEnglishDescription.setCategory( novell );
        novellEnglishDescription.setLanguage( en );

        final CategoryDescription novellFrenchDescription = new CategoryDescription();
        novellFrenchDescription.setName( "Roman" );
        novellFrenchDescription.setCategory( novell );
        novellFrenchDescription.setLanguage( fr );

        final List<CategoryDescription> descriptions3 = new ArrayList<CategoryDescription>();
        descriptions3.add( novellEnglishDescription );
        descriptions3.add( novellFrenchDescription );

        novell.setDescriptions( descriptions3 );

        categoryService.create( novell );
        categoryService.addChild( book, novell );

        final Category tech = new Category();
        tech.setDepth( 1 );
        tech.setLineage( "/" + book.getId() + "/" );
        tech.setMerchantSore( store );
        tech.setCode( "tech" );

        final CategoryDescription techEnglishDescription = new CategoryDescription();
        techEnglishDescription.setName( "Technology" );
        techEnglishDescription.setCategory( tech );
        techEnglishDescription.setLanguage( en );

        final CategoryDescription techFrenchDescription = new CategoryDescription();
        techFrenchDescription.setName( "Technologie" );
        techFrenchDescription.setCategory( tech );
        techFrenchDescription.setLanguage( fr );

        final List<CategoryDescription> descriptions4 = new ArrayList<CategoryDescription>();
        descriptions4.add( techFrenchDescription );
        descriptions4.add( techFrenchDescription );

        tech.setDescriptions( descriptions4 );

        categoryService.create( tech );
        categoryService.addChild( book, tech );

    }

    @Test
    public void testGetModules()
        throws ServiceException
    {

        final List<IntegrationModule> shippingModules = moduleConfigurationService.getIntegrationModules( "SHIPPING" );

        for ( final IntegrationModule module : shippingModules )
        {

            System.out.println( module.getCode() );
        }
        System.out.println( "Done" );

    }

    @Test
    public void testCreateManufacturer()
        throws ServiceException
    {

        final Language english = new Language();
        english.setCode( "en" );
        languageService.create( english );

        final Language french = new Language();
        french.setCode( "fr" );
        languageService.create( french );

        final Currency euro = new Currency();
        euro.setCurrency( java.util.Currency.getInstance( "EUR" ) );
        currencyService.create( euro );

        final Currency cad = new Currency();
        cad.setCurrency( java.util.Currency.getInstance( "CAD" ) );
        currencyService.create( cad );

        final Country fr = new Country( "FR" );
        countryService.create( fr );

        final Country ca = new Country( "CA" );
        countryService.create( ca );

        final Language DEFAULT_LANGUAGE = languageService.getByCode( "en" );
        final Language FRENCH = languageService.getByCode( "fr" );
        final Currency currency = currencyService.getByCode( "CAD" );

        // create a merchant
        final MerchantStore store = new MerchantStore();
        store.setCountry( ca );
        store.setCurrency( currency );
        store.setDefaultLanguage( DEFAULT_LANGUAGE );
        store.setInBusinessSince( date );
        store.setStorename( "store name" );
        store.setStoreEmailAddress( "test@test.com" );
        merchantService.create( store );

        final Manufacturer manufacturer = new Manufacturer();
        // store.getManufacturers().add(manufacturer);

        // merchantService.update(store);

        // Manufacturer manufacturer = new Manufacturer();
        manufacturer.setMerchantSore( store );

        final ManufacturerDescription fd = new ManufacturerDescription();
        fd.setLanguage( FRENCH );
        fd.setName( "Sony french" );
        fd.setManufacturer( manufacturer );

        final ManufacturerDescription ed = new ManufacturerDescription();
        ed.setLanguage( DEFAULT_LANGUAGE );
        ed.setName( "Sony english" );
        ed.setManufacturer( manufacturer );

        final Set descriptions = new HashSet();
        descriptions.add( fd );
        descriptions.add( ed );

        manufacturer.setDescriptions( descriptions );

        manufacturerService.create( manufacturer );

        // manufacturerService.delete(manufacturer);
        // merchantService.delete(store);

    }

    @Test
    public void testDeleteManufacturerService()
        throws ServiceException
    {

        final Manufacturer manufacturer = manufacturerService.getById( 1L );
        manufacturerService.delete( manufacturer );

    }

    @Test
    public void testStoreRandomProducts()
        throws ServiceException
    {

        final Language en = testSupportFactory.createLanguage( "en" );
        languageService.save( en );

        final Language fr = testSupportFactory.createLanguage( "fr" );
        languageService.save( fr );

        final Language[] languages = { en, fr };

        final ProductType generalType = testSupportFactory.createProductType();
        productTypeService.save( generalType );

        final Country country = testSupportFactory.createCountry( en );
        countryService.save( country );

        final Currency currency = testSupportFactory.createCurrency();
        currencyService.save( currency );

        final MerchantStore store =
            testSupportFactory.createMerchantStore( MerchantStore.DEFAULT_STORE, country, currency, en );
        merchantService.save( store );

        final Manufacturer manufacturer = testSupportFactory.createRandomManufacturer( store, en );
        manufacturerService.save( manufacturer );

        final Category category = testSupportFactory.createCategory( null, store, languages );
        categoryService.save( category );

        final int nrOfProducts = 300;

        for ( int i = 1; i <= nrOfProducts; i++ )
        {
            log.info( MessageFormat.format( "adding product nr:{0}", i ) );
            testSupportFactory.createAndStoreRandomProduct( manufacturer, generalType, category, store, en );
        }
    }
}
