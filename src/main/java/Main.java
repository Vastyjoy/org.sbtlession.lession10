import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {


    public static void main(String[] args) throws IllegalAccessException {
        List<Person> person= new ArrayList<>();
        person.add(new Person("Павел", 40,true));
        person.add(new Person("Екатерина", 30,false));
        person.add(new Person("Максим", 21, true));
        person.add(new Person("Ангелина",40,false));
        Streams<Person> streams=(Streams<Person>) Streams.of(person);
        Map<String, Integer> mapNamePerson=streams
                .filter(Person::isMan)
                .transform(p->new Person(p.getName(),p.getAge()+30,p.isMan()))
                .filter(p->p.getAge()<70)
                .toMap(Person::getName,Person::getAge);
        for(Map.Entry<String,Integer> entry:mapNamePerson.entrySet()){
            System.out.println(entry.getKey()+" в возрасте "+ entry.getValue());
        }
    }
}
