package com.hp.xo.resourcepool.utils;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import com.hp.xo.resourcepool.ApiConstants;
import com.hp.xo.resourcepool.exception.ApiErrorCode;
import com.hp.xo.resourcepool.exception.CloudRuntimeException;
import com.hp.xo.resourcepool.exception.ServiceException;
import com.hp.xo.resourcepool.model.Parameter;
import com.hp.xo.resourcepool.request.BaseListRequest;
import com.hp.xo.resourcepool.request.BaseRequest;
import com.hp.xo.resourcepool.request.BaseRequest.FieldType;
import com.hp.xo.resourcepool.request.Request;

public class RequestWrapper {
    private static final Logger log = Logger.getLogger(RequestWrapper.class.getName());
    private static RequestWrapper instance;
    
    private RequestWrapper() {
    	super();
    }
    
    public static RequestWrapper getInstance() {
    	if (null == instance) {
    		instance = new RequestWrapper();
    	}
    	
        return instance;
    }


    public void wrapRequest(Request request, Map<String, Object> params)  {
      Map<String, Object> unpackedParams = params;//cmd.unpackParams(params);

      if (request instanceof BaseListRequest) {
          Object pageSizeObj = unpackedParams.get(ApiConstants.PAGE_SIZE);
          Long pageSize = null;
          if (pageSizeObj != null) {
              pageSize = Long.valueOf((String) pageSizeObj);
          }

          if ((unpackedParams.get(ApiConstants.PAGE) == null) && (pageSize != null && !pageSize.equals(BaseListRequest.PAGESIZE_UNLIMITED))) {
              ServiceException ex = new ServiceException(ApiErrorCode.PARAM_ERROR + "\"page\" parameter is required when \"pagesize\" is specified");
//              ex.setCSErrorCode(CSExceptionErrorCode.getCSErrCode(ex.getClass().getName()));
              throw ex;
          } else if (pageSize == null && (unpackedParams.get(ApiConstants.PAGE) != null)) {
              throw new ServiceException(ApiErrorCode.PARAM_ERROR + "\"pagesize\" parameter is required when \"page\" is specified");
          }
      }

      List<Field> fields = ReflectUtil.getAllFieldsForClass(request.getClass(), BaseRequest.class);

      for (Field field : fields) {
          Parameter parameterAnnotation = field.getAnnotation(Parameter.class);
          if ((parameterAnnotation == null) || !parameterAnnotation.expose()) {
              continue;
          }

          //TODO: Annotate @Validate on API Cmd classes, FIXME how to process Validate
//          Validate validateAnnotation = field.getAnnotation(Validate.class);
          Object paramObj = unpackedParams.get(parameterAnnotation.name());
//          if (paramObj == null) {
//              if (parameterAnnotation.required()) {
//                  throw new ServerApiException(ApiErrorCode.PARAM_ERROR, "Unable to execute API command " + request.getCommandName().substring(0, request.getCommandName().length() - 8) + " due to missing parameter "
//                          + parameterAnnotation.name());
//              }
//              continue;
//          }

          // marshall the parameter into the correct type and set the field value
          try {
          	if (paramObj != null) {
          		setFieldValue(field, request, paramObj, parameterAnnotation);
          	} 
          } catch (Exception e) {
          		// TOOD
          		e.printStackTrace();
          }
          	
//          } catch (IllegalArgumentException argEx) {
//              if (log.isDebugEnabled()) {
//                  log.debug("Unable to execute API command " + request.getCommandName() + " due to invalid value " + paramObj + " for parameter " + parameterAnnotation.name());
//              }
//              throw new ServiceException(ApiErrorCode.PARAM_ERROR + "Unable to execute API command " + request.getCommandName().substring(0, request.getCommandName().length() - 8) + " due to invalid value " + paramObj
//                      + " for parameter " + parameterAnnotation.name());
//          } catch (ParseException parseEx) {
//              if (log.isDebugEnabled()) {
//                  log.debug("Invalid date parameter " + paramObj + " passed to command " + request.getCommandName().substring(0, request.getCommandName().length() - 8));
//              }
//              throw new ServiceException(ApiErrorCode.PARAM_ERROR + "Unable to parse date " + paramObj + " for command " + request.getCommandName().substring(0, request.getCommandName().length() - 8)
//                      + ", please pass dates in the format mentioned in the api documentation");
////          } catch (InvalidParameterValueException invEx) {
////              throw new ServiceException(ApiErrorCode.PARAM_ERROR, "Unable to execute API command " + request.getCommandName().substring(0, request.getCommandName().length() - 8) + " due to invalid value. " + invEx.getMessage());
//          } catch (CloudRuntimeException cloudEx) {
//          	log.error("CloudRuntimeException", cloudEx);
//              // FIXME: Better error message? This only happens if the API command is not executable, which typically
//              //means
//              // there was
//              // and IllegalAccessException setting one of the parameters.
//              throw new ServiceException(ApiErrorCode.INTERNAL_ERROR + "Internal error executing API command " + request.getCommandName().substring(0, request.getCommandName().length() - 8));
//          }

      }
  }

