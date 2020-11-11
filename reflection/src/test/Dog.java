package test;

public class Dog {
    private String name;
    private int age;
    private void showLove(){
        System.out.println("Show love to Owner");
    }

    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Dog() {
    }

    private Dog(int age) {
        this.age = age;
    }
}
