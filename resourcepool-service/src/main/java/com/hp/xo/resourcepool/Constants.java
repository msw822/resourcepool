package com.hp.xo.resourcepool;



/**
 * Constant values used throughout the application.
 *
 * @author Zhefang Chen
 */
public final class Constants {

	private Constants() {
        // hide me
    }
	
    public static final String ACTION_ADD = "add";

	public static final String ACTION_UPDATE = "update";

	public static final String EMPTY = "";

	
    //~ Static fields/initializers =============================================
    /* =========================================================================
     * System Constants
     * =========================================================================
     */
    /** The name of the ResourceBundle used in this application */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /** File separator from System properties */
    public static final String FILE_SEP = System.getProperty("file.separator");

    /** User home from System properties */
    public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

    /** The name of the configuration hashmap stored in web application scope. */
    public static final String CONFIG = "appConfig";

    /**
     * Session scope attribute that holds the locale set by the user. By setting this key
     * to the same one that Struts uses, we get synchronization in Struts w/o having
     * to do extra work or have two session-level variables.
     */
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

    /** The name of the CSS Theme setting. */
    public static final String CSS_THEME = "csstheme";
    
    /** The name of menu repositroy key. */
    public static final String MENU_REPOSITORY_KEY = "userMenuRepository";
    
    /** The name of top menu list. */
    public static final String MENU_TOP_LIST = "userTopMenuList";
    
    /** The name of menu permession adapoter. */
    public static final String MENU_PERMESSION_ADAPOTER = "userPermessionAdapoter";
    
    /** Resource type - url */
    public static final String RESOURCE_TYPE_URL = "URL";
    
    /** Resource type - function */
    public static final String RESOURCE_TYPE_FUNC = "FUNC";
    
    /** The name of the Administrator role, as specified in web.xml */
    public static final String ROLE_ADMIN = "SYS_ADMIN";

    /** String UTF8 chartset */
    public static final String CHARSET_UTF8 = "UTF-8";
    
    /** Default Database Config reload space time (ms) */
    public static final Long DEFAULT_DB_CONFIG_RELOAD_SPACETIME = 500000L;
    
    /** Default DataTable row count */
    public static final Long DEFAULT_DATATABLE_ROWCOUNT = 10L;
    
	public static final String SESSION_MESSAGES = "messages";
	public static final String SESSION_ERRORS = "errors";
	public static final String SESSION_USER = "userProfile";
	public static final String SESSION_ORG_CODE = "currentOrgCode";
	public static final String REQUEST_LOCALE = "locale";
	public static final String REQUEST_THEME = "theme";
	
	public static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd";
	public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	public static final String DEFAULT_DATETIME_PATTERN	= DEFAULT_DATE_PATTERN + " " + DEFAULT_TIME_PATTERN;
	public static final String DECIMAL_FORMAT = "###,###.00";
	public static final String RESOURCE_BUNDLE_IMPORT_KEY_PREFIX = "@import@";
	public static final String BREADCRUMB_BEGIN = "[BREADCRUMB]";
	public static final String BREADCRUMB_END = "[/BREADCRUMB]";
    public static final String DEFAULT_CHANGE_PASSWORD_PATH = "/changePassword.html";
    public static final String BUNDLE_RESOURCE_CONTEXT_PARAM_KEY = "javax.servlet.jsp.jstl.fmt.localizationContext";
    public static final Long DEFAULT_APPLICATION_ID = 3L;
    public static final String APPLICATION_ID="application.id";
    public static final Integer DEFAULT_EDITLOCK_MAX_LOCK_DURATION = 5;
    public static final Integer DEFAULT_USER_LOGIN_ATTEMPT_TIMES = 5;
    public static final Integer LEAST_PASSWORD_LENGTH = 6;
    public static final Boolean DEFAULT_AUDIT_TRAIL_ENABLE = true;
    public static final Integer NEW_USER_VERSION = 0;
    public static final Integer DEFAULT_PASSWORD_EXPIRIED_ON = 30;
	public static final String DEFAULT_PASSWORD_OF_USER = "123456";
	public static final Integer USER_LOGIN_ATTEMPT_TIME = DEFAULT_USER_LOGIN_ATTEMPT_TIMES;
    
    /* =========================================================================
     * Ohter Constants
     * =========================================================================
     */
    
    /** The request scope attribute under which an editable user form is stored */
    public static final String USER_KEY = "userForm";

    /** The request scope attribute that holds the user list */
    public static final String USER_LIST = "userList";

    /** The request scope attribute for indicating a newly-registered user */
    public static final String REGISTERED = "registered";
    
    /**
     * The name of the user's role list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String USER_ROLES = "userRoles";

    /**
     * The name of the available roles list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String AVAILABLE_ROLES = "availableRoles";
    
    public static final String ROLE_KEY = "role";
    public static final String USER_ID="userId";
    public static final String USER_EMAIL="email";

    public static final String SPLIT_BY_SEMICOLON=";";
    public static final String SPLIT_BY_COMMA=",";
            

	public static final String YES = "Yes";
	public static final String NO="No";
	
	public static final String RELEASE_DAY = "RELEASEDAY";
	public static final String NOT_EQUALS_STATUS = "NOT_EQUALS_STATUS";

	public static final String SPLIT_MIDDLE_LINE = "-";

	public static final String KEY_FROM_PLANNED_ETD = "fromPlannedETD";
	public static final String KEY_TO_PLANNED_ETD = "toPlannedETD";
    public static final String GFC_PARTY_CODE = "GFC_PARTY_CODE";
    public static final String GFC_PARTY_DESCRIPTON = "GFC_PARTY_DESCRIPTON";
    public static final String ORGANIZATION_CODE = "ORGANIZATION_CODE";
    public static final String PECENT_LIKE = "%";
    
    public static final String DATE_PATTERN_YYYY_MM = "yyyy-MM";
 }
