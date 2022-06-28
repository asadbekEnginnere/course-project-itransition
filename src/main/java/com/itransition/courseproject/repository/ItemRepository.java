package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Item;
import com.itransition.courseproject.projection.ItemProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {


    @Query(nativeQuery = true,
            value = "select i.id,\n" +
                    "       i.name,\n" +
                    "       concat(u.first_name,' ',u.last_name) as authorFullName,\n" +
                    "       c.name as collection,\n" +
                    "       coalesce(cte.value,'null') as imageUrl,\n" +
                    "       coalesce(count(l.*),0) as likesCount,\n" +
                    "       coalesce(count(d.*),0) as disLikesCount,\n" +
                    "       coalesce(count(c2.*),0) as commentCount\n" +
                    "from  items i\n" +
                    "          join collections c on c.id = i.collection_id\n" +
                    "          join user_collection uc on uc.id = c.user_collection_id\n" +
                    "          join users u on uc.user_id = u.id\n" +
                    "          left join  (\n" +
                    "    select cc.id,cv.item_id,cv.value,cc.type\n" +
                    "    from custom_column cc\n" +
                    "             join custom_value cv on cc.id = cv.custom_column_id\n" +
                    "    where cc.type='image'\n" +
                    ")cte on cte.item_id=i.id\n" +
                    "left join likes l on i.id = l.item_id\n" +
                    "left join dislikes d on i.id = d.item_id\n" +
                    "left join comments c2 on i.id = c2.item_id " +
                    "where i.collection_id= :id \n" +
                    "group by i.id, i.name, concat(u.first_name,' ',u.last_name), c.name, coalesce(cte.value,'null')\n" +
                    "order by i.created_at desc ")
    List<ItemProjection> getAllItemsByCollectionId(int id);


    @Query(nativeQuery = true,
            value = "select i.id,\n" +
                    "       i.name,\n" +
                    "       concat(u.first_name,' ',u.last_name) as authorFullName,\n" +
                    "       c.name as collection,\n" +
                    "       coalesce(cte.value,'null') as imageUrl,\n" +
                    "       coalesce(count(l.*),0) as likesCount,\n" +
                    "       coalesce(count(d.*),0) as disLikesCount,\n" +
                    "       coalesce(count(c2.*),0) as commentCount\n" +
                    "from  items i\n" +
                    "          join collections c on c.id = i.collection_id\n" +
                    "          join user_collection uc on uc.id = c.user_collection_id\n" +
                    "          join users u on uc.user_id = u.id\n" +
                    "          left join  (\n" +
                    "    select cc.id,cv.item_id,cv.value,cc.type\n" +
                    "    from custom_column cc\n" +
                    "             join custom_value cv on cc.id = cv.custom_column_id\n" +
                    "    where cc.type='image'\n" +
                    ")cte on cte.item_id=i.id\n" +
                    "left join likes l on i.id = l.item_id\n" +
                    "left join dislikes d on i.id = d.item_id\n" +
                    "left join comments c2 on i.id = c2.item_id\n" +
                    "group by i.id, i.name, concat(u.first_name,' ',u.last_name), c.name, coalesce(cte.value,'null')\n" +
                    "order by i.created_at desc\n" +
                    "limit 4 ")
    List<ItemProjection> getLatestItems();


    @Query(nativeQuery = true,
            value = "select cast(json_build_object('id', result.id,\n" +
                    "                             'itemName',result.itemName,\n" +
                    "                             'itemCollection',result.itemCollection,\n" +
                    "                             'authorFullName',result.authorFullName,\n" +
                    "                             'itemImageUrl',result.itemImageUrl,\n" +
                    "                             'itemTag',result.itemTag,\n" +
                    "                             'itemCustomColumn',result.itemCustomColumn," +
                    "                             'dateTime',result.dateTime \n" +
                    "    ) as text)\n" +
                    "from(select i.id,\n" +
                    "       i.name as itemName,\n" +
                    "       c.name as itemCollection,\n" +
                    "       concat(u.first_name,' ',u.last_name) as authorFullName,\n" +
                    "       coalesce(cte.value,'null') as itemImageUrl,\n" +
                    "       json_agg(distinct itemTags.*) as itemTag,\n" +
                    "       json_agg(distinct customColumn.*) as itemCustomColumn," +
                    "       i.created_at as dateTime \n" +
                    "from items i\n" +
                    "join collections c on c.id = i.collection_id\n" +
                    "join user_collection uc on uc.id = c.user_collection_id\n" +
                    "join users u on u.id = uc.user_id\n" +
                    "left join  (\n" +
                    "    select cc.id,cv.item_id,cv.value,cc.type\n" +
                    "    from custom_column cc\n" +
                    "             join custom_value cv on cc.id = cv.custom_column_id\n" +
                    "    where cc.type='image'\n" +
                    ")cte on cte.item_id=i.id\n" +
                    "join (\n" +
                    "    select tags.id,tags.name,it.item_id as itemId\n" +
                    "    from tags\n" +
                    "    join item_tags it on tags.id = it.tag_id\n" +
                    ")itemTags on itemTags.itemId=i.id\n" +
                    "left join(\n" +
                    "    select cv.id,cc2.name,cc2.type,cv.value,cv.item_id as itemId\n" +
                    "    from custom_value cv\n" +
                    "    join custom_column cc2 on cv.custom_column_id = cc2.id \n" +
                    ")customColumn on customColumn.itemId=i.id\n" +
                    "where i.id= :id \n" +
                    "group by i.id, i.name, c.name, concat(u.first_name,' ',u.last_name), coalesce(cte.value,'null'))result;")
    String getItemById(int id);

    @Query(nativeQuery = true, value = "select i.id,\n" +
            "       i.name,\n" +
            "       concat(u.first_name,' ',u.last_name) as authorFullName,\n" +
            "       c.name as collection,\n" +
            "       coalesce(cte.value,'null') as imageUrl,\n" +
            "       coalesce(count(l.*),0) as likesCount,\n" +
            "       coalesce(count(d.*),0) as disLikesCount,\n" +
            "       coalesce(count(c2.*),0) as commentCount\n" +
            "from  items i\n" +
            "          join collections c on c.id = i.collection_id\n" +
            "          join user_collection uc on uc.id = c.user_collection_id\n" +
            "          join users u on uc.user_id = u.id\n" +
            "          left join  (\n" +
            "    select cc.id,cv.item_id,cv.value,cc.type\n" +
            "    from custom_column cc\n" +
            "             join custom_value cv on cc.id = cv.custom_column_id\n" +
            "    where cc.type='image'\n" +
            ")cte on cte.item_id=i.id\n" +
            "left join likes l on i.id = l.item_id\n" +
            "left join dislikes d on i.id = d.item_id\n" +
            "left join comments c2 on i.id = c2.item_id\n" +
            "group by i.id, i.name, concat(u.first_name,' ',u.last_name), c.name, coalesce(cte.value,'null')\n" +
            "order by i.created_at desc ")
    List<ItemProjection> getAllItems();


    @Query(nativeQuery = true,
            value = "select i.id,\n" +
                    "       i.name,\n" +
                    "       concat(u.first_name, ' ', u.last_name) as authorFullName,\n" +
                    "       c.name                                 as collection,\n" +
                    "       coalesce(cte.value, 'null')            as imageUrl,\n" +
                    "       coalesce(count(l.*), 0)                as likesCount,\n" +
                    "       coalesce(count(d.*), 0)                as disLikesCount,\n" +
                    "       coalesce(count(c2.*), 0)               as commentCount\n" +
                    "from items i\n" +
                    "         join collections c on c.id = i.collection_id\n" +
                    "         join user_collection uc on uc.id = c.user_collection_id\n" +
                    "         join users u on uc.user_id = u.id\n" +
                    "         left join (select cc.id, cv.item_id, cv.value, cc.type\n" +
                    "                    from custom_column cc\n" +
                    "                             join custom_value cv on cc.id = cv.custom_column_id\n" +
                    "                    where cc.type = 'image') cte on cte.item_id = i.id\n" +
                    "         left join likes l on i.id = l.item_id\n" +
                    "         left join dislikes d on i.id = d.item_id\n" +
                    "         left join comments c2 on i.id = c2.item_id\n" +
                    "         join item_tags it on i.id = it.item_id\n" +
                    "         join tags t on it.tag_id = t.id\n" +
                    "where t.id= :id \n" +
                    "group by i.id, i.name, concat(u.first_name, ' ', u.last_name), c.name, coalesce(cte.value, 'null')\n" +
                    "order by i.created_at desc ")
    List<ItemProjection> getAllItemByTagId(int id);

    @Query(nativeQuery = true,
    value = "select count(items.*)\n" +
            "from items\n" +
            "join collections c on c.id = items.collection_id\n" +
            "join user_collection uc on uc.id = c.user_collection_id\n" +
            "join users u on u.id = uc.user_id\n" +
            "where u.id= :id ;")
    int getTotalItemsByUserId(int id);

}
