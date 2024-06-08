# jar-hier-dump

Dumps the class hierarchy of a jar in a CSV format. For each row, the first field is the class in question, the second field is the superclass, and any additional fields are superinterfaces.

For example, running `jar-hier-dump-1.0-all.jar` on itself produces the following (abridged) output:

```
org/objectweb/asm/TypePath,java/lang/Object
org/objectweb/asm/TypeReference,java/lang/Object
org/objectweb/asm/signature/SignatureReader,java/lang/Object
org/objectweb/asm/signature/SignatureVisitor,java/lang/Object
org/objectweb/asm/signature/SignatureWriter,org/objectweb/asm/signature/SignatureVisitor
agency/highlysuspect/JarHierDump$1,org/objectweb/asm/ClassVisitor
agency/highlysuspect/JarHierDump,java/lang/Object,org/objectweb/asm/Opcodes
```

## Compiling

`./gradlew fat`

## Usage

`java -jar jar-hier-dump.jar [input]`