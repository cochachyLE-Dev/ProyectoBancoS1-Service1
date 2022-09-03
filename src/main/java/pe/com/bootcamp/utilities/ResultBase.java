package pe.com.bootcamp.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class ResultBase {	
	
	@Getter @Setter
	private boolean ibException;
	@Getter @Setter @JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;	
	
	public ResultBase() {}
	public ResultBase(boolean ibException, String message) {
		super();
		this.ibException = ibException;
		this.message = message;
	}	
}

