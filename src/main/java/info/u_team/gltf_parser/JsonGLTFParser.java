package info.u_team.gltf_parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import info.u_team.gltf_parser.generated.gltf.Buffer;
import info.u_team.gltf_parser.generated.gltf.GlTF;
import info.u_team.gltf_parser.generated.gltf.Image;

/**
 * Implementation of {@link GLTFParser} for json (.gltf) files
 * 
 * @author HyCraftHD
 * @version 1.0.0
 */
public class JsonGLTFParser extends GLTFParser {
	
	private static final Gson GSON = new GsonBuilder().create();
	
	private static final String GLTF_SUPPORTED_VERSION = "2.0";
	private static final String BUFFER_BASE64_START = "data:application/octet-stream;base64,";
	private static final String IMAGE_BASE64_START = "data:image/png;base64,";
	
	private final Map<Buffer, ByteBuffer> buffers = new HashMap<>();
	private final Map<Image, ByteBuffer> images = new HashMap<>();
	
	/**
	 * {@link GLTFParser#GLTFParser(byte[], int, int)}
	 */
	protected JsonGLTFParser(byte[] data, int offset, int lenght) {
		super(data, offset, lenght);
	}
	
	/**
	 * Parses the given data as json gltf files See: {@link GLTFParser#parse()}
	 */
	@Override
	public GlTF parse() throws IOException, GLTFParseException {
		final GlTF gltf;
		
		try (final Reader reader = new InputStreamReader(new ByteArrayInputStream(data.array(), data.arrayOffset(), data.limit()), StandardCharsets.UTF_8)) {
			gltf = GSON.fromJson(reader, GlTF.class);
		} catch (JsonParseException ex) {
			throw new GLTFParseException("Could not parse gltf json", ex);
		}
		
		if (!(gltf.getAsset() instanceof Map))
			throw new GLTFParseException("Could not read required asset data");
		
		@SuppressWarnings("unchecked")
		final Map<String, String> asset = (Map<String, String>) gltf.getAsset();
		if (!GLTF_SUPPORTED_VERSION.equals(asset.get("version")))
			throw new GLTFParseException("Version does not match, only '2.0' is supported");
		
		for (Buffer buffer : gltf.getBuffers()) {
			final String uri = buffer.getUri();
			if (uri != null) {
				buffers.put(buffer, decodeBase64(BUFFER_BASE64_START, uri));
			}
		}
		
		for (Image image : gltf.getImages()) {
			final String uri = image.getUri();
			if (uri != null) {
				images.put(image, decodeBase64(IMAGE_BASE64_START, uri));
			}
		}
		
		this.gltf = gltf;
		return gltf;
	}
	
	/**
	 * {@link GLTFParser#getData(Buffer)}
	 */
	@Override
	public ByteBuffer getData(Buffer buffer) {
		return buffers.get(buffer).duplicate();
	}
	
	/**
	 * {@link GLTFParser#getData(Image)}
	 */
	@Override
	public ByteBuffer getData(Image image) {
		return images.get(image).duplicate();
	}
	
	@Override
	public void close() throws Exception {
		
	}
	
	private ByteBuffer decodeBase64(String uriStart, String uri) throws GLTFParseException {
		if (uri.startsWith(uriStart)) {
			return ByteBuffer.wrap(Base64.getDecoder().decode(uri.substring(uriStart.length())));
		} else {
			throw new GLTFParseException("Expected uri to start with " + uriStart + " but it starts with " + uri);
		}
	}
}
