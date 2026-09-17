package tr.gov.kocaeli.sehir.identity.application;
import java.util.*;
public interface CorporateAuthenticationAdapter{Optional<CorporateIdentity>validate(String serviceTicket,String serviceUrl);record CorporateIdentity(String username,String fullName,String email,Map<String,String>attributes){}}

