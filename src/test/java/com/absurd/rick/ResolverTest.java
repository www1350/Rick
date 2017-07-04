package com.absurd.rick;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wangwenwei on 17/6/29.
 */
public class ResolverTest extends BaseTest{
    @Test
    public void resolver() throws IOException {
        List<String> lists = Files.readAllLines(Paths.get("/Users/wangwenwei/Downloads/user.txt"));
        List<String> lists2 = new ArrayList<>();
        for(String line : lists) {
            if (!line.contains("/*")) {
                lists2.add(line);
            }else{
                lists2.add(",");
            }
        }
        Files.write(Paths.get("/Users/wangwenwei/tables/a.txt"),lists2);

    }


    @Test
    public void toJson() throws IOException {
        List<String> lists = Files.readAllLines(Paths.get("/Users/wangwenwei/tables/a.txt"));
        StringBuilder stringBuilder = new StringBuilder();
        for(String line : lists){
            stringBuilder.append(line);
        }
        List<UT> utList = JSON.parseArray(stringBuilder.toString(),UT.class);
        List<String> lists2 = new ArrayList<>();
        for(UT u : utList){
            String tt = String.format("update sell_chance set belong_sales = '%s' where belong_sales = '%s';",u.getSouche_id(),u.getPhone());
            lists2.add(tt);
        }
        Files.write(Paths.get("/Users/wangwenwei/tables/b.txt"),lists2);

    }


    @Test
    public void toAJson() throws IOException {
        List<String> lists = Files.readAllLines(Paths.get("/Users/wangwenwei/tables/a.txt"));
        List<String> list2 = Files.readAllLines(Paths.get("/Users/wangwenwei/tables/c.txt"));
        StringBuilder stringBuilder = new StringBuilder();
        for(String line : lists){

             stringBuilder.append(line);
        }
        List<UT> utList = JSON.parseArray(stringBuilder.toString(),UT.class);
        List<String> lists2 = new ArrayList<>();
        for(UT u : utList){
            if (list2.contains(u.getPhone())) {
                String tt = String.format("update sell_chance set operator = '%s' where operator = '%s';", u.getSouche_id(), u.getPhone());
                lists2.add(tt);
            }
        }
        Files.write(Paths.get("/Users/wangwenwei/tables/d.txt"),lists2);

    }


    @Test
    public void ttt() throws IOException {
        List<String> lists = Files.readAllLines(Paths.get("/Users/wangwenwei/tables/f.txt"));
        StringBuilder stringBuilder = new StringBuilder();
        List<String> lists2 = new ArrayList<>();
        for(String line : lists){
            lists2.add(line);
            lists2.add(",");
        }

        Files.write(Paths.get("/Users/wangwenwei/tables/ff.txt"),lists2);

    }

    @Test
    public void te2() throws IOException {
        List<String> lists = Files.readAllLines(Paths.get("/Users/wangwenwei/tables/end_2.txt"));
        Set set = new HashSet();
        for(String line:lists){
            set.add(line);
        }
        List<String> lists2 = Files.readAllLines(Paths.get("/Users/wangwenwei/tables/end_1.txt"));
        List<String> lists3 = new ArrayList<>();
        for(String line : lists2){
            if(!set.contains(line)){
                lists3.add(line);
            }
        }
        Files.write(Paths.get("/Users/wangwenwei/tables/end_3.txt"),lists3);
    }
}
