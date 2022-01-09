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
	
	public abstract GlTF parse() throws IOException, GLTFParseException;
	
	public abstract ByteBuffer getData(Buffer buffer);
	
	public ByteBuffer getData(BufferView bufferView) {
		throw new UnsupportedOperationException();
	}
	
	public ByteBuffer getData(Accessor accessor) {
		throw new UnsupportedOperationException();
	}
	
	public ByteBuffer getData(Image image) {
		throw new UnsupportedOperationException();
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
