package com.dmeplugin.dmestore.exception;


public class ServiceException extends Exception {
  private String code;
  private String message;

  public ServiceException() {
    super();
  }

  public ServiceException(String message) {
    super(message);
    this.message = message;
  }

  /**
   * 构造方法.
   * @param code 错误编码
   * @param message 错误信息
   */
  public ServiceException(String code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  @Override
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
}
