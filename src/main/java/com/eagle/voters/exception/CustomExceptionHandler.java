package com.eagle.voters.exception;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.eagle.voters.utilities.Constants;

@SuppressWarnings({ "unchecked" })
@ControllerAdvice


public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	private final MessageSource messageSource;

	@Autowired
	public CustomExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Value("${spring.servlet.multipart.max-file-size}")
	String MAX_FILE_SIZE ;

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		JSONObject obj=new JSONObject();
		JSONArray arr=new JSONArray();
		try {	 
			obj.put(Constants.CODE,0);
			obj.put(Constants.MESSAGE,ex.getMessage());
			obj.put(Constants.DATA,arr);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(obj);
		} catch (Exception e) {
			JSONObject obj2=new JSONObject();
			obj2.put(Constants.ERROR_MESSAGE,ex.getMessage());
			arr.add(obj2);
			obj.put(Constants.CODE,0);
			obj.put(Constants.MESSAGE,Constants.UNRECOGNIZED_PROPERTY);
			obj.put(Constants.DATA,arr);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(obj);
		}
		 
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		JSONObject res=new JSONObject();
		JSONArray arr=new JSONArray();
		try {
			BindingResult result = ex.getBindingResult();
			List<ObjectError> allErrors = result.getAllErrors();
			List<JSONObject> errorList = new ArrayList<>();
			for (ObjectError objectError : allErrors) {
				JSONObject obj = new JSONObject();
				FieldError fieldError = (FieldError) objectError;
				obj.put(fieldError.getField(), messageSource.getMessage(objectError, request.getLocale()));
				errorList.add(obj);
			}
			res.put(Constants.CODE,Constants.CODE_0);
			res.put(Constants.MESSAGE,Constants.VALIDATION_FAILED);
			res.put(Constants.DATA,errorList);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			JSONObject obj2=new JSONObject();
			obj2.put(Constants.ERROR_MESSAGE,ex.getMessage());
			arr.add(obj2);
			res.put(Constants.CODE,0);
			res.put(Constants.MESSAGE,Constants.UNRECOGNIZED_PROPERTY);
			res.put(Constants.DATA,arr);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
		
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	  public ResponseEntity<JSONObject> handleMaxSizeException(MaxUploadSizeExceededException ex) {
		JSONObject obj=new JSONObject();
		 JSONArray arr=new JSONArray();
	    try {
				obj.put(Constants.CODE,0);
				obj.put(Constants.MESSAGE,Constants.FILE_SIZE_EXCEED+MAX_FILE_SIZE);
				obj.put(Constants.DATA,arr);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(obj);
		} catch (Exception e) {
			JSONObject obj2=new JSONObject();
			obj2.put(Constants.ERROR_MESSAGE,ex.getMessage());
			arr.add(obj2);
			obj.put(Constants.CODE,0);
			obj.put(Constants.MESSAGE,Constants.UNRECOGNIZED_PROPERTY);
			obj.put(Constants.DATA,arr);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(obj);
		}
		 
	  }
	
	 @Override
	 public ResponseEntity<Object> handleHttpMessageNotReadable(
			 HttpMessageNotReadableException ex, HttpHeaders headers,
             HttpStatus status, WebRequest request) {
		 JSONObject obj=new JSONObject();
		 JSONArray arr=new JSONArray();
		 try {
			 JSONObject obj2=new JSONObject();
			 obj2.put(Constants.ERROR_MESSAGE,ex.getRootCause().getMessage());
			 arr.add(obj2);
				obj.put(Constants.CODE,0);
				obj.put(Constants.MESSAGE,Constants.UNRECOGNIZED_PROPERTY);
				obj.put(Constants.DATA,arr);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(obj);
		} catch (Exception e) {
			JSONObject obj2=new JSONObject();
			obj2.put(Constants.ERROR_MESSAGE,ex.getMessage());
			arr.add(obj2);
			obj.put(Constants.CODE,0);
			obj.put(Constants.MESSAGE,Constants.UNRECOGNIZED_PROPERTY);
			obj.put(Constants.DATA,arr);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(obj);
		}
		 
     }   

	    @Override
	    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
   		 JSONObject obj=new JSONObject();
   		 JSONArray arr=new JSONArray();
	    	try {
				 JSONObject obj2=new JSONObject();
				 obj2.put(Constants.ERROR_MESSAGE,ex.getRootCause().getMessage()+Constants.SPACE+Constants.PARAMETER_MISSING);
				 obj2.put(ex.getParameterName(), ex.getParameterName()+" Missing");
				 arr.add(obj2);
				 
					obj.put(Constants.CODE,0);
					obj.put(Constants.MESSAGE,Constants.PARAMETER_MISSING);
					obj.put(Constants.DATA,arr);
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(obj);
			} catch (Exception e) {
				JSONObject obj2=new JSONObject();
				obj2.put(Constants.ERROR_MESSAGE,ex.getMessage());
				arr.add(obj2);
				obj.put(Constants.CODE,0);
				obj.put(Constants.MESSAGE,Constants.UNRECOGNIZED_PROPERTY);
				obj.put(Constants.DATA,arr);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(obj);
			}
	    	
	    } 
}






///

//@Data
//@EqualsAndHashCode(callSuper = false)
//@AllArgsConstructor
//class ApiError {
//
//	
//	   private HttpStatus status;
//	   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
//	   private LocalDateTime timestamp;
//	   private String message;
//	   private String debugMessage;
//	   private List<ApiSubError> subErrors;
//
//	   private ApiError() {
//	       timestamp = LocalDateTime.now();
//	   }
//
//	   ApiError(HttpStatus status) {
//	       this();
//	       this.status = status;
//	   }
//
//	   ApiError(HttpStatus status, Throwable ex) {
//	       this();
//	       this.status = status;
//	       this.message = "Unexpected error";
//	       this.debugMessage = ex.getLocalizedMessage();
//	   }
//
//	   ApiError(HttpStatus status, String message, Throwable ex) {
//	       this();
//	       this.status = status;
//	       this.message = message;
//	       this.debugMessage = ex.getLocalizedMessage();
//	   }
//	}
//
//
//
//abstract class ApiSubError {
//
//}
//
//@Data
//@EqualsAndHashCode(callSuper = false)
//@AllArgsConstructor
//class ApiValidationError extends ApiSubError {
//   private String object;
//   private String field;
//   private Object rejectedValue;
//   private String message;
//
//   ApiValidationError(String object, String message) {
//       this.object = object;
//       this.message = message;
//   }
//}
