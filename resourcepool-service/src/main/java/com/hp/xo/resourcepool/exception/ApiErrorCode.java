package com.hp.xo.resourcepool.exception;

/**
 * Enum class for various  error code
 *
 */
public enum ApiErrorCode {

    MALFORMED_PARAMETER_ERROR(430),
    PARAM_ERROR(431),
    UNSUPPORTED_ACTION_ERROR(432),
    API_LIMIT_EXCEED(429),

    INTERNAL_ERROR(530),
    ACCOUNT_ERROR(531),
    ACCOUNT_RESOURCE_LIMIT_ERROR(532),
    INSUFFICIENT_CAPACITY_ERROR(533),
    RESOURCE_UNAVAILABLE_ERROR(534),
    RESOURCE_ALLOCATION_ERROR(535),
    RESOURCE_IN_USE_ERROR(536),
    NETWORK_RULE_CONFLICT_ERROR(537);

    private int httpCode;

    private ApiErrorCode(int httpStatusCode){
        httpCode = httpStatusCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String toString(){
        return String.valueOf(this.httpCode);
    }


}
