import com.study.es.util.HttpContextUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 李坡
 * @date: 2018/7/5 17:25
 * @since 1.0
 */
public class EntityTest {
    private final String url = TestConstant.URI + "/city";

    @Test
    public void deleteIndex() {
        String myurl = this.url + "/deleteIndex";
        System.out.println(myurl);

        Map<String, String> map = new HashMap<>();

        String get = HttpContextUtils.doPost(myurl, map);
        System.out.println(get);
    }

    @Test
    public void add() {
        String myurl = this.url + "/add";
        System.out.println(myurl);

        Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put("name", "北京2");

        String get = HttpContextUtils.doPost(myurl, map);
        System.out.println(get);
    }

    @Test
    public void get() {
        String myurl = this.url + "/get";
        System.out.println(myurl);

        Map<String, String> map = new HashMap<>();
        map.put("id", "1");

        String get = HttpContextUtils.doPost(myurl, map);
        System.out.println(get);
    }

    @Test
    public void batchAdd() {
        String myurl = this.url + "/batchAdd";
        System.out.println(myurl);

        Map<String, String> map = new HashMap<>();

        String get = HttpContextUtils.doPost(myurl, map);
        System.out.println(get);
    }

    @Test
    public void searchAll() {
        String myurl = this.url + "/searchAll";
        System.out.println(myurl);

        Map<String, String> map = new HashMap<>();

        String get = HttpContextUtils.doPost(myurl, map);
        System.out.println(get);
    }

    @Test
    public void searchEntity() {
        String myurl = this.url + "/searchEntity";
        System.out.println(myurl);

        Map<String, String> map = new HashMap<>();
        map.put("name", "");

        String get = HttpContextUtils.doPost(myurl, map);
        System.out.println(get);
    }

    @Test
    public void queryStringQuery() {
        String myurl = this.url + "/queryStringQuery";
        System.out.println(myurl);

        Map<String, String> map = new HashMap<>();
        map.put("queryString", "杭州");

        String get = HttpContextUtils.doPost(myurl, map);
        System.out.println(get);
    }

    @Test
    public void aggregationQuery() {
        String myurl = this.url + "/aggregationQuery";
        System.out.println(myurl);

        Map<String, String> map = new HashMap<>();

        String get = HttpContextUtils.doPost(myurl, map);
        System.out.println(get);
    }


}
