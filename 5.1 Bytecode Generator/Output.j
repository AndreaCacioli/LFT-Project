.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 ldc 0
 istore 0
 ldc 1
 istore 1
L1:
 iload 0
 ldc 300
 if_icmpge L2
 iload 0
 iload 1
 iadd 
 istore 2
 iload 2
 invokestatic Output/print(I)V
 iload 1
 istore 0
 iload 2
 istore 1
 goto L1
L2:
 ldc 0
 istore 0
 ldc 0
 istore 1
L3:
 iload 0
 ldc 5
 if_icmpge L4
 ldc 0
 istore 1
L5:
 iload 1
 ldc 3
 if_icmpge L6
 iload 1
 invokestatic Output/print(I)V
 iload 1
 ldc 1
 iadd 
 istore 1
 goto L5
L6:
 ldc 1
 iload 0
 iadd 
 istore 0
 goto L3
L4:
 ldc 11
 ldc 10
 isub 
 istore 0
 ldc 11
 istore 1
 iload 0
 iload 1
 if_icmple L8
 ldc 69
 istore 0
 iload 0
 invokestatic Output/print(I)V
 goto L7
L8:
 iload 0
 iload 1
 if_icmpne L9
 invokestatic Output/read()I
 istore 0
 iload 0
 invokestatic Output/print(I)V
 goto L7
L9:
 iload 0
 invokestatic Output/print(I)V
L7:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

