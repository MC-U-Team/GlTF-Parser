package info.u_team.gltf_parser.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import info.u_team.gltf_parser.GLTFParser;
import info.u_team.gltf_parser.generated.gltf.GlTF;

public abstract class GLTFParserTest {
	
	protected abstract GLTFParser simpleCubeParser();
	
	@Test
	public void testParseSimpleCubeDoNotThrowException() {
		final GLTFParser parser = simpleCubeParser();
		final GlTF gltf = assertDoesNotThrow(parser::parse);
		
		assertNotNull(gltf);
	}
	
}
