package com.itransition.courseproject.projection;

import java.util.List;

public interface ItemDetailProjection {
    Integer getId();
    String getItemName();
    String getItemCollection();
    String getAuthorFullName();
    String getItemImageUrl();
    List<TagProjection> getItemTag();
    List<ItemCustomColumnProjection> getItemCustomColumn();
}
