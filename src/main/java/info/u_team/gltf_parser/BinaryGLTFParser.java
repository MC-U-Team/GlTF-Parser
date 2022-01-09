package info.u_team.gltf_parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import info.u_team.gltf_parser.generated.gltf.Buffer;
import info.u_team.gltf_parser.generated.gltf.GlTF;

public class BinaryGLTFParser extends GLTFParser {
	
	private static final int GLTF_MAGIC_HEADER = 0x46546C67;
	private static final int GLTF_SUPPORTED_VERSION = 2;
	private static final int GLTF_CHUNK_BINARY = 0x004E4942;
	private static final int GLTF_CHUNK_JSON = 0x4E4F534A;
	
	private final HashMap<Buffer, byte[]> gltfToBinaryData = new HashMap<Buffer, byte[]>();
	
	private final ByteBuffer buffer;
	@SuppressWarnings("unused")
	private final int offset;
	private final int lenght;
	
	public BinaryGLTFParser(byte[] data, int offset, int lenght) {
		buffer = ByteBuffer.wrap(data, offset, lenght);
		this.offset = offset;
		this.lenght = lenght;
	}
	
	@Override
	public GlTF parse() throws IOException, GLTFParseException {
		final int magic = buffer.getInt();
		if (magic != GLTF_MAGIC_HEADER)
			throw new GLTFParseException("Header magic does not match 'gltf' string!");
		
		final int version = buffer.getInt();
		if (version != GLTF_SUPPORTED_VERSION)
			throw new GLTFParseException("Version does not match, only '2' is supported!");
		
		final int length = buffer.getInt();
		if (length != this.lenght)
			throw new GLTFParseException("Gltf header length does not match the parameter bLength!");
		
		final int chunkLengthJson = buffer.getInt();
		final int chunkTypeJson = buffer.getInt();
		final byte[] chunkDataJson = new byte[chunkLengthJson];
		buffer.get(chunkDataJson);
		if (chunkTypeJson != GLTF_CHUNK_JSON)
			throw new GLTFParseException("First chunk is not a json chunk!");
		final GlTF gltf = fromJson(chunkDataJson, 0, chunkDataJson.length).parse();
		
		final int chunkLengthBin = buffer.getInt();
		final int chunkTypeBin = buffer.getInt();
		final byte[] chunkDataBin = new byte[chunkLengthBin];
		buffer.get(chunkDataBin);
		if (chunkTypeBin != GLTF_CHUNK_BINARY)
			throw new GLTFParseException("Second chunk is not a binary chunk!");
		
		final Buffer binBuffer = gltf.getBuffers().get(0);
		if (binBuffer.getUri() != null)
			throw new GLTFParseException("Buffer not a internal binary buffer!");
		gltfToBinaryData.put(binBuffer, chunkDataBin);
		
		this.gltf = gltf;
		return gltf;
	}
	
	@Override
	public ByteBuffer getData(Buffer buffer) {
		return ByteBuffer.wrap(gltfToBinaryData.get(buffer));
	}
	
	@Override
	public void close() throws Exception {
		
	}
}
