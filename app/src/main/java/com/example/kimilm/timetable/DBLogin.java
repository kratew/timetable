package com.example.kimilm.timetable;

import java.util.ArrayList;

import org.bson.Document;

import com.example.kimilm.timetable.TimeTable;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class DBLogin {
	
	public static String IP = "45.119.146.33";
    public static int Port = 27017;
    
    //dbName
    public static String dbName = "User";

    //Connect to MongoDB
    public static MongoClient mongoClient = new MongoClient(IP, Port);
    public static MongoDatabase db = mongoClient.getDatabase(dbName);
    
    //user collection
    public static MongoCollection<Document> collection = db.getCollection("user");
	
	public static void main (String [] args)
	{
        String id = "kratew";
        String pwd = "1234";
		
        //db User Search
		Document document = searchUser(id, pwd);
		
		if (document != null)
			System.out.println(document.toJson());
		else
			System.out.println("no User!");
		
		Friend fri = parseToFriend(document);
		
		System.out.println(fri.Id);
		System.out.println(fri.Pw);
		System.out.println(fri.Name);
		System.out.println(fri.frList);
		
		//wrong pwd
		pwd = "123";
		
		document = searchUser(id, pwd);
		
		if (document != null)
			System.out.println(document.toJson());
		else
			System.out.println("no User!");
		
		mongoClient.close();
	}
	
	//Search User
	public static Document searchUser (String id, String pwd)
	{
        // id && pwd make query
        ArrayList<BasicDBObject> queryObj = new ArrayList<>();
        queryObj.add(new BasicDBObject("_id", id));
        queryObj.add(new BasicDBObject("pwd", pwd));
        
        BasicDBObject query = new BasicDBObject("$and", queryObj);
        
        // query to db
        MongoCursor<Document> cursor = collection.find(query).iterator();
        
        if (cursor.hasNext())
        {
        		return new Document(cursor.next());
        }
        
        return null;
	}
	
	public static Friend parseToFriend (Document document)
	{
		String id = document.getString("_id");
		String pwd = document.getString("pwd");
		String name = document.getString("name");
		TimeTable timeTable = getTable(document.getString("timetable"));
		ArrayList<String> fList = toSubString(document.get("f_id").toString());
		
		return new Friend(id, pwd, name, timeTable, fList);
	}
	
	public static ArrayList<String> toSubString (String str)
	{
		ArrayList strArray = new ArrayList<>();
		
		str = str.replace("[", "");
		str = str.replace("]", "");
		
		String [] subStr = str.split(", ");
		
		for (String token : subStr)
		{
			strArray.add(token);
		}
		
		return strArray;
	}
	
	public static TimeTable getTable (String str)
	{
		return new TimeTable();
	}
}
