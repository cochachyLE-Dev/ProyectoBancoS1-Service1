package pe.com.bootcamp.utilities;

import java.util.ArrayList;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

public class UnitResult<T> extends ResultBase {	
	
	@Getter @Setter @JsonInclude(JsonInclude.Include.NON_NULL)
	private T value;
	@Getter @Setter @JsonInclude(JsonInclude.Include.NON_NULL)
	private ArrayList<T> list;
	
	public UnitResult() {}
	
	public UnitResult(boolean ibException, String message) {
		super(ibException, message);
	}
	public UnitResult(T value, ArrayList<T> list,@Nullable boolean ibException, @Nullable String message) {
		super(ibException, message);
		this.value = value;
		this.list = list;
	}
	public UnitResult(T value, @Nullable boolean ibException, @Nullable String message) {
		super(ibException, message);
		this.value = value;
		this.list = null;
	}
	public UnitResult(T value) {
		super(false, null);
		this.value = value;
		this.list = null;
	}
	public UnitResult(ArrayList<T> list,@Nullable boolean ibException, @Nullable String message) {
		super(ibException, message);
		this.value = null;
		this.list = list;
	}
	public UnitResult(ArrayList<T> list) {
		super(false, null);
		this.value = null;
		this.list = list;
	}
}