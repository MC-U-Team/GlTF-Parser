package info.u_team.gltf_parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;

import info.u_team.gltf_parser.generated.gltf.Buffer;
import info.u_team.gltf_parser.generated.gltf.GlTF;

public class JsonGLTFParser extends GLTFParser {

	private static final Gson GSON = new GsonBuilder().create();

	public JsonGLTFParser(byte[] data) {
		super(data);
	}
	
	public JsonGLTFParser(byte[] data, int offset, int lenght) {
		super(data, offset, lenght);
	}
	
	private static void validateGLTF(GlTF gltf) throws GLTFParseException {
		if (!(gltf.getAsset() instanceof LinkedTreeMap)) {
			throw new GLTFParseException("Could not read asset values");
		}
		
		@SuppressWarnings("unchecked")
		final LinkedTreeMap<String, String> asset = (LinkedTreeMap<String, String>) gltf.getAsset();
		
		if (!"2.0".equals(asset.get("version"))) {
			throw new GLTFParseException("Only models with 2.0 version are supported.");
		}
	}
	
	@Override
	public void close() throws Exception {
	}
	
	@Override
	public GlTF parse() throws IOException, GLTFParseException {
		try (final Reader reader = new InputStreamReader(new ByteArrayInputStream(buffer.array(), buffer.arrayOffset(), buffer.limit()), StandardCharsets.UTF_8)) {
			final GlTF gltf = GSON.fromJson(reader, GlTF.class);
			validateGLTF(gltf);
			
			this.gltf = gltf;
			return gltf;
		} catch (JsonParseException ex) {
			throw new GLTFParseException("Could not parse gltf json", ex);
		}
	}
	
	@Override
	public ByteBuffer getData(Buffer buffer) {
		throw new UnsupportedOperationException();
	}
	
}
