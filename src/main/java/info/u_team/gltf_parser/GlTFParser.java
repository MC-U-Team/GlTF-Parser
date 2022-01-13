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
public abstract class GlTFParser implements AutoCloseable {
	
	protected GlTF gltf;
	protected final ByteBuffer data;
	
	/**
	 * Creates a new parser instance for the input data. The input data is offset by the offset input and a maximum of
	 * length bytes are being used for the input
	 * 
	 * @param data The data to parse
	 * @param offset The offset by which data is offset
	 * @param length The maximum length to take form the input
	 */
	public GlTFParser(byte[] data, int offset, int length) {
		this.data = ByteBuffer.wrap(data, offset, length);
	}
	
	/**
	 * Parses the input data as gltf resource
	 * 
	 * @return The {@link GlTF} structure
	 * @throws IOException When data could not be read
	 * @throws GlTFParseException When data could not be parsed
	 */
	public abstract GlTF parse() throws IOException, GlTFParseException;
	
	/**
	 * Returns the binary data of a given {@link Buffer} object
	 * 
	 * @param buffer The {@link Buffer} from which the data should be returned
	 * @return The {@link ByteBuffer} wrapping the data
	 */
	public abstract ByteBuffer getData(Buffer buffer);
	
	/**
	 * Returns the binary data of a given {@link BufferView} object
	 * 
	 * @param bufferView The {@link BufferView} from which the data should be returned
	 * @return The {@link ByteBuffer} wrapping the data
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
	 * @param accessor The {@link Accessor} from which the data should be returned
	 * @return The {@link ByteBuffer} wrapping the data
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
	 * @param image The {@link Image} from which the data should be returned
	 * @return The {@link ByteBuffer} wrapping the data
	 */
	public abstract ByteBuffer getData(Image image);
	
	/**
	 * Reads a gltf json file from the bytes and tries to parse it to a {@link GlTF} object.
	 * 
	 * @param data The byte array data
	 * @return A gltf model
	 * @see GlTFParser#fromJson(byte[], int, int)
	 */
	public static JsonGlTFParser fromJson(final byte data[]) {
		return new JsonGlTFParser(data, 0, data.length);
	}
	
	/**
	 * Reads a gltf json file from the bytes and tries to parse it to a {@link GlTF} object.
	 * 
	 * @param data The byte array data
	 * @param offset The offset in the byte array
	 * @param lenght The length to read from the byte array
	 * @return A gltf model
	 */
	public static JsonGlTFParser fromJson(final byte data[], final int offset, final int lenght) {
		return new JsonGlTFParser(data, offset, lenght);
	}
	
	/**
	 * Reads a glb binary gltf file from the bytes and tries to parse it to a {@link GlTF} object.
	 * 
	 * @param data The byte array data
	 * @return A gltf model
	 * @see GlTFParser#fromBinary(byte[], int, int)
	 */
	public static BinaryGlTFParser fromBinary(final byte data[]) {
		return new BinaryGlTFParser(data, 0, data.length);
	}
	
	/**
	 * Reads a glb binary gltf file from the bytes and tries to parse it to a {@link GlTF} object.
	 * 
	 * @param data The byte array data
	 * @param offset The offset in the byte array
	 * @param lenght The length to read from the byte array
	 * @return A gltf model
	 */
	public static BinaryGlTFParser fromBinary(final byte data[], final int offset, final int lenght) {
		return new BinaryGlTFParser(data, offset, lenght);
	}
	
}
