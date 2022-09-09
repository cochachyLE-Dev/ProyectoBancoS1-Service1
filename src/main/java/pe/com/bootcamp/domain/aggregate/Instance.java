package pe.com.bootcamp.domain.aggregate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Instance {
	private String id;
	private String name;
	private String pageUrl;
	private String ipAddress;
	private int port;
	private String Status;
}
