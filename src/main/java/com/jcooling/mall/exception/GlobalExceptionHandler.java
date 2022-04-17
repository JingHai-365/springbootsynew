package com.jcooling.mall.exception;

import com.jcooling.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @data: 2022/04/11
* @time: 10:31:17
* @version: 1.0
* @description: nothing.
*/
@ControllerAdvice
public class GlobalExceptionHandler {

   //日志输出
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e) {
        log.error("Default Exception: ", e);
        return ApiRestResponse.error(JcoolingMallExceptionEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(JcoolingMallException.class)
    @ResponseBody
    public Object handleImoocMallException(JcoolingMallException e) {
        log.error("JcoolingMallException: ", e);
        return ApiRestResponse.error(e.getCode(), e.getMsg());
    }


   @ExceptionHandler(MethodArgumentNotValidException.class)
   @ResponseBody
   public ApiRestResponse handleMethodExceptionArgumentNotValidException(MethodArgumentNotValidException e){
       log.error("MethodArgumentNotValidException",e);
       return handleBindingResult(e.getBindingResult());
   }
    //统一参数校验异常处理具体实现方法
    private ApiRestResponse handleBindingResult(BindingResult result){
        ArrayList<String> list = new ArrayList<>();
        if(result.hasErrors()){//whether check passed
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError allError : allErrors) {
                String defaultMessage = allError.getDefaultMessage();
                list.add(defaultMessage);
            }
        }
        if(list.size()==0){//由于list已经初始化，也就不可能等于null，因此需要通过长度判断
            //如果为空，返回请求参数错误
            return ApiRestResponse.error(JcoolingMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        //通常list不为空，返回也是异常处理，但其中的内容是有所指，也就是将list中的内容暴露出去
        return ApiRestResponse.error(JcoolingMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(),list.toString());
    }
}