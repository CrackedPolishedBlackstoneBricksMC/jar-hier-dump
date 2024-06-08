package agency.highlysuspect;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarHierDump implements Opcodes {
	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.err.println("Usage: jar-hier-dump [input] [output.csv]?");
			System.exit(1);
		}
		
		Path input = Paths.get(args[0]);
		if(Files.notExists(input)) {
			System.err.println("file " + input + " does not exist");
			System.exit(1);
		}
		
		try(
			ZipInputStream zip = new ZipInputStream(new BufferedInputStream(Files.newInputStream(input)));
			PrintWriter out = new PrintWriter(System.out)
		) {
			ZipEntry entry;
			while((entry = zip.getNextEntry()) != null) {
				if(entry.getName().endsWith(".class")) {
					ClassReader reader = new ClassReader(zip);
					reader.accept(new ClassVisitor(ASM9) {
						@Override
						public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
							//superName can be null for module-info classes, as well as java/lang/Object itself
							if(superName != null) {
								out.print(name);
								out.print(',');
								out.print(superName);
								for(String itf : interfaces) {
									out.print(',');
									out.print(itf);
								}
								out.println();
							}
							
							super.visit(version, access, name, signature, superName, interfaces);
						}
					}, ClassReader.SKIP_CODE);
				}
			}
		}
	}
}