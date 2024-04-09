package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    public static String getRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return  "learnqa" + timestamp + "@example.com";
    }

    public static Map<String,String> getRegistrationDate(){
        Map<String,String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password","123");
        data.put("username","learnqa");
        data.put("firstName","learnqa");
        data.put("lastName", "learnqa");

        return data;
    }

    public static  Map<String,String> getRegistrationDate(Map<String,String> nonDefaultValues){
        Map<String,String> defaultValues = DataGenerator.getRegistrationDate();
        Map<String,String> userDate = new HashMap<>();
        String[] keys = {"email","password","username","firstName","lastName"};
        for (String key : keys){
            if (nonDefaultValues.containsKey(key)){
                userDate.put(key,nonDefaultValues.get(key));
            } else {
                userDate.put(key, defaultValues.get(key));
            }
        }
        return userDate;
    }

    public static  Map<String,String> getInvalidRegistrationDate(String keyToDelete){
        Map<String,String> invalidUserDate =  DataGenerator.getRegistrationDate();
            if (invalidUserDate.containsKey(keyToDelete)){
                invalidUserDate.remove(keyToDelete);
            }
        return invalidUserDate;
    }

}
