package info.u_team.gltf_parser;

public class GlTFParseException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public GlTFParseException() {
	}
	
	public GlTFParseException(String message) {
		super(message);
	}
	
	public GlTFParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
