public class Person{
    String name;
    int age;
    boolean man;

    public boolean isMan() {
        return man;
    }

    public Person(String name, int age, boolean man) {
        this.name = name;
        this.age = age;
        this.man = man;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", man=" + man +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}