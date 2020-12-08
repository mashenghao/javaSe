package jvm.javaComplier;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.util.Arrays;

/**
 * 实现java运行时的动态编译
 *
 * @author: mahao
 * @date: 2020/8/7
 */
public class MainClass {

    public static void main(String[] args) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        
        JavaCompiler.CompilationTask result =
                compiler.getTask(null, null, null, null, null, Arrays.asList(null));
    }
}
