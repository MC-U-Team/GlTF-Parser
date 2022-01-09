package info.u_team.gltf_parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;

import info.u_team.gltf_parser.generated.gltf.Accessor;
import info.u_team.gltf_parser.generated.gltf.Buffer;
import info.u_team.gltf_parser.generated.gltf.BufferView;
import info.u_team.gltf_parser.generated.gltf.GlTF;

/**
 * Main entry point for pasing gltf files
 */
public class GLTFParser {

	private static final int GLTF_MAGIC_HEADER = 0x46546C67;
	private static final int GLTF_SUPPORTED_VERSION = 2;
	private static final int GLTF_CHUNK_BINARY = 0x004E4942;
	private static final int GLTF_CHUNK_JSON = 0x4E4F534A;

	private static final HashMap<Buffer, byte[]> GLTF_TO_BINARY_DATA = new HashMap<Buffer, byte[]>();

	/**
	 * Reads a glb binary gltf file from the bytes and tries to parse it to a
	 * {@link GlTF} object.
	 * 
	 * @param data   The byte array data
	 * @param offset The offset in the byte array
	 * @param lenght The length to read from the byte array
	 * @return A gltf model
	 * @throws IOException        When byte array could not be read
	 * @throws JsonParseException When json could not be parsed
	 */
	public static GlTF parseBinary(final byte[] data, final int bLength, final int offset)
			throws IOException, JsonParseException {
		final ByteBuffer buffer = ByteBuffer.wrap(data, offset, bLength);

		final int magic = buffer.getInt();
		if (magic != GLTF_MAGIC_HEADER)
			throw new IOException("Header magic does not match 'gltf' string!");

		final int version = buffer.getInt();
		if (version != GLTF_SUPPORTED_VERSION)
			throw new IOException("Version does not match, only '2' is supported!");

		final int length = buffer.getInt();
		if (length != bLength)
			throw new IOException("Gltf header length does not match the parameter bLength!");

		final int chunkLengthJson = buffer.getInt();
		final int chunkTypeJson = buffer.getInt();
		final byte[] chunkDataJson = new byte[chunkLengthJson];
		buffer.get(chunkDataJson);
		if (chunkTypeJson != GLTF_CHUNK_JSON)
			throw new IOException("First chunk is not a json chunk!");
		final GlTF gltf = parseJson(chunkDataJson);

		final int chunkLengthBin = buffer.getInt();
		final int chunkTypeBin = buffer.getInt();
		final byte[] chunkDataBin = new byte[chunkLengthBin];
		buffer.get(chunkDataBin);
		if (chunkTypeBin != GLTF_CHUNK_BINARY)
			throw new IOException("Second chunk is not a binary chunk!");

		final Buffer binBuffer = gltf.getBuffers().get(0);
		if (binBuffer.getUri() != null)
			throw new IOException("Buffer not a internal binary buffer!");
		GLTF_TO_BINARY_DATA.put(binBuffer, chunkDataBin);

		return gltf;
	}

	public static final byte[] getBinaryForBuffer(final Buffer buffer) {
		return GLTF_TO_BINARY_DATA.get(buffer);
	}
	
	public static final ByteBuffer getBinaryFromBufferView(final GlTF instance, final BufferView view) {
		final int bufferIndex = (Integer)view.getBuffer();
		final Buffer buffer = instance.getBuffers().get(bufferIndex);
		final byte[] data = getBinaryForBuffer(buffer);
		return  ByteBuffer.wrap(data, view.getByteOffset(), view.getByteLength());
	}
	
	public static final ByteBuffer getBinaryFromAccessor(final GlTF instance, final Accessor accessor) {
		final int bufferIndex = (Integer)accessor.getBufferView();
		final BufferView buffer = instance.getBufferViews().get(bufferIndex);
		final ByteBuffer byteBuffer = getBinaryFromBufferView(instance, buffer);
		byteBuffer.position(byteBuffer.position() + accessor.getByteOffset());
		return byteBuffer;
	}

	private static final Gson GSON = new GsonBuilder().create();

	/**
	 * Reads a gltf json file from the bytes and tries to parse it to a {@link GlTF}
	 * object.
	 * 
	 * @param data The byte array data
	 * @return A gltf model
	 * @throws IOException        When byte array could not be read
	 * @throws JsonParseException When json could not be parsed
	 */
	public static final GlTF parseJson(final byte data[]) throws IOException, GLTFParseException {
		return parseJson(data, 0, data.length);
	}

	/**
	 * Reads a gltf json file from the bytes and tries to parse it to a {@link GlTF}
	 * object.
	 * 
	 * @param data   The byte array data
	 * @param offset The offset in the byte array
	 * @param lenght The length to read from the byte array
	 * @return A gltf model
	 * @throws IOException        When byte array could not be read
	 * @throws JsonParseException When json could not be parsed
	 */
	public static final GlTF parseJson(final byte data[], final int offset, final int lenght) throws IOException, GLTFParseException {
		try (final Reader reader = new InputStreamReader(new ByteArrayInputStream(data, offset, lenght), StandardCharsets.UTF_8)) {
			final GlTF gltf = GSON.fromJson(reader, GlTF.class);
			validateGLTF(gltf);
			return gltf;
		} catch (JsonParseException ex) {
			throw new GLTFParseException("Could not parse gltf json", ex);
		}
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

}
