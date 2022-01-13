package info.u_team.gltf_parser.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import info.u_team.gltf_parser.GLTFParser;
import info.u_team.gltf_parser.generated.gltf.Accessor;
import info.u_team.gltf_parser.generated.gltf.BufferView;
import info.u_team.gltf_parser.generated.gltf.GlTF;
import info.u_team.gltf_parser.generated.gltf.Image;

public abstract class SimpleCubeGLTFParserTest {
	
	protected abstract GLTFParser simpleCubeParser();
	
	@Test
	public void testParseSimpleCubeDoNotThrowException() {
		final GLTFParser parser = simpleCubeParser();
		final GlTF gltf = assertDoesNotThrow(parser::parse);
		
		assertNotNull(gltf);
	}
	
	@Test
	public void testSimpleCubeGetBuffer() {
		final GLTFParser parser = simpleCubeParser();
		final GlTF gltf = assertDoesNotThrow(parser::parse);
		
		final ByteBuffer buffer = parser.getData(gltf.getBuffers().get(0));
		
		assertNotNull(buffer);
		assertTrue(buffer.limit() > 0);
	}
	
	@Test
	public void testSimpleCubeGetBufferViews() {
		final GLTFParser parser = simpleCubeParser();
		final GlTF gltf = assertDoesNotThrow(parser::parse);
		
		for (final BufferView bufferView : gltf.getBufferViews()) {
			final ByteBuffer buffer = parser.getData(bufferView);
			
			assertNotNull(buffer);
			assertEquals(bufferView.getByteOffset(), buffer.position());
			assertEquals(bufferView.getByteLength(), buffer.limit() - buffer.position());
		}
	}
	
	@Test
	public void testSimpleCubeGetAccessors() {
		final GLTFParser parser = simpleCubeParser();
		final GlTF gltf = assertDoesNotThrow(parser::parse);
		
		for (final Accessor accessor : gltf.getAccessors()) {
			final int bufferIndex = ((Number) accessor.getBufferView()).intValue();
			final BufferView bufferView = gltf.getBufferViews().get(bufferIndex);
			final ByteBuffer buffer = parser.getData(accessor);
			
			assertNotNull(buffer);
			assertEquals(bufferView.getByteOffset() + accessor.getByteOffset(), buffer.position());
		}
	}
	
	@Test
	public void testSimpleCubeGetImages() {
		final GLTFParser parser = simpleCubeParser();
		final GlTF gltf = assertDoesNotThrow(parser::parse);
		
		for (final Image image : gltf.getImages()) {
			final ByteBuffer buffer = parser.getData(image);
			
			assertNotNull(buffer);
			assertTrue(buffer.limit() > 0);
		}
	}
}
