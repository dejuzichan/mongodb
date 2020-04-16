package mongondb.com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

public class MongoDBTest {

    public static void main( String[] args )
    {
        try {
             //insertOneTest();  //插入单条记录
            // insertManyTest();  //插入多条记录
            //deleteOneTest();  //删除单条记录
            //deleteManyTest();  //删除多条记录
            // updateOneTest();
           // updateManyTest();
            // findTest();
            //findDocumentByLimit();
            //findDocumentBySort();
            //findDocumentByRelation();
            selectManyByConditon();
        System.out.println( "操作完成!" );
        }
        catch (Exception ex)
        {
            System.out.println( "错误："+ex.getMessage());
        }
    }


    public static void selectManyByConditon()
    {
        //获取数据库连接对象
        MongoDatabase mongoDatabase = MongoDBConnectionUtil.getAuthConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("userInfo");
        //加入查询条件
        BasicDBObject query = new BasicDBObject();
        //时间区间查询 记住如果想根据这种形式进行时间的区间查询 ，存储的时候 记得把字段存成字符串，就按yyyy-MM-dd HH:mm:ss 格式来
        //query.put("times", new BasicDBObject("$gte", "2018-06-02 12:20:00").append("$lte","2018-07-04 10:02:46"));
        //模糊查询
        // Pattern pattern = Pattern.compile("^.*1.*$", Pattern.CASE_INSENSITIVE);
        //query.put("name", pattern);
        //精确查询
        query.put("name", "张三");
        //skip 是分页查询，从第0条开始查10条数据。 Sorts是排序用的。有descending 和ascending
        MongoCursor<Document> cursor = collection
                                        .find(query)
                                        .sort(Sorts.orderBy(Sorts.descending("id")))
                                        .skip(0)
                                        .limit(5)
                                        .iterator();//
        int unm=0;
        try {
            while (cursor.hasNext()) {

                //查询出的结果转换成jsonObject,然后进行封装或者直接返回给前端处理。我这是封装成对象了
                UserInfo userInfo = JSONObject.parseObject( cursor.next().toJson().toString(),UserInfo.class);
              /*          userBehaviorLogs.setId(jsonObject.getString("id"));//id
                userBehaviorLogs.setAge(Integer.parseInt(jsonObject.getString("age")));//用户id
                userBehaviorLogs.setName(jsonObject.getString("name"));//用户名称
                userBehaviorLogs.setSex(jsonObject.getString("sex"));//参数*/
                unm++;
                //System.out.println(unm+"="+userBehaviorLogs.getTimes()+"==="+userBehaviorLogs.getId());

                System.out.println(JSON.toJSON(userInfo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

    }


    /**
     * 插入单条数据
     */
    public static void insertOneTest()
    {
        //获取数据库连接对象
        MongoDatabase mongoDatabase = MongoDBConnectionUtil.getAuthConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("userInfo");
        //要插入的数据
        Document document = new Document("name","张三")
                .append("sex", "男")
                .append("age", 18);
        //插入一个文档
        collection.insertOne(document);
    }

    /**
     * 插入多条数据
     */
    public static void insertManyTest(){
        //获取数据库连接对象
        MongoDatabase mongoDatabase = MongoDBConnectionUtil.getAuthConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("userInfo");
        //要插入的数据
        List<Document> list = new ArrayList<>();
        for(int i = 1; i <= 3; i++) {
            Document document = new Document("name", "张三"+String.valueOf(i))
                    .append("sex", "男")
                    .append("age", 18);
            list.add(document);
        }

        //插入多个文档
        collection.insertMany(list);
    }

    /**
     *  删除与筛选器匹配的单个文档
     */
    public static void deleteOneTest(){
        //获取数据库连接对象
        MongoDatabase mongoDatabase = MongoDBConnectionUtil.getAuthConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("userInfo");
        //申明删除条件
        Bson filter = Filters.eq("name","张三");
        //删除与筛选器匹配的单个文档
        DeleteResult result = collection.deleteOne(filter);
    }

    /**
     * 删除多条记录
     */
    public static void deleteManyTest(){
        //获取数据库连接对象
        MongoDatabase mongoDatabase = MongoDBConnectionUtil.getAuthConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("userInfo");
        //申明删除条件
        Bson filter = Filters.eq("age",18);
        //删除与筛选器匹配的所有文档
        collection.deleteMany(filter);
    }

    /**
     *   修改单个文档
     */
    public static void updateOneTest(){
        //获取数据库连接对象
        MongoDatabase mongoDatabase = MongoDBConnectionUtil.getAuthConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("userInfo");
        //修改过滤器
        Bson filter = Filters.eq("name", "张三1");
        //指定修改的更新文档
        Document document = new Document("$set", new Document("age", 100));
        //修改单个文档
        UpdateResult result = collection.updateOne(filter, document);
    }

    /**
     * 更新多条记录
     */
    public static void updateManyTest(){
        //获取数据库连接对象
        MongoDatabase mongoDatabase = MongoDBConnectionUtil.getAuthConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("userInfo");
        //修改过滤器
        Bson filter = Filters.eq("name", "张三1");
        //指定修改的更新文档
        Document document = new Document("$set", new Document("age", 99));
        //修改多个文档
        UpdateResult result = collection.updateMany(filter, document);
    }

    //查找集合中的所有文档
    public static void findTest(){
        //获取数据库连接对象
        MongoDatabase mongoDatabase = MongoDBConnectionUtil.getAuthConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("userInfo");
        //查找集合中的所有文档
        FindIterable findIterable = collection.find();

        Document document = (Document) findIterable.first(); //查询第一个数据
        MongoCursor cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }


    // 进行分页查询，限制查询出的数据条数
    public static void findDocumentByLimit() {
        try {
            MongoDatabase  database =  MongoDBConnectionUtil.getAuthConnect();
            MongoCollection<Document> collection = database.getCollection("userInfo");
            long totalCount =   collection.count();
            // 组装文档
            BasicDBObject searchDoc = new BasicDBObject().append("id", 1);
            // 进行查询
            FindIterable<Document> find = collection.find().sort(new BasicDBObject("_id", 1)).limit(5);
            for (Document document : find) {
                String doc =  document.toString();
                System.out.println(document.getInteger("id") + "\t" + document.getString("name") + "\t"
                        + document.getString("sex") + "\t" + document.getDate("date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按照某个条件排序查询，并限制查询的条数。 sort（'字段名',num）方法的参数num为排序的方式，1为升序，-1为降序
     */
    public static void findDocumentBySort() {
        try {
            MongoDatabase  database =  MongoDBConnectionUtil.getAuthConnect();
            MongoCollection<Document> collection = database.getCollection("userInfo");
            // 组装文档
            BasicDBObject searchDoc = new BasicDBObject().append("id", 1);
            // 查询，排序并限制条数（查询的数量太大进行排序会导致内存溢出而报错）

            FindIterable<Document> find = collection.find().sort(new Document().append("id", -1)).limit(5);
            for (Document document : find) {
                System.out.println(document.getInteger("id") + "\t" + document.getString("name") + "\t"
                        + document.getString("sex") + "\t" + document.getDate("date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 带有比较运算符的条件查询，并限制查询的条数
    public static void findDocumentByRelation() {
        try {
            MongoDatabase  database =  MongoDBConnectionUtil.getAuthConnect();
            MongoCollection<Document> collection = database.getCollection("userInfo");
            // 组装文档,将关系运算符嵌套在条件中。
            // 大于：$gt,大于等于：$gte,小于：$lt,小于等于：$lte,不等于：$ne
            BasicDBObject searchDoc = new BasicDBObject().append("id", new Document().append("$lt", 10));
            // 查询限制条数（查询的数量太大进行排序会导致内存溢出而报错）

            FindIterable<Document> find = collection.find(searchDoc).limit(5);
            for (Document document : find) {

                System.out.println(document.getInteger("id") + "\t" + document.getString("name") + "\t"
                        + document.getString("sex") + "\t" + document.getDate("date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
