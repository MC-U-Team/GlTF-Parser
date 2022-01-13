package info.u_team.gltf_parser.tests;

import info.u_team.gltf_parser.GLTFParser;
import info.u_team.gltf_parser.TestModelResourceLoader;

public class SimpleCubeJsonGLTFParserTest extends SimpleCubeGLTFParserTest {
	
	private final byte[] simpleCube = TestModelResourceLoader.readModel(TestModelResourceLoader.SIMPLE_CUBE_JSON);
	
	@Override
	protected GLTFParser simpleCubeParser() {
		return GLTFParser.fromJson(simpleCube, 0, simpleCube.length);
	}
	
}
