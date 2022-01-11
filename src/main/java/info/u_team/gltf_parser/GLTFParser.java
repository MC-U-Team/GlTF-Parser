package info.u_team.gltf_parser;

import java.io.IOException;
import java.nio.ByteBuffer;

import info.u_team.gltf_parser.generated.gltf.Accessor;
import info.u_team.gltf_parser.generated.gltf.Buffer;
import info.u_team.gltf_parser.generated.gltf.BufferView;
import info.u_team.gltf_parser.generated.gltf.GlTF;
import info.u_team.gltf_parser.generated.gltf.Image;

/**
 * Main entry point for parsing a GLTF file
 * 
 * @author HyCraftHD, MrTroble
 * @version 1.0.0
 */
public abstract class GLTFParser implements AutoCloseable {
	
	protected GlTF gltf;
	protected final ByteBuffer buffer;
	
	/**
	 * Creates a new parser instance for the input data
	 * 
	 * @param data the data to parse
	 */
	public GLTFParser(byte[] data) {
		this(data, 0, data.length);
	}
	
	/**
	 * Creates a new parser instance for the input data The input data is offset by the offset input and a maximum of length
	 * bytes are being used for the input
	 * 
	 * @param data data the data to parse
	 * @param offset the offset by which data is offset
	 * @param length the maximum length to take form the input
	 */
	public GLTFParser(byte[] data, int offset, int length) {
		buffer = ByteBuffer.wrap(data, offset, length);
	}
	
	/**
	 * Parses the input data as gltf resource
	 * 
	 * @return {@link GlTF} the gltf structure
	 * @throws IOException
	 * @throws GLTFParseException
	 */
	public abstract GlTF parse() throws IOException, GLTFParseException;
	
	/**
	 * Returns the binary data of a given {@link Buffer} object
	 * 
	 * @param buffer the {@link Buffer} from which the data should be returned
	 * @return the {@link ByteBuffer} wrapping the data
	 */
	public abstract ByteBuffer getData(Buffer buffer);
	
	/**
	 * Returns the binary data of a given {@link BufferView} object
	 * 
	 * @param bufferView the {@link BufferView} from which the data should be returned
	 * @return the {@link ByteBuffer} wrapping the data
	 */
	public ByteBuffer getData(BufferView bufferView) {
		final int bufferIndex = ((Number) bufferView.getBuffer()).intValue();
		final Buffer buffer = gltf.getBuffers().get(bufferIndex);
		final ByteBuffer data = getData(buffer);
		data.position(data.position() + bufferView.getByteOffset());
		data.limit(data.position() + bufferView.getByteLength());
		return data;
	}
	
	/**
	 * Returns the binary data of a given {@link Accessor} object
	 * 
	 * @param accessor the {@link Accessor} from which the data should be returned
	 * @return the {@link ByteBuffer} wrapping the data
	 */
	public ByteBuffer getData(Accessor accessor) {
		final int bufferIndex = ((Number) accessor.getBufferView()).intValue();
		final BufferView buffer = gltf.getBufferViews().get(bufferIndex);
		final ByteBuffer byteBuffer = getData(buffer);
		byteBuffer.position(byteBuffer.position() + accessor.getByteOffset());
		return byteBuffer;
	}
	
	/**
	 * Returns the binary data of a given {@link Image} object
	 * 
	 * @param image the {@link Image} from which the data should be returned
	 * @return the {@link ByteBuffer} wrapping the data
	 */
	public ByteBuffer getData(Image image) {
		final int index = ((Number) image.getBufferView()).intValue();
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
