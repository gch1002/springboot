package com.reflection.reflectTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) throws Exception {
       /* Person person = new Person();
        person.setName("张三");
        person.setAge(11);
        System.out.println(person);*/


        System.out.println("*************通过反射可以调用私有方法  属性 以及私有构造器***************");
        Class p = Person.class;  //Person.class 是  Class的一个对象
        Constructor<Person> constructor = p.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        Person person1 = (Person) constructor.newInstance("李四", 11);
        System.out.println(person1);

        Field name = p.getDeclaredField("name");
        name.setAccessible(true);
        name.set(person1, "王五");
        System.out.println(person1);
        System.out.println("-----------------------------------");
        Person person = new Person();

        String str = "name";
        Field name1 = p.getDeclaredField(str);
        name1.setAccessible(true);
        name1.set(person, "赵六");
        System.out.println(person);


        System.out.println("-----------------------------------");

        Method show = p.getDeclaredMethod("show", String.class);
        show.setAccessible(true);
        Object aaaa = show.invoke(person1, "aaaa");  //相当于person1.show("aaaa")
        System.out.println(aaaa);

        System.out.println("******获取Class的四种方式*******");
        //方式一   运行时类的属性 .class
        Class clazz1 = Person.class;
        System.out.println(clazz1);
        //方式二    通过运行时类的对象  调用getClass()
        Person p1 = new Person();
        Class clazz2 = p1.getClass();
        System.out.println(clazz2);

        //方式三   通过Class的静态方法   forName(String classPath)                     用得最多
        Class clazz3 = Class.forName("com.gch.demo.reflectTest.Person");
        System.out.println(clazz3);

        System.out.println(clazz1 == clazz2);
        System.out.println(clazz1 == clazz3);

        //方式四    通过类的加载器  classLoader
        ClassLoader classLoader = Test.class.getClassLoader();
        Class clazz4 = classLoader.loadClass("com.gch.demo.reflectTest.Person");
        System.out.println(clazz4);
        System.out.println(clazz1 == clazz4);


    }


}
