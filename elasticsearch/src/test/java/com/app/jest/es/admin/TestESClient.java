package com.app.jest.es.admin;

import com.app.jest.es.client.ESClient;
import com.app.jest.es.client.ESResource;
import com.app.jest.es.client.ESUser;
import com.app.jest.es.util.ESUtil;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author yangq
 * @version 1.0
 *          <br>qing.yang@dewmobile.net</br>
 * @file TODO: file name
 * @date 14-6-27
 */
public class TestESClient extends TestCase {
    public static ESClient client;

    public static final String resource_index = "test_resource_index";
    public static final String resource_type = "test_resource_type";

    public static final String user_index = "test_user_index";
    public static final String user_type = "test_user_type";
    static {
        Map<String, Integer> hosts = new HashMap<String, Integer>();
        hosts.put("192.168.127.129", 9200);
        client = new ESClient(hosts, resource_index, user_index);
    }

    public void setUp() throws Exception {
        if (!ESUtil.indiceExists(client.getClient(), user_index)) {
            ESUtil.createIndex(client.getClient(), user_index);
            ESUtil.flushIndex(client.getClient());
        } else {
            ESUtil.deleteIndex(client.getClient(), user_index);
            ESUtil.createIndex(client.getClient(), user_index);
            ESUtil.flushIndex(client.getClient());
        }

        if (!ESUtil.indiceExists(client.getClient(), resource_index)) {
            ESUtil.createIndex(client.getClient(), resource_index);
            ESUtil.flushIndex(client.getClient());
        } else {
            ESUtil.deleteIndex(client.getClient(), resource_index);
            ESUtil.createIndex(client.getClient(), resource_index);
            ESUtil.flushIndex(client.getClient());
        }
    }

    public void tearDown() throws Exception {
        ESUtil.deleteIndex(client.getClient(), user_index);
        ESUtil.deleteIndex(client.getClient(), resource_index);
    }

    public void testSearchUserByName() throws Exception {
        ESUser[] users = new ESUser[] {
          new ESUser("123", "yang qing"),
          new ESUser("234", "qing qing"),
          new ESUser("256", "周杰伦 qing")
        };

        for (ESUser user: users) {
            ESUtil.addDoc(client.getClient(), user_index, user_type, user);
        }
        ESUtil.flushIndex(client.getClient());
        final String sng = "qing";
        List<ESUser> result = client.getUserByName(sng, 0, 30);
        Assert.assertTrue(result.size() >= 2);
        for (ESUser user: result) {
            Assert.assertTrue(user.name.contains(sng));
            ESUtil.deleteDocument(client.getClient(), user_index, user_type, user.id);
        }
    }

    public void testSearchUserById() throws Exception {
        ESUser[] users;
        users = new ESUser[] {
                new ESUser("123", "yang qing"),
                new ESUser("234", "qing qing"),
                new ESUser("256", "周杰伦 qing")
        };

        for (ESUser user: users) {
            ESUtil.addDoc(client.getClient(), user_index, user_type, user);
        }
        ESUtil.flushIndex(client.getClient());
        for (ESUser user: users) {
            ESUser result = client.getUserById(user.id);
            Assert.assertEquals(user.id, result.id);
            Assert.assertEquals(user.name, result.name);
            ESUtil.deleteDocument(client.getClient(), user_index, user_type, user.id);
        }
    }


    public void testSearchResourceByName() throws Exception {
        String[] u = {"今天天气很好", "外面没有下雨","这是什么情况"};
        int[][] tags = new int[][] {
                {1,2,3},
                {4,5,6},
                {7,8,9}
        };
        ESResource[] resources = new ESResource[] {
            new ESResource("123", u[0], tags[0]),
            new ESResource("234", u[1], tags[1]),
            new ESResource("789", u[2], tags[2])
        };

        for (ESResource resource: resources) {
            ESUtil.addDoc(client.getClient(), resource_index, resource_type, resource);
        }
        ESUtil.flushIndex(client.getClient());
        for (ESResource resource: resources) {
            List<ESResource> result = client.getResourceByName(resource.n, 0, 30);
            Assert.assertEquals(result.get(0).n, resource.n);
            ESUtil.deleteDocument(client.getClient(), resource_index, resource_type, resource.id);
        }
    }

    public void testSearchResourceByTags() throws Exception {
        String[] u = {"今天天气很好", "外面没有下雨","这是什么情况"};
        int[][] tags = new int[][] {
                {1,2,3},
                {4,5,6},
                {7,8,9}
        };
        ESResource[] resources = new ESResource[] {
                new ESResource("123", u[0], tags[0]),
                new ESResource("234", u[1], tags[1]),
                new ESResource("789", u[2], tags[2])
        };

        for (ESResource resource: resources) {
            ESUtil.addDoc(client.getClient(), resource_index, resource_type, resource);
        }
        ESUtil.flushIndex(client.getClient());
        for (ESResource resource: resources) {
            List<ESResource> result = client.getResourceByTag(resource.tags, 0, 30);
//            System.out.println(Arrays.toString(result.get(0).tags));
            Assert.assertEquals(result.get(0).n, resource.n);
            ESUtil.deleteDocument(client.getClient(), resource_index, resource_type, resource.id);
        }
    }
}
