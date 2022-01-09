package info.u_team.gltf_parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import info.u_team.gltf_parser.generated.gltf.GlTF;

/**
 * Main entry point for pasing gltf files
 */
public class GLTFParser {
	
	private static final Gson GSON = new GsonBuilder().create();
	
	/**
	 * Reads a gltf json file from the bytes and tries to parse it to a {@link GlTF} object.
	 * 
	 * @param data The byte array data
	 * @return A gltf model
	 * @throws IOException When byte array could not be read
	 * @throws JsonParseException When json could not be parsed
	 */
	public static final GlTF parseJson(final byte data[]) throws IOException, JsonParseException {
		return parseJson(data, 0, data.length);
	}
	
	/**
	 * Reads a gltf json file from the bytes and tries to parse it to a {@link GlTF} object.
	 * 
	 * @param data The byte array data
	 * @param offset The offset in the byte array
	 * @param lenght The length to read from the byte array
	 * @return A gltf model
	 * @throws IOException When byte array could not be read
	 * @throws JsonParseException When json could not be parsed
	 */
	public static final GlTF parseJson(final byte data[], final int offset, final int lenght) throws IOException, JsonParseException {
		try (final Reader reader = new InputStreamReader(new ByteArrayInputStream(data, offset, lenght), StandardCharsets.UTF_8)) {
			final GlTF gltf = GSON.fromJson(reader, GlTF.class);
			
			return gltf;
		}
	}
	
}
