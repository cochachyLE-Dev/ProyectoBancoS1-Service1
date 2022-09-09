package pe.com.bootcamp.domain.aggregate;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Client {
    private String id;
	private String dni;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = " yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date creationDate;
}
