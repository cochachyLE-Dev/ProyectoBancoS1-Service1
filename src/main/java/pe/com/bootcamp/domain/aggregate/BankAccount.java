package pe.com.bootcamp.domain.aggregate;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BankAccount {	
    private String id;
	private String accountNumber;
	private String clientIdentNum;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date creationDate;
}