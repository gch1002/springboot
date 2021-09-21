package com.reflection.reflectTest;

public class StaticInnerClassDemo {

    private static class StaticInnerClass {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "StaticInnerClass{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        StaticInnerClass staticInnerClass = new StaticInnerClass();
        staticInnerClass.setName("aaaa");
        test(staticInnerClass);


    }

    public static void test(StaticInnerClass staticInnerClass) {
        System.out.println(staticInnerClass);
    }
}