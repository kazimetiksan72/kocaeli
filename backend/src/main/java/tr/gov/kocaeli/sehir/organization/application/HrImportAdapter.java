package tr.gov.kocaeli.sehir.organization.application;
import java.io.InputStream;import java.util.List;
public interface HrImportAdapter{List<PersonRecord>read(InputStream input);record PersonRecord(String personnelNo,String username,String fullName,String email,String unitCode,String title){}}