    public Map<String, Object[]> tranformFromRequest(BaseRequest request) {
    	Map<String, Object[]> result = null;
    	Map<String, Object> params = new HashMap<String, Object>();
    	this.wrapParams(params, request);
    	if (null != params) {
    		result = new HashMap<String, Object[]>(params.size());
    	}
    	
    	for (String key : params.keySet()) {
    		result.put(key, new Object[]{params.get(key)});
    	}
    	
    	return result;
    }
    public void wrapRequest2(final Request request, final Map<String, Object[]> params) {
    	Map<String, Object> params2 = new HashMap<String, Object>(params.size());
    	
    	for (String key : params.keySet()) {
    		Object[] values = params.get(key);
    		if (null != values && values.length > 0) {
    			params2.put(key, values[0]);
    		} else {
    			params2.put(key, null);
    		}
    		
    	}
    	
    	this.wrapRequest(request, params2);
    }
//    
//    public void wrapParams2(final Map<String, Object[]> params, final BaseRequest request) {
//    	Map<String, Object> params2 = new HashMap<String, Object>(params.size());
//    	
//    	for (String key : params.keySet()) {
//    		Object[] values = params.get(key);
//    		if (null != values && values.length > 0) {
//    			params2.put(key, values[0]);
//    		} else {
//    			params2.put(key, null);
//    		}
//    		
//    	}
//    	
//    	this.wrapParams(params2, request);
//    	
//    } 
    
