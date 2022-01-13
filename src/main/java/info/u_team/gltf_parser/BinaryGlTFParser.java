package info.u_team.gltf_parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import info.u_team.gltf_parser.generated.gltf.Buffer;
import info.u_team.gltf_parser.generated.gltf.BufferView;
import info.u_team.gltf_parser.generated.gltf.GlTF;
import info.u_team.gltf_parser.generated.gltf.Image;

/**
 * Implementation of {@link GlTFParser} for binary (.glb) files
 * 
 * @author MrTroble
 * @version 1.0.0
 */
public class BinaryGlTFParser extends GlTFParser {
	
	private static final int GLTF_MAGIC_HEADER = 0x46546C67;
	private static final int GLTF_SUPPORTED_VERSION = 2;
	private static final int GLTF_CHUNK_BINARY = 0x004E4942;
	private static final int GLTF_CHUNK_JSON = 0x4E4F534A;
	
	private byte[] binaryData;
	
	/**
	 * Creates a new binary parser for gltf
	 * 
	 * @param data data the data to parse
	 * @param offset the offset by which data is offset
	 * @param length the maximum length to take form the input
	 * @see GlTFParser#GLTFParser(byte[], int, int)
	 */
	protected BinaryGlTFParser(byte[] data, int offset, int lenght) {
		super(data, offset, lenght);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GlTF parse() throws IOException, GlTFParseException {
		data.order(ByteOrder.LITTLE_ENDIAN);
		final int magic = data.getInt();
		if (magic != GLTF_MAGIC_HEADER)
			throw new GlTFParseException("Header magic does not match 'gltf' string!");
		
		final int version = data.getInt();
		if (version != GLTF_SUPPORTED_VERSION)
			throw new GlTFParseException("Version does not match, only '2' is supported!");
		
		final int length = data.getInt();
		if (length != data.limit())
			throw new GlTFParseException("Gltf header length does not match the parameter bLength!");
		
		final int chunkLengthJson = data.getInt();
		final int chunkTypeJson = data.getInt();
		final byte[] chunkDataJson = new byte[chunkLengthJson];
		data.get(chunkDataJson);
		if (chunkTypeJson != GLTF_CHUNK_JSON)
			throw new GlTFParseException("First chunk is not a json chunk!");
		final GlTF gltf = fromJson(chunkDataJson, 0, chunkDataJson.length).parse();
		
		final int chunkLengthBin = data.getInt();
		final int chunkTypeBin = data.getInt();
		binaryData = new byte[chunkLengthBin];
		data.get(binaryData);
		if (chunkTypeBin != GLTF_CHUNK_BINARY)
			throw new GlTFParseException("Second chunk is not a binary chunk!");
		
		final Buffer binBuffer = gltf.getBuffers().get(0);
		if (binBuffer.getUri() != null)
			throw new GlTFParseException("Buffer not a internal binary buffer!");
		
		this.gltf = gltf;
		return gltf;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ByteBuffer getData(Buffer buffer) {
		return ByteBuffer.wrap(binaryData);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ByteBuffer getData(BufferView bufferView) {
		final int bufferIndex = ((Number) bufferView.getBuffer()).intValue();
		if (bufferIndex != 0)
			throw new UnsupportedOperationException("Index other then 0 not allowed in binary mode!");
		final ByteBuffer data = ByteBuffer.wrap(binaryData);
		data.position(data.position() + bufferView.getByteOffset());
		data.limit(data.position() + bufferView.getByteLength());
		return data;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ByteBuffer getData(Image image) {
		final int index = ((Number) image.getBufferView()).intValue();
		return getData(gltf.getBufferViews().get(index));
	}
	
	@Override
	public void close() throws Exception {
		binaryData = null;
	}
}
