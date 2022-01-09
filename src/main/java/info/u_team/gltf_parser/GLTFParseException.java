package info.u_team.gltf_parser;

public class GLTFParseException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public GLTFParseException() {
	}
	
	public GLTFParseException(String message) {
		super(message);
	}
	
	public GLTFParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
