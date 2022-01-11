package info.u_team.gltf_parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import info.u_team.gltf_parser.generated.gltf.GlTF;

public class Main {
	
	public static void main(String[] args) throws IOException, GLTFParseException {
		final byte[] simpleCube = TestModelResourceLoader.readModel(TestModelResourceLoader.SIMPLE_CUBE_BIN);
		final BinaryGLTFParser parser = GLTFParser.fromBinary(simpleCube, 0, simpleCube.length);
		
		final byte[] simpleCube2 = TestModelResourceLoader.readModel(TestModelResourceLoader.SIMPLE_CUBE_JSON);
		final JsonGLTFParser parser2 = GLTFParser.fromJson(simpleCube2, 0, simpleCube2.length);
		
		final String path = "D:\\Programmieren\\Java\\Forge\\U Team\\GLTF\\Ex";
		
		Files.deleteIfExists(Paths.get(path, "bin_image.png"));
		Files.deleteIfExists(Paths.get(path, "json_image.png"));
		
		final ByteBuffer buffer = getImage(parser);
		System.out.println("___________________");
		final ByteBuffer buffer2 = getImage(parser2);
		
		final SeekableByteChannel channel = Files.newByteChannel(Paths.get(path, "bin_image.png"), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
		final SeekableByteChannel channel2 = Files.newByteChannel(Paths.get(path, "json_image.png"), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
		
		channel.write(buffer);
		channel2.write(buffer2);
		
		channel.close();
		channel2.close();
	}
	
	private static ByteBuffer getImage(GLTFParser parser) throws IOException, GLTFParseException {
		final GlTF gltf = parser.parse();
		
		System.out.println(gltf.getImages().get(0));
		
		final ByteBuffer buffer = parser.getData(gltf.getImages().get(0));
		
		System.out.println(buffer);
		System.out.println(buffer.array().length);
		
		return buffer;
	}
	
}
