package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> dog = Class.forName("test.Dog");
        Constructor<?> pConstructor = dog.getDeclaredConstructor(int.class);
        pConstructor.setAccessible(true);
        Dog o = (Dog)pConstructor.newInstance(3);
        Field name = dog.getDeclaredField("name");
        Field age = dog.getDeclaredField("age");
        age.setAccessible(true);
        name.setAccessible(true);
        name.set(o,"BeiBei");
        Method showLove = dog.getDeclaredMethod("showLove");
        showLove.setAccessible(true);
        showLove.invoke(o);
//        Object o1 = age.get(o);
//        System.out.println(o1);
        System.out.println((String) name.get(o));
    }
}
