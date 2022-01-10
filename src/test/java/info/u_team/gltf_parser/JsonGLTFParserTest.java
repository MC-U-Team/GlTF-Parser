package info.u_team.gltf_parser;

public class JsonGLTFParserTest extends GLTFParserTest {
	
	private final byte[] simpleCube = TestModelResourceLoader.readModel(TestModelResourceLoader.SIMPLE_CUBE_JSON);
	
	@Override
	protected GLTFParser simpleCubeParser() {
		return GLTFParser.fromJson(simpleCube, 0, simpleCube.length);
	}
	
}