    public void wrapParams(final Map<String, Object> params, final BaseRequest request) {

        List<Field> fields = ReflectUtil.getAllFieldsForClass(request.getClass(), BaseRequest.class);

        
        for (Field field : fields) {
            Parameter parameterAnnotation = field.getAnnotation(Parameter.class);
            if ((parameterAnnotation == null) || !parameterAnnotation.expose()) {
                continue;
            }
            System.out.println("Object paramObj = params.get(parameterAnnotation.name()) , " + parameterAnnotation.name());
//            Object paramObj = params.get(parameterAnnotation.name());
//            System.out.println(paramObj);
            // marshall the parameter into the correct type and set the field value
            try {
//            	if (paramObj != null) {
            		Object value = getFieldValue(field, request, parameterAnnotation);
            		
            		if (null != value) {
            			params.put(parameterAnnotation.name(), value);
            			System.out.println("--"+value);		
            		}
//            	}
            	
//            } catch (IllegalArgumentException argEx) {
//                if (log.isDebugEnabled()) {
//                    log.debug("Unable to execute API command " + request.getCommandName() + " due to invalid value " + paramObj + " for parameter " + parameterAnnotation.name());
//                }
//                throw new ServiceException(ApiErrorCode.PARAM_ERROR + "Unable to execute API command " + request.getCommandName().substring(0, request.getCommandName().length() - 8) + " due to invalid value " + paramObj
//                        + " for parameter " + parameterAnnotation.name());
//            } catch (ParseException parseEx) {
//                if (log.isDebugEnabled()) {
//                    log.debug("Invalid date parameter " + paramObj + " passed to command " + request.getCommandName().substring(0, request.getCommandName().length() - 8));
//                }
//                throw new ServiceException(ApiErrorCode.PARAM_ERROR + "Unable to parse date " + paramObj + " for command " + request.getCommandName().substring(0, request.getCommandName().length() - 8)
//                        + ", please pass dates in the format mentioned in the api documentation");
////            } catch (InvalidParameterValueException invEx) {
////                throw new ServiceException(ApiErrorCode.PARAM_ERROR, "Unable to execute API command " + request.getCommandName().substring(0, request.getCommandName().length() - 8) + " due to invalid value. " + invEx.getMessage());
            } catch (CloudRuntimeException cloudEx) {
            	log.error("CloudRuntimeException", cloudEx);
                // FIXME: Better error message? This only happens if the API command is not executable, which typically
                //means
                // there was
                //TODO and IllegalAccessException setting one of the parameters.
                throw new ServiceException(ApiErrorCode.INTERNAL_ERROR + "Internal error executing API command ");
            } catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
    }

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object getFieldValue(Field field, BaseRequest request, Parameter annotation) throws IllegalArgumentException, ParseException {
    	Object result = null;
        try {
            field.setAccessible(true);
            FieldType fieldType = annotation.type();
            switch (fieldType) {
            case BOOLEAN:
            	result = field.get(request); 
                break;
            case DATE:
                // This piece of code is for maintaining backward compatibility
                // and support both the date formats(Bug 9724)
                // Do the date messaging for ListEventsCmd only
//                if (cmdObj instanceof ListEventsCmd || cmdObj instanceof DeleteEventsCmd
//                        || cmdObj instanceof ArchiveEventsCmd
//                        || cmdObj instanceof ArchiveAlertsCmd
//                        || cmdObj instanceof DeleteAlertsCmd
//                        ) { TODO
            	  if (1==1) {
            		result = field.get(request);
            		if (null != result) {
	                    boolean isObjInNewDateFormat = isObjInNewDateFormat(result.toString());
	                    if (isObjInNewDateFormat) {
	                        DateFormat newFormat = BaseRequest.NEW_INPUT_FORMAT;
	                        synchronized (newFormat) {
	                            result = newFormat.parse(result.toString());
	                        }
	                    } else {
	                        DateFormat format = BaseRequest.INPUT_FORMAT;
	                        synchronized (format) {
	                            Date date = format.parse(result.toString());
	                            if (field.getName().equals("startDate")) {
	                                date = messageDate(date, 0, 0, 0);
	                            } else if (field.getName().equals("endDate")) {
	                                date = messageDate(date, 23, 59, 59);
	                            }
	                            result = date;
	                        }
	                    }
            		}
                } else {
                    DateFormat format = BaseRequest.INPUT_FORMAT;
                    format.setLenient(false);
                    synchronized (format) {
                        result = format.parseObject(result.toString());
                    }
                }
                break;
            case FLOAT:
            	result = field.get(request); 
                break;
            case INTEGER:
            	result = field.get(request); 
                break;
            case LIST:
            	result = field.get(request);
            	if (null != result) {
	                List listParam = new ArrayList();
	                StringTokenizer st = new StringTokenizer(result.toString(), ",");
	                while (st.hasMoreTokens()) {
	                    String token = st.nextToken();
	                    FieldType listType = annotation.collectionType();
	                    switch (listType) {
	                    case INTEGER:
	                        listParam.add(Integer.valueOf(token));
	                        break;
	                    case UUID:
	                        if (token.isEmpty())
	                            break;
	//                        Long internalId = translateUuidToInternalId(token, annotation);
	//                        listParam.add(internalId);
	                        listParam.add(token);
	                        break;
	                    case LONG: {
	                        listParam.add(Long.valueOf(token));
	                    }
	                    break;
	                    case SHORT:
	                        listParam.add(Short.valueOf(token));
	                    case STRING:
	                        listParam.add(token);
	                        break;
	                    }
	                }
	                
	                result = listParam;
            	}
                break;
            case UUID:
            	result = field.get(request); 
                break;
            case LONG:
            	result = field.get(request); 
                break;
            case SHORT:
            	result = field.get(request); 
                break;
            case STRING:
            	result = field.get(request);                
                break;
            case TZDATE:
            	result = field.get(request); 
            	if (null != result) {
            		result = DateUtil.parseTZDateString(result.toString());
            	}
                break;
            case MAP:
            default:
            	result = field.get(request);        
                break;
            }
        } catch (IllegalAccessException ex) {
        	//TODO
            log.error("Error initializing command , field " + field.getName() + " is not accessible.");
            throw new CloudRuntimeException("Internal error initializing parameters for command  [field " + field.getName() + " is not accessible]");
        }
        
		return result;
    }

 
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void setFieldValue(Field field, Request request, Object paramObj, Parameter annotation) throws IllegalArgumentException, ParseException {
        try {
            field.setAccessible(true);
            FieldType fieldType = annotation.type();
            switch (fieldType) {
            case BOOLEAN:
            	if (null != paramObj) {
            		field.set(request, Boolean.valueOf(paramObj.toString())); 
            	} else {
            		field.set(request, null);
            	}
                break;
            case DATE:
                // This piece of code is for maintaining backward compatibility
                // and support both the date formats(Bug 9724)
                // Do the date messaging for ListEventsCmd only
//                if (cmdObj instanceof ListEventsCmd || cmdObj instanceof DeleteEventsCmd
//                        || cmdObj instanceof ArchiveEventsCmd
//                        || cmdObj instanceof ArchiveAlertsCmd
//                        || cmdObj instanceof DeleteAlertsCmd
//                        ) { TODO
            	  if (1==1) {
                    boolean isObjInNewDateFormat = isObjInNewDateFormat(paramObj.toString());
                    if (isObjInNewDateFormat) {
                        DateFormat newFormat = BaseRequest.NEW_INPUT_FORMAT;
                        synchronized (newFormat) {
                            field.set(request, newFormat.parse(paramObj.toString()));
                        }
                    } else {
                        DateFormat format = BaseRequest.INPUT_FORMAT;
                        synchronized (format) {
                            Date date = format.parse(paramObj.toString());
                            if (field.getName().equals("startDate")) {
                                date = messageDate(date, 0, 0, 0);
                            } else if (field.getName().equals("endDate")) {
                                date = messageDate(date, 23, 59, 59);
                            }
                            field.set(request, date);
                        }
                    }
                } else {
                    DateFormat format = BaseRequest.INPUT_FORMAT;
                    format.setLenient(false);
                    synchronized (format) {
                        field.set(request, format.parse(paramObj.toString()));
                    }
                }
                break;
            case FLOAT:
                // Assuming that the parameters have been checked for required before now,
                // we ignore blank or null values and defer to the command to set a default
                // value for optional parameters ...
                if (paramObj != null && isNotBlank(paramObj.toString())) {
                    field.set(request, Float.valueOf(paramObj.toString()));
                }
                break;
            case INTEGER:
                // Assuming that the parameters have been checked for required before now,
                // we ignore blank or null values and defer to the command to set a default
                // value for optional parameters ...
                if (paramObj != null && isNotBlank(paramObj.toString())) {
                    field.set(request, Integer.valueOf(paramObj.toString()));
                }
                break;
            case LIST:
                List listParam = new ArrayList();
                StringTokenizer st = new StringTokenizer(paramObj.toString(), ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    FieldType listType = annotation.collectionType();
                    switch (listType) {
                    case INTEGER:
                        listParam.add(Integer.valueOf(token));
                        break;
                    case UUID:
                        if (token.isEmpty())
                            break;
//                        Long internalId = translateUuidToInternalId(token, annotation);
//                        listParam.add(internalId);
                        listParam.add(token);
                        break;
                    case LONG: {
                        listParam.add(Long.valueOf(token));
                    }
                    break;
                    case SHORT:
                        listParam.add(Short.valueOf(token));
                    case STRING:
                        listParam.add(token);
                        break;
                    }
                }
                field.set(request, listParam);
                break;
            case UUID:
                if (null == paramObj || paramObj.toString().isEmpty())
                    break;
//                Long internalId = translateUuidToInternalId(paramObj.toString(), annotation);
//                field.set(request, internalId);
                  field.set(request, paramObj);
                break;
            case LONG:
            	if (null != paramObj) {
            		field.set(request, Long.valueOf(paramObj.toString()));
            	} else {
                    field.set(request, null);
            	}
                break;
            case SHORT:
                field.set(request, Short.valueOf(paramObj.toString()));
                break;
            case STRING:
                if ((paramObj != null) && paramObj.toString().length() > annotation.length()) {
                    log.error("Value greater than max allowed length " + annotation.length() + " for param: " + field.getName());
//                    throw new InvalidParameterValueException("Value greater than max allowed length " + annotation.length() + " for param: " + field.getName());
                    throw new ServiceException("Value greater than max allowed length " + annotation.length() + " for param: " + field.getName());
                }
                
                field.set(request, (String)paramObj);
                
                break;
            case TZDATE:
                field.set(request, DateUtil.parseTZDateString(paramObj.toString()));
                break;
            case MAP:
            default:
                field.set(request, paramObj);
                break;
            }
        } catch (IllegalAccessException ex) {
        	//TODO
            log.error("Error initializing command , field " + field.getName() + " is not accessible.");
            throw new CloudRuntimeException("Internal error initializing parameters for command [field " + field.getName() + " is not accessible]");
        }
    }

    private static boolean isObjInNewDateFormat(String string) {
        Matcher matcher = BaseRequest.newInputDateFormat.matcher(string);
        return matcher.matches();
    }

    private static Date messageDate(Date date, int hourOfDay, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        return cal.getTime();
    }

}
