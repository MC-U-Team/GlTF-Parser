package info.u_team.gltf_parser;

import java.io.IOException;
import java.nio.ByteBuffer;

import info.u_team.gltf_parser.generated.gltf.Accessor;
import info.u_team.gltf_parser.generated.gltf.Buffer;
import info.u_team.gltf_parser.generated.gltf.BufferView;
import info.u_team.gltf_parser.generated.gltf.GlTF;
import info.u_team.gltf_parser.generated.gltf.Image;

/**
 * Main entry point for pasing gltf files
 */
public abstract class GLTFParser implements AutoCloseable {
	
	protected GlTF gltf;
	protected final ByteBuffer buffer;
	
	public GLTFParser(byte[] data) {
		this(data, 0, data.length);
	}
	
	public GLTFParser(byte[] data, int offset, int lenght) {
		buffer = ByteBuffer.wrap(data, offset, lenght);
	}
	
	public abstract GlTF parse() throws IOException, GLTFParseException;
	
	public abstract ByteBuffer getData(Buffer buffer);
	
	public ByteBuffer getData(BufferView bufferView) {
		final int bufferIndex = (Integer) bufferView.getBuffer();
		final Buffer buffer = gltf.getBuffers().get(bufferIndex);
		final ByteBuffer data = getData(buffer);
		data.position(data.position() + bufferView.getByteOffset());
		data.limit(bufferView.getByteLength());
		return data;
	}
	
	public ByteBuffer getData(Accessor accessor) {
		final int bufferIndex = (Integer) accessor.getBufferView();
		final BufferView buffer = gltf.getBufferViews().get(bufferIndex);
		final ByteBuffer byteBuffer = getData(buffer);
		byteBuffer.position(byteBuffer.position() + accessor.getByteOffset());
		return byteBuffer;
	}
	
	public ByteBuffer getData(Image image) {
		final int index = (int) image.getBufferView();
		return getData(gltf.getBufferViews().get(index));
	}
	
	/**
	 * Reads a gltf json file from the bytes and tries to parse it to a {@link GlTF} object.
	 * 
	 * @param data The byte array data
	 * @param offset The offset in the byte array
	 * @param lenght The length to read from the byte array
	 * @return A gltf model
	 */
	public static JsonGLTFParser fromJson(final byte data[], final int offset, final int lenght) {
		return new JsonGLTFParser(data, offset, lenght);
	}
	
	/**
	 * Reads a glb binary gltf file from the bytes and tries to parse it to a {@link GlTF} object.
	 * 
	 * @param data The byte array data
	 * @param offset The offset in the byte array
	 * @param lenght The length to read from the byte array
	 * @return A gltf model
	 */
	public static BinaryGLTFParser fromBinary(final byte data[], final int offset, final int lenght) {
		return new BinaryGLTFParser(data, offset, lenght);
	}
	
}
