package fr._42.orm.models;

import fr._42.orm.annotations.OrmColumn;
import fr._42.orm.annotations.OrmColumnId;
import fr._42.orm.annotations.OrmEntity;

@OrmEntity(table = "simple_user")
public class User {
    @OrmColumnId
    private Long id;

    @OrmColumn(name = "first_name", length = 50)
    private String firstName;

    @OrmColumn(name = "age")
    private Integer age;

    public User() { }
    public User(String firstName, Integer age) {
        this.firstName = firstName;
        this.age = age;
    }

    public Long     getId()                         { return id; }
    public void     setId(Long id)                  { this.id = id; }
    public String   getFirstName()                  { return firstName; }
    public void     setFirstName(String firstName)  { this.firstName = firstName; }
    public Integer  getAge()                        { return age; }
    public void     setAge(Integer age)             { this.age = age; }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", age=" + age +
                '}';
    }
}
