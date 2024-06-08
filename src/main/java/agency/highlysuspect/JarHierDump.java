package agency.highlysuspect;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
		
		SortedSet<Entry> entries = new TreeSet<>();
		
		try(ZipInputStream zip = new ZipInputStream(new BufferedInputStream(Files.newInputStream(input)))) {
			ZipEntry entry;
			while((entry = zip.getNextEntry()) != null) {
				if(entry.getName().endsWith(".class")) {
					ClassReader reader = new ClassReader(zip);
					reader.accept(new ClassVisitor(ASM9) {
						@Override
						public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
							//superName can be null for module-info classes, as well as java/lang/Object itself
							if(superName != null) {
								entries.add(new Entry(name, superName, interfaces));
							}
							
							super.visit(version, access, name, signature, superName, interfaces);
						}
					}, ClassReader.SKIP_CODE);
				}
			}
		}
		
		entries.forEach(Entry::print);
	}
	
	static class Entry implements Comparable<Entry> {
		public Entry(String name, String superName, String[] interfaces) {
			this.name = name;
			this.superName = superName;
			this.interfaces = interfaces;
		}
		
		final String name;
		final String superName;
		final String[] interfaces;
		
		@Override
		public int compareTo(Entry o) {
			return name.compareTo(o.name);
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Entry entry = (Entry) o;
			return name.equals(entry.name);
		}
		
		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		void print() {
			System.out.print(name);
			System.out.print(',');
			System.out.print(superName);
			for(String itf : interfaces) {
				System.out.print(',');
				System.out.print(itf);
			}
			System.out.println();
		}
		
	}
}