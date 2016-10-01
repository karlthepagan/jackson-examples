package karl.codes;

import groovy.json.JsonBuilder;

import java.util.Map;

/**
 * Created by karl on 10/1/16.
 */
public class Groovy {
    public static String js(Map data) {
        return new JsonBuilder(data).toString();
    }
}
