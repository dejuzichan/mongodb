package mongondb.com;

import org.bson.types.ObjectId;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private ObjectId _id;
    private String name;
    private String sex;
    private int age;

    public ObjectId get_id() {
        return _id;
    }
    public void set_id(ObjectId _id) {
        this._id = _id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}
