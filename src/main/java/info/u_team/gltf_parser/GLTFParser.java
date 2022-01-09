package info.u_team.gltf_parser;

import java.io.IOException;
import java.nio.ByteBuffer;

import info.u_team.gltf_parser.generated.gltf.GlTF;

/**
 * Main entry point for pasing gltf files
 */
public class GLTFParser {

	private static final int GLTF_MAGIC_HEADER = 0x46546C67;
	private static final int GLTF_SUPPORTED_VERSION = 2;
	private static final int GLTF_CHUNK_BINARY = 0x004E4942;
	private static final int GLTF_CHUNK_JSON = 0x4E4F534A;
	
	public static GlTF parseBinary(final byte[] data, final int bLength, final int offset) throws IOException {
		final ByteBuffer buffer = ByteBuffer.wrap(data, offset, bLength);
		
		final int magic = buffer.getInt();
		if(magic != GLTF_MAGIC_HEADER)
			throw new IOException("Header magic does not match 'gltf' string!");
		
		final int version = buffer.getInt();
		if(version != GLTF_SUPPORTED_VERSION)
			throw new IOException("Version does not match, only '2' is supported!");
		
		final int length = buffer.getInt();
		if(length != bLength)
			throw new IOException("Gltf header length does not match the parameter bLength!");
		
		final int chunkLengthJson = buffer.getInt();
		final int chunkTypeJson = buffer.getInt();
		final byte[] chunkDataJson = new byte[chunkLengthJson];
		buffer.get(chunkDataJson);
		if(chunkTypeJson != GLTF_CHUNK_JSON)
			throw new IOException("First chunk is not a json chunk!");

		final int chunkLengthBin = buffer.getInt();
		final int chunkTypeBin = buffer.getInt();
		final byte[] chunkDataBin = new byte[chunkLengthBin];
		buffer.get(chunkDataBin);
		if(chunkTypeBin != GLTF_CHUNK_BINARY)
			throw new IOException("Second chunk is not a binary chunk!");

		return null;
	}
	
}
