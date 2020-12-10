package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.constant.DmeConstants;
import com.dmeplugin.dmestore.exception.DmeException;
import com.dmeplugin.dmestore.exception.NoDmeException;
import com.dmeplugin.dmestore.exception.VcenterRuntimeException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DMEOpenApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * BaseController
 *
 * @author yy
 * @since 2020-12-01
 **/
@Controller
public class BaseController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    protected static final String CODE_SUCCESS = "200";

    protected static final String CODE_ALL_FAILURE = "-100001";

    protected static final String CODE_NOTALL_FAILURE = "-100000";

    private static final String CODE_FAILURE = "-99999";

    private static final String CODE_DB_EXCEPTION = "-70001";

    private static final String CODE_ESIGHT_CONNECT_EXCEPTION = "-80010";

    private static final String CODE_NO_ESIGHT_EXCEPTION = "-80011";

    private static final String CODE_CERT_EXCEPTION = "-80002";

    private static final String PREFIX = "-50";

    private static final String FIELD_CODE = "code";

    private static final String FIELD_DESCRIPTION = "description";

    private static final String FIELD_DATA = "data";

    private static final ResponseBodyBean FAILURE_BEAN = new ResponseBodyBean(CODE_FAILURE, null, null);

    private static final ResponseBodyBean SUCCESS_BEAN = new ResponseBodyBean(CODE_SUCCESS, null, null);

    @ExceptionHandler(NoDmeException.class)
    @ResponseStatus(HttpStatus.OK)
    protected Map<String, Object> handleException(final NoDmeException exception) {
        LOGGER.debug("No eSight configuration!{}", exception.getMessage());
        return generateError(generateCode(exception.getCode()), exception.getMessage(), Collections.emptyList());
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.OK)
    protected Map<String, Object> handleException(final RestClientException exception,
        final HttpServletRequest request) {
        LOGGER.error("Rest client Exception!{}", exception.getMessage());
        Throwable rootCause = exception.getRootCause();
        if (rootCause instanceof CertificateException) {
            return generateError(request, CODE_CERT_EXCEPTION, exception.getMessage(), null);
        }
        return generateError(request, CODE_ESIGHT_CONNECT_EXCEPTION, exception.getMessage(), null);
    }

    @ExceptionHandler(VcenterRuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    protected Map<String, Object> handleException(final VcenterRuntimeException exception,
        final HttpServletRequest request) {
        LOGGER.error("vCenter plugin exception!{}", exception.getMessage());
        return generateError(request, generateCode(exception.getCode()), exception.getMessage(), null);
    }

    @ExceptionHandler(DmeException.class)
    @ResponseStatus(HttpStatus.OK)
    protected Map<String, Object> handleException(final DmeException exception, final HttpServletRequest request) {
        LOGGER.error("eSight Exception!{}", exception.getMessage());
        if (CODE_NO_ESIGHT_EXCEPTION.equals(exception.getCode())) {
            return generateError(request, exception.getCode(), exception.getMessage(), null);
        }
        return generateError(request, PREFIX + exception.getCode(), exception.getMessage(), null);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.OK)
    protected Map<String, Object> handleException(final SQLException exception, final HttpServletRequest request) {
        LOGGER.error("DB Exception!{}", exception.getMessage());
        Map<String, Object> errorMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        errorMap.put(FIELD_CODE, CODE_DB_EXCEPTION);
        errorMap.put(FIELD_DESCRIPTION, exception.getMessage());
        errorMap.put(FIELD_DATA, null);
        return errorMap;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    protected Map<String, Object> handleException(final Exception exception, final HttpServletRequest request) {
        LOGGER.error("System Exception!{}", exception.getMessage());
        return generateError(request, CODE_FAILURE, exception.getMessage(), null);
    }

    private Map<String, Object> generateError(final HttpServletRequest request, final String code, final String message,
        final Object data) {
        Map<String, Object> errorMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        errorMap.put(FIELD_CODE, code);
        errorMap.put(FIELD_DESCRIPTION, message);
        errorMap.put(FIELD_DATA, data);
        if (request != null && "GET".equalsIgnoreCase(request.getMethod())) {
            String ip = request.getParameter("esightIp");
            if (ip == null || "".equals(ip.trim())) {
                ip = request.getParameter("ip");
            }
            errorMap.put("ip", ip == null ? "" : ip);
        }
        return errorMap;
    }

    private Map<String, Object> generateError(final String code, final String message, final Object data) {
        return generateError(null, code, message, data);
    }

    public String generateCode() {
        return generateCode(null);
    }

    public String generateCode(final String code) {
        return (code == null || code.isEmpty()) ? CODE_FAILURE : code;
    }

    public boolean isSuccessResponse(final Object code) {
        return DMEOpenApiService.isSuccessResponse(code);
    }

    protected ResponseBodyBean success() {
        return SUCCESS_BEAN;
    }

    protected ResponseBodyBean success(Object data) {
        return success(data, null);
    }

    protected ResponseBodyBean success(Object data, String description) {
        ResponseBodyBean bodyBean = new ResponseBodyBean(CODE_SUCCESS, null, null);
        bodyBean.setData(data);
        bodyBean.setDescription(description);
        return bodyBean;
    }

    protected ResponseBodyBean failure() {
        return FAILURE_BEAN;
    }

    protected ResponseBodyBean failure(String description) {
        return failure(CODE_FAILURE, description);
    }

    protected ResponseBodyBean failure(String code, String description) {
        ResponseBodyBean bodyBean = new ResponseBodyBean(CODE_FAILURE, null, null);
        bodyBean.setDescription(description);
        bodyBean.setCode(code);
        return bodyBean;
    }

    protected ResponseBodyBean failure(String code, String description, Object data) {
        ResponseBodyBean bodyBean = new ResponseBodyBean(CODE_FAILURE, null, null);
        bodyBean.setDescription(description);
        bodyBean.setCode(code);
        bodyBean.setData(data);

        return bodyBean;
    }

    protected ResponseBodyBean failure(String description, Object data) {
        return failure(CODE_FAILURE, description, data);
    }
}