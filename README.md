# jar-hier-dump

Dumps the class hierarchy of a jar in a CSV format. For each row, the first field is the class in question, the second field is the superclass, and any additional fields are superinterfaces.

For example, running `jar-hier-dump-1.1-all.jar` on itself produces the following (abridged) output:

```
agency/highlysuspect/JarHierDump,java/lang/Object,org/objectweb/asm/Opcodes
agency/highlysuspect/JarHierDump$1,org/objectweb/asm/ClassVisitor
agency/highlysuspect/JarHierDump$Entry,java/lang/Object,java/lang/Comparable
org/objectweb/asm/AnnotationVisitor,java/lang/Object
org/objectweb/asm/AnnotationWriter,org/objectweb/asm/AnnotationVisitor
org/objectweb/asm/Attribute,java/lang/Object
org/objectweb/asm/Attribute$Set,java/lang/Object
```

## Compiling

`./gradlew fat`

## Usage

`java -jar jar-hier-dump.jar [input]`