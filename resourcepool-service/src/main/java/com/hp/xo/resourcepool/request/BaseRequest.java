package com.hp.xo.resourcepool.request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.hp.xo.resourcepool.exception.ApiErrorCode;
import com.hp.xo.resourcepool.exception.ServiceException;


public  class BaseRequest implements Request {
	private final Logger log = Logger.getLogger(this.getClass().getName());
    public static final String RESPONSE_TYPE_XML = "xml";
    public static final String RESPONSE_TYPE_JSON = "json";
    public static final DateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat NEW_INPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Pattern newInputDateFormat = Pattern.compile("[\\d]+-[\\d]+-[\\d]+ [\\d]+:[\\d]+:[\\d]+");
    public static final String DATE_OUTPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final DateFormat _outputFormat = new SimpleDateFormat(DATE_OUTPUT_FORMAT); 
    public enum FieldType {
        BOOLEAN, DATE, FLOAT, INTEGER, SHORT, LIST, LONG, OBJECT, MAP, STRING, TZDATE, UUID
    }
	private String userid = null;
	private String loginId = null;
	private String apikey = null;
	private String secretkey = null;
	private String clientIP = null;
	private String defaultLang = null;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getApikey() {
		return apikey;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	public String getSecretkey() {
		return secretkey;
	}
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String getDefaultLang() {
		return defaultLang;
	}
	public void setDefaultLang(String defaultLang) {
		this.defaultLang = defaultLang;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	public static String getDateString(Date date) {
        if (date == null) {
            return "";
        }
        String formattedString = null;
        synchronized (_outputFormat) {
            formattedString = _outputFormat.format(date);
        }
        return formattedString;
    }

//    // FIXME: move this to a utils method so that maps can be unpacked and integer/long values can be appropriately cast
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, Object> unpackParams(Map<String, String> params) {
        Map<String, Object> lowercaseParams = new HashMap<String, Object>();
        for (String key : params.keySet()) {
            int arrayStartIndex = key.indexOf('[');
            int arrayStartLastIndex = key.lastIndexOf('[');
            if (arrayStartIndex != arrayStartLastIndex) {
                throw new ServiceException(ApiErrorCode.MALFORMED_PARAMETER_ERROR + "Unable to decode parameter " + key
                        + "; if specifying an object array, please use parameter[index].field=XXX, e.g. userGroupList[0].group=httpGroup");
            }

            if (arrayStartIndex > 0) {
                int arrayEndIndex = key.indexOf(']');
                int arrayEndLastIndex = key.lastIndexOf(']');
                if ((arrayEndIndex < arrayStartIndex) || (arrayEndIndex != arrayEndLastIndex)) {
                    // malformed parameter
                    throw new ServiceException(ApiErrorCode.MALFORMED_PARAMETER_ERROR + "Unable to decode parameter " + key
                            + "; if specifying an object array, please use parameter[index].field=XXX, e.g. userGroupList[0].group=httpGroup");
                }

                // Now that we have an array object, check for a field name in the case of a complex object
                int fieldIndex = key.indexOf('.');
                String fieldName = null;
                if (fieldIndex < arrayEndIndex) {
                    throw new ServiceException(ApiErrorCode.MALFORMED_PARAMETER_ERROR + "Unable to decode parameter " + key
                            + "; if specifying an object array, please use parameter[index].field=XXX, e.g. userGroupList[0].group=httpGroup");
                } else {
                    fieldName = key.substring(fieldIndex + 1);
                }

                // parse the parameter name as the text before the first '[' character
                String paramName = key.substring(0, arrayStartIndex);
                paramName = paramName.toLowerCase();

                Map<Integer, Map> mapArray = null;
                Map<String, Object> mapValue = null;
                String indexStr = key.substring(arrayStartIndex + 1, arrayEndIndex);
                int index = 0;
                boolean parsedIndex = false;
                try {
                    if (indexStr != null) {
                        index = Integer.parseInt(indexStr);
                        parsedIndex = true;
                    }
                } catch (NumberFormatException nfe) {
                    log.warn("Invalid parameter " + key + " received, unable to parse object array, returning an error.");
                }

                if (!parsedIndex) {
                    throw new ServiceException(ApiErrorCode.MALFORMED_PARAMETER_ERROR + "Unable to decode parameter " + key
                            + "; if specifying an object array, please use parameter[index].field=XXX, e.g. userGroupList[0].group=httpGroup");
                }

                Object value = lowercaseParams.get(paramName);
                if (value == null) {
                    // for now, assume object array with sub fields
                    mapArray = new HashMap<Integer, Map>();
                    mapValue = new HashMap<String, Object>();
                    mapArray.put(Integer.valueOf(index), mapValue);
                } else if (value instanceof Map) {
                    mapArray = (HashMap) value;
                    mapValue = mapArray.get(Integer.valueOf(index));
                    if (mapValue == null) {
                        mapValue = new HashMap<String, Object>();
                        mapArray.put(Integer.valueOf(index), mapValue);
                    }
                }

                // we are ready to store the value for a particular field into the map for this object
                mapValue.put(fieldName, params.get(key));

                lowercaseParams.put(paramName, mapArray);
            } else {
                lowercaseParams.put(key.toLowerCase(), params.get(key));
            }
        }
        return lowercaseParams;
    }
    @Override
	public String toString() {
		return "BaseRequest [log=" + log + ", userid=" + userid + ", loginId="
				+ loginId + ", apikey=" + apikey + ", secretkey=" + secretkey
				+ ", clientIP=" + clientIP + ", defaultLang=" + defaultLang
				+ "]";
	}
}
