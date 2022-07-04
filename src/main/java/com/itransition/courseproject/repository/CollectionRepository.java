package com.itransition.courseproject.repository;

import com.itransition.courseproject.dto.CollectionDto;
import com.itransition.courseproject.entity.collection.Collection;
import com.itransition.courseproject.projection.CollectionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection,Integer> {

    @Query(nativeQuery = true,
    value = "select c.id as id,\n" +
            "       c.name as title,\n" +
            "       c.description as description,\n" +
            "       coalesce(c.image_url,'null') as imageUrl,\n" +
            "       concat(u.first_name,' ',u.last_name) as author,\n" +
            "       coalesce(cte.total,0) as totalItems,\n" +
            "       c.created_at as createdAt,\n" +
            "       c.updated_at as editedAt,\n" +
            "       t.name as topic\n" +
            "from collections c\n" +
            "         join user_collection uc on uc.id = c.user_collection_id\n" +
            "         join users u on u.id = uc.user_id\n" +
            "         join topics t on t.id = c.topic_id\n" +
            "         left join(\n" +
            "    select items.collection_id,\n" +
            "           count(items.collection_id) as total\n" +
            "    from items\n" +
            "    group by items.collection_id)cte on c.id=cte.collection_id\n" +
            "where u.id= :id \n" +
            "order by totalItems desc ")
    List<CollectionProjection> getAllCollection(int id);

    @Query("select new com.itransition.courseproject.dto.CollectionDto(col.id,col.name,col.description,col.imageUrl) from com.itransition.courseproject.entity.collection.Collection col where col.id= :id")
    CollectionDto getCollection(Integer id);


    @Query(nativeQuery = true,
    value = "select c.id as id,\n" +
            "       c.name as title,\n" +
            "       c.description as description,\n" +
            "       coalesce(c.image_url,'null') as imageUrl,\n" +
            "       concat(u.first_name,' ',u.last_name) as author,\n" +
            "       coalesce(cte.total,0) as totalItems,\n" +
            "       c.created_at as createdAt,\n" +
            "       c.updated_at as editedAt,\n" +
            "       t.name as topic\n" +
            "from collections c\n" +
            "         join user_collection uc on uc.id = c.user_collection_id\n" +
            "         join users u on u.id = uc.user_id\n" +
            "    join topics t on t.id = c.topic_id\n" +
            "         left join(\n" +
            "    select items.collection_id,\n" +
            "           count(items.collection_id) as total\n" +
            "    from items\n" +
            "    group by items.collection_id)cte on c.id=cte.collection_id\n" +
            "order by totalItems\n" +
            "        desc limit 5 ")
    List<CollectionProjection> getTopFiveLargestCollection();

    @Query(nativeQuery = true,
            value = "select c.id as id,\n" +
                    "       c.name as title,\n" +
                    "       c.description as description,\n" +
                    "       coalesce(c.image_url,'null') as imageUrl,\n" +
                    "       concat(u.first_name,' ',u.last_name) as author,\n" +
                    "       coalesce(cte.total,0) as totalItems,\n" +
                    "       c.created_at as createdAt,\n" +
                    "       c.updated_at as editedAt,\n" +
                    "       t.name as topic\n" +
                    "from collections c\n" +
                    "         join user_collection uc on uc.id = c.user_collection_id\n" +
                    "         join users u on u.id = uc.user_id\n" +
                    "    join topics t on t.id = c.topic_id\n" +
                    "         left join(\n" +
                    "    select items.collection_id,\n" +
                    "           count(items.collection_id) as total\n" +
                    "    from items\n" +
                    "    group by items.collection_id)cte on c.id=cte.collection_id\n" +
                    "order by totalItems desc ")
    List<CollectionProjection> getCollectionList();

    @Query(nativeQuery = true,
            value = "select c.id                                   as id,\n" +
                    "       c.name                                 as title,\n" +
                    "       c.description                          as description,\n" +
                    "       coalesce(c.image_url, 'null')          as imageUrl,\n" +
                    "       concat(u.first_name, ' ', u.last_name) as author,\n" +
                    "       coalesce(cte.total, 0)                 as totalItems,\n" +
                    "       c.created_at                           as createdAt,\n" +
                    "       c.updated_at                           as editedAt,\n" +
                    "       t.name as topic\n" +
                    "from collections c\n" +
                    "         join user_collection uc on uc.id = c.user_collection_id\n" +
                    "         join users u on u.id = uc.user_id\n" +
                    "         join topics t on t.id = c.topic_id\n" +
                    "         left join(select items.collection_id,\n" +
                    "                          count(items.collection_id) as total\n" +
                    "                   from items\n" +
                    "                   group by items.collection_id) cte on c.id = cte.collection_id\n" +
                    "where c.id = :id ")
    CollectionProjection collectionById(int id);


    @Query(nativeQuery = true,
    value = "select count(collections.*)\n" +
            "from collections\n" +
            "join user_collection uc on uc.id = collections.user_collection_id\n" +
            "join users u on u.id = uc.user_id\n" +
            "where u.id= :id ;")
    int getTotalCollectionsByUserId(int id);



    @Query(nativeQuery = true,
    value = "select cast(json_build_object('id', result.id,\n" +
            "                              'name',result.name,\n" +
            "                              'description',result.description,\n" +
            "                              'imageUrl',result.imageUrl,\n" +
            "                              'creationTime',result.creationTime,\n" +
            "                              'topicId',result.topicId,\n" +
            "                              'customColumns',result.customColumns\n" +
            "    ) as text)\n" +
            "from(select c.id          as id,\n" +
            "       c.name        as name,\n" +
            "       c.description as description,\n" +
            "       coalesce(c.image_url,'none')   as imageUrl,\n" +
            "       c.created_at as creationTime,\n" +
            "       t.id          as topicId,\n" +
            "       json_agg(cte.*) as customColumns\n" +
            "from collections c\n" +
            "         join topics t on t.id = c.topic_id\n" +
            "         join (select distinct cz.id    as collectionId,\n" +
            "                      cc.id    as id,\n" +
            "                      cc.name  as name,\n" +
            "                      cc.type  as type,\n" +
            "                      'null' as value\n" +
            "               from collections cz\n" +
            "                        join collection_item_column cic on cz.id = cic.collection_id\n" +
            "                        join custom_column cc on cc.id = cic.custom_column_id) cte on cte.collectionId = c.id\n" +
            "where c.id = :id \n" +
            "group by c.id, c.name, c.description, c.image_url, t.id) result;")
    String collectionWithCustomColumns(int id);

    @Query(nativeQuery = true,
    value = "select c.id as id,\n" +
            "       c.name as title,\n" +
            "       c.description as description,\n" +
            "       coalesce(c.image_url,'null') as imageUrl,\n" +
            "       concat(u.first_name,' ',u.last_name) as author,\n" +
            "       coalesce(cte.total,0) as totalItems,\n" +
            "       c.created_at as createdAt,\n" +
            "       c.updated_at as editedAt,\n" +
            "       t.name as topic\n" +
            "from collections c\n" +
            "         join user_collection uc on uc.id = c.user_collection_id\n" +
            "         join users u on u.id = uc.user_id\n" +
            "         join topics t on t.id = c.topic_id\n" +
            "         left join(\n" +
            "    select items.collection_id,\n" +
            "           count(items.collection_id) as total\n" +
            "    from items\n" +
            "    group by items.collection_id)cte on c.id=cte.collection_id\n" +
            "where t.id= :id \n" +
            "order by totalItems desc ")
    List<CollectionProjection> getCollectionByTopicId(int id);
}
