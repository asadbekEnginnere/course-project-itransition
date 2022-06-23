package com.itransition.courseproject.repository;

import com.itransition.courseproject.dto.CollectionDto;
import com.itransition.courseproject.entity.collection.Collection;
import com.itransition.courseproject.projection.CollectionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection,Integer> {

    @Query(nativeQuery = true,
    value = "select cast(json_build_object('id', result.id,\n" +
            "                                        'id',result.id,\n" +
            "                                        'name',result.name,\n" +
            "                                        'description',result.description,\n" +
            "                                        'imageUrl',result.imageUrl,\n" +
            "                                        'creationTime',result.creationTime\n" +
            "               ) as text)\n" +
            "from (select collections.id,\n" +
            "             collections.name,\n" +
            "             collections.description,\n" +
            "             coalesce(collections.image_url,'none') as imageUrl,\n" +
            "             collections.created_at as creationTime\n" +
            "      from collections\n" +
            "               join user_collection uc on uc.id = collections.user_collection_id\n" +
            "               join users u on u.id = uc.user_id\n" +
            "      where u.id= :id )result ")
    List<String> getAllCollection(int id);

    @Query("select new com.itransition.courseproject.dto.CollectionDto(col.id,col.name,col.description,col.imageUrl) from com.itransition.courseproject.entity.collection.Collection col where col.id= :id")
    CollectionDto getCollection(Integer id);


    @Query(nativeQuery = true,
    value = "select c.id as id,\n" +
            "       c.name as title,\n" +
            "       c.description as description,\n" +
            "       coalesce(c.image_url,'null') as imageUrl,\n" +
            "       concat(u.first_name,' ',u.last_name) as author,\n" +
            "       coalesce(cte.total,0) as totalItems\n" +
            "from collections c\n" +
            "join user_collection uc on uc.id = c.user_collection_id\n" +
            "join users u on u.id = uc.user_id\n" +
            "left join(\n" +
            "    select items.collection_id,\n" +
            "           count(items.collection_id) as total\n" +
            "    from items\n" +
            "    group by items.collection_id)cte on c.id=cte.collection_id\n" +
            "order by totalItems\n" +
            "desc limit 5 ")
    List<CollectionProjection> getTopFiveLargestCollection();

    @Query(nativeQuery = true,
            value = "select c.id as id,\n" +
                    "       c.name as title,\n" +
                    "       c.description as description,\n" +
                    "       coalesce(c.image_url,'null') as imageUrl,\n" +
                    "       concat(u.first_name,' ',u.last_name) as author,\n" +
                    "       coalesce(cte.total,0) as totalItems\n" +
                    "from collections c\n" +
                    "join user_collection uc on uc.id = c.user_collection_id\n" +
                    "join users u on u.id = uc.user_id\n" +
                    "left join(\n" +
                    "    select items.collection_id,\n" +
                    "           count(items.collection_id) as total\n" +
                    "    from items\n" +
                    "    group by items.collection_id)cte on c.id=cte.collection_id\n" +
                    "order by totalItems desc ")
    List<CollectionProjection> getCollectionList();


}
