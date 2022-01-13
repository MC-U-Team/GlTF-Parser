package info.u_team.gltf_parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;

import info.u_team.gltf_parser.generated.gltf.Buffer;
import info.u_team.gltf_parser.generated.gltf.GlTF;
import info.u_team.gltf_parser.generated.gltf.Image;

public class JsonGLTFParser extends GLTFParser {
	
	private static final Gson GSON = new GsonBuilder().create();
	
	private static final String GLTF_SUPPORTED_VERSION = "2.0";
	private static final String IMAGE_BASE64_START = "data:image/png;base64,";
	
	public JsonGLTFParser(byte[] data) {
		super(data);
	}
	
	public JsonGLTFParser(byte[] data, int offset, int lenght) {
		super(data, offset, lenght);
	}
	
	@Override
	public GlTF parse() throws IOException, GLTFParseException {
		final GlTF gltf;
		
		try (final Reader reader = new InputStreamReader(new ByteArrayInputStream(buffer.array(), buffer.arrayOffset(), buffer.limit()), StandardCharsets.UTF_8)) {
			gltf = GSON.fromJson(reader, GlTF.class);
		} catch (JsonParseException ex) {
			throw new GLTFParseException("Could not parse gltf json", ex);
		}
		
		@SuppressWarnings("unchecked")
		final LinkedTreeMap<String, String> asset = (LinkedTreeMap<String, String>) gltf.getAsset();
		
		if (!GLTF_SUPPORTED_VERSION.equals(asset.get("version")))
			throw new GLTFParseException("Version does not match, only '2.0' is supported");
		
		this.gltf = gltf;
		return gltf;
	}
	
	@Override
	public ByteBuffer getData(Buffer buffer) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ByteBuffer getData(Image image) {
		final String uri = image.getUri();
		if (uri != null && uri.startsWith(IMAGE_BASE64_START)) {
			return decodeBase64(uri.substring(IMAGE_BASE64_START.length()));
		} else {
			return null; // TODO
		}
	}
	
	private ByteBuffer decodeBase64(String base64) {
		return ByteBuffer.wrap(Base64.getDecoder().decode(base64));
	}
	
	@Override
	public void close() throws Exception {
		
	}
}
