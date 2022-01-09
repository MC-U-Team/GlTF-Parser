package info.u_team.gltf_parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import info.u_team.gltf_parser.generated.gltf.GlTF;

public class GLTFParserTest {
	
	@Test
	public void testParseDoNotThrowException() throws Exception {
		final byte[] bytes = ModelResourceLoader.readModel(ModelResourceLoader.SIMPLE_CUBE_JSON);
		final GlTF gltf = GLTFParser.fromJson(bytes, 0, bytes.length).parse();
		
		assertNotNull(gltf);
	}
	
	@Test
	public void testParseBinaryDoNotThrowException() throws Exception {
		final byte[] bytes = ModelResourceLoader.readModel(ModelResourceLoader.SIMPLE_CUBE_BIN);
		final GlTF gltf = GLTFParser.fromBinary(bytes, 0, bytes.length).parse();
		
		assertNotNull(gltf);
	}

	
}
