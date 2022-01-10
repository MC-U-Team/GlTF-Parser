package info.u_team.gltf_parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import info.u_team.gltf_parser.generated.gltf.GlTF;

public abstract class GLTFParserTest {
	
	protected abstract GLTFParser simpleCubeParser();
	
	@Test
	public void testParseDoNotThrowException() throws Exception {
		final GLTFParser parser = simpleCubeParser();
		final GlTF gltf = assertDoesNotThrow(parser::parse);
		
		assertNotNull(gltf);
	}
	
}
