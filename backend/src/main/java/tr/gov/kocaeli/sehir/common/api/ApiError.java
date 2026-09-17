package tr.gov.kocaeli.sehir.common.api;
import java.util.*;
public record ApiError(String code,String message,List<FieldError> fieldErrors,String traceId){public record FieldError(String field,String message){}}

