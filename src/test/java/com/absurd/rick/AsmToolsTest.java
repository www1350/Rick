package com.absurd.rick;

import com.absurd.rick.service.impl.CarServiceImpl;
import com.absurd.rick.util.UUIDUtil;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.junit.Test;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by wangwenwei on 2017/8/23.
 */
public class AsmToolsTest {

    @Test
    public void getMethod(){
        Method[] methods = CarServiceImpl.class.getMethods();
        LocalVariableTableParameterNameDiscoverer local=new LocalVariableTableParameterNameDiscoverer();
        String[] params=local.getParameterNames( methods[0]);
        for(String param: params){
            System.out.println(param);
        }
    }

    @Test
    public void javassistInit(){

        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get("com.absurd.rick.model.Car");
            cc.setSuperclass(pool.get("com.absurd.rick.model.User"));
            byte[] b = cc.toBytecode();
            Class aClass = cc.toClass();
            cc.writeFile();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void javassistInsertMethod(){
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = null;
        try {
            cc = cp.get("com.absurd.rick.util.UUIDUtil");
            CtMethod m = cc.getDeclaredMethod("getID");
            m.insertBefore("{ System.out.println(\"id:\"); }");
            Class c = cc.toClass();
            UUIDUtil h = (UUIDUtil)c.newInstance();
            System.out.println( h.getID());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void javassistMakeAClass(){

        try {
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(this.getClass()));
            CtClass ctClass =  pool.makeClass("Abc");
            ctClass.writeFile();
            CtClass makeInterface = pool.makeInterface("ccc");
            makeInterface.writeFile();

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void javassistMakeAClassWithRemoteLoader(){
        ClassPool pool = ClassPool.getDefault();
        ClassPath cp = new URLClassPath("www.javassist.org", 80, "/java/", "org.javassist.");
        pool.insertClassPath(cp);
        CtClass ctClass =  pool.makeClass("Abc");
        try {
            ctClass.writeFile();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void javassistGetInfo() throws Exception{
        Class<?> clazz = Class.forName("com.absurd.rick.service.impl.CarServiceImpl");
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get(clazz.getName());

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method mt:declaredMethods) {
            String modifier = Modifier.toString(mt.getModifiers());
            Class<?> returnType = mt.getReturnType();
            String name = mt.getName();
            Class<?>[] parameterTypes = mt.getParameterTypes();

            System.out.print("\n"+modifier+" "+returnType.getName()+" "+name+" (");


            //CtMethod[] declaredMethods1 = cc.getDeclaredMethods();
            CtMethod ctm = cc.getDeclaredMethod(name);
            MethodInfo methodInfo = ctm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attribute = (LocalVariableAttribute)codeAttribute.getAttribute(LocalVariableAttribute.tag);
            int pos = Modifier.isStatic(ctm.getModifiers()) ? 0 : 1;
            for (int i=0;i<ctm.getParameterTypes().length;i++) {
                System.out.print(parameterTypes[i]+" "+attribute.variableName(i+pos));
                if (i<ctm.getParameterTypes().length-1) {
                    System.out.print(",");
                }
            }

            System.out.print(")");

            Class<?>[] exceptionTypes = mt.getExceptionTypes();
            if (exceptionTypes.length>0) {
                System.out.print(" throws ");
                int j=0;
                for (Class<?> cl:exceptionTypes) {
                    System.out.print(cl.getName());
                    if (j<exceptionTypes.length-1) {
                        System.out.print(",");
                    }
                    j++;
                }
            }
        }

    }
}
