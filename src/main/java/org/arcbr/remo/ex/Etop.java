package org.arcbr.remo.ex;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//@Component
public class Etop {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    eEtosa eEtosa;
    @PostConstruct
    public void Etop() {
//        redisTemp.println(name);late.setValueSerializer(null);
////        redisTemplate.opsForValue().set("name", "Etobaskan");
////        Object name = redisTemplate.opsForValue().get("name");
////        System.out
        ObjectHashMapper mapper = new ObjectHashMapper();

        User user = new User().setName("Eto").setLastName("Acar").setList(Arrays.asList(21,424,4));
        User user1 = new User().setName("Berkay").setLastName("Cetinlaya").setList(Arrays.asList(2, 4));
        User user2 = new User().setName("Kado").setLastName("Aydemir").setList(Arrays.asList(223,56, 24));

        user.setUser(user1);
        user.setUsers(Arrays.asList(user1, user2));
        Map<byte [], byte []> map = mapper.toHash(user);
        System.out.println(map);
        map.forEach((k, v) -> System.out.println( new String(k)+ ": "+ new String(v) ));


        eEtosa.set(user);


//        HashMapper<Object, byte[], byte[]> objectHashMapper = new ObjectHashMapper();

//        DecoratingStringHashMapper<User> hashMapper = new DecoratingStringHashMapper<>(objectHashMapper);
//
//        Map<String, String> map = hashMapper.toHash(user);
//        System.out.println(map);
//
//
//        map.forEach( (k,v ) -> System.out.println(new String(k) + ": " + new String(v )));
    }

    public static void main(String[] args) {

        List<? extends AggregationOperation> source = Arrays.asList(Aggregation.match(Criteria.where("id").is("id")));

        List<AggregationOperation> list = new ArrayList<>(source);
        list.add(0, Aggregation.match(Criteria.where("id").is("id")));
        list.add(0, Aggregation.project());

        list.forEach( elem -> System.out.println(elem.getClass().getName()));

    }
}
