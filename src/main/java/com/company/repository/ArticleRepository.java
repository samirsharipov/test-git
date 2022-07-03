package com.company.repository;


import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatus;
import com.company.mapper.ArticleShortInfoByCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    Optional<ArticleEntity> findByTitleAndContent(String title, String content);



    @Modifying
    @Transactional
    @Query("update ArticleEntity a set a.status =:status, a.publishDate =:time, a.publisher.id=:pid where a.id=:articleId")
    void changeStatusToPublish(@Param("articleId") String articleId, @Param("pid") Integer pId,
                               @Param("status") ArticleStatus status, @Param("time") LocalDateTime time);

    @Modifying
    @Transactional
    @Query("update ArticleEntity a set a.status =:status where a.id=:articleId")
    void changeStatusNotPublish(@Param("articleId") String articleId, @Param("status") ArticleStatus status);


    @Query(value = "SELECT  art.id as id ,art.title as title, art.description as description,art.publish_date as publishDate  " +
            " FROM article_type as a " +
            " inner join article as art on art.id = a.article_id " +
            " inner join type as t on t.id = a.types_id " +
            " Where  t.key =:key  and  art.visible = true and" +
            " art.status = 'PUBLISHED'" +
            "order by art.publish_date " +
            " limit 5",
            nativeQuery = true)
    List<ArticleShortInfoByCategory> findTop5ByArticleNative(@Param("key") String key);//////////////




    @Query(value = "SELECT art.*" +
            " FROM article_type as a" +
            " inner join article as art on art.id = a.article_id" +
            " inner join type as t on t.id = a.types_id" +
            " Where  t.key =:key  and  art.visible = true and" +
            " art.status = 'PUBLISHED'" +
            "order by art.publish_date " +
            " limit 3",
            nativeQuery = true)
    List<ArticleEntity> findTop3ByArticleNative(@Param("key") String key);



    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishDate) " +
            " from ArticleEntity as art " +
            " Where art.visible = true and art.status = 'PUBLISHED' and art.id not in (:idList) " +
            " order by art.publishDate ")
    Page<ArticleEntity> findLast8NotIn(@Param("idList") List<String> idList, Pageable pageable);



    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishDate) " +
            " from ArticleTypeEntity as a " +
            " inner join a.article as art " +
            " inner join a.types as t " +
            " Where t.key =:typeKey and art.visible = true and art.status = 'PUBLISHED' and art.id not in (:id)" +
            " order by art.publishDate ")
    Page<ArticleEntity> findLast4TypeNotIn(@Param("typeKey") String typeKey, String id, Pageable pageable);
/*
    @Query(value = "SELECT  art.id as id ,art.title as title, art.description as description,art.publish_date as publishDate " +
            " FROM article as art " +
            " inner join article_type as a on a.article_id = art.id " +
            " inner join types as t on t.id = a.types_id " +
            " where  t.key =:key and art.visible = true and art.status = 'PUBLISHED' and art.id not in (:id) " +
            " order by art.publish_date " +
            " limit 5 ",
            nativeQuery = true)
    List<ArticleShortInfoByCategory> findLast4TypeNotIn(@Param("key") String key, @Param("id") String id);
*/

  /*      @Query(value = "SELECT art.*" +
                " FROM region as r" +
                " inner join article as art on art.id = r.article_id" +
                " inner join type as t on t.id = a.types_id" +
                " Where t.key =:key " +
                " order by art.publish_date" +
                " limit 3",
                nativeQuery = true)

        List<ArticleEntity> findTopByArticleRegionNative(@Param("key") String key);*/

    /* List<ArticleEntity> findAllByRegion(RegionEntity region);*/

    @Query(value = " SELECT art.* FROM  " +
            " article as art " +
            " inner join region as r on r.id=art.region_id" +
            " where r.key=:key ",
            nativeQuery = true)
    List<ArticleEntity> findbyArticleRegionKeyNative(@Param("key") String key);



//////////////////////////////////////////////////////////////////////////////


//
  /*  List<ArticleEntity> findTop5ByCategoryAndStatusAndVisibleTrueOrderByCreatedDateDesc(CategoryEntity category,
                                                                                        ArticleStatus status);

    List<ArticleEntity> findTop5ByCategoryAndStatusAndVisibleOrderByCreatedDateDesc(CategoryEntity category,
                                                                                    ArticleStatus status, Boolean visible);
    */
//////////////////////////////////////////////////////////

//yana bir yoli
/*    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishDate) " +
            " from ArticleEntity  as art where art.category.key =:categoryKey and art.status =:status " +
            " and art.visible = true " +
            " order by art.publishDate ")
    Page<ArticleEntity> findLast5ByCategory(@Param("categoryKey") String categoryKey,
                                            @Param("status") ArticleStatus status, Pageable pageable);*/
////////////////////////

    //@Query(value = "select  art.id as id ,art.title as title, art.description as description,art.publish_date as publishDate" +
    //            "   from article as art " +
    //            "   inner join category as cat on art.category_id = cat.id " +
    //            " where cat.key=:key and art.status='PUBLISHED' and art.visible=true  " +
    //            " order by art.publish_date limit 5 ",
    //            nativeQuery = true)
    //    List<ArticleShortInfoByCategory> findTop5ByArticleByCategory2(@Param("key") String key);



////////////////////////
@Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishDate) " +
        " from ArticleTagEntity as a " +
        " inner join a.article as art " +
        " inner join a.tag as t " +
        " Where t.name =:tagName and art.visible = true and art.status = 'PUBLISHED' " +
        " order by art.publishDate ")
Page<ArticleEntity> getLast4ArticleByTagName(@Param("tagName") String tagName, Pageable pageable);

///////////////////////////////

  /*  @Query(value = "SELECT art.* " +
            " FROM article as art " +
            " inner join article_type as a on a.article_id = art.id " +
            " inner join types as t on t.id = a.types_id " +
            " where  t.key =:key  " +
            " order by art.publish_date " +
            " limit 5 ",
            nativeQuery = true)
    List<ArticleEntity> findTop5ByArticleNative(@Param("key") String key);*/

    /////////////////////////////////////////////
/*    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishDate)  " +
            " from ArticleTagEntity as at" +
            "inner join at.article as art " +
            " where  art.status = 'PUBLISHED' and" +
            " art.visible = true " +
            " order by art.viewCount ")
      Page<ArticleEntity> most(Pageable pageable);*/

//ishladi
    @Query(value = "SELECT  art.id as id ,art.title as title, art.description as description,art.publish_date as publishDate" +
            " FROM article as art " +
            " inner join article_tag as a on art.id = a.article_id " +
            " inner join tag as t on t.id = a.tag_id " +
            " Where  t.name =:name  and  art.visible = true and" +
            " art.status = 'PUBLISHED'" +
            "order by art.publish_date " +
            " limit 5",
            nativeQuery = true)
    List<ArticleShortInfoByCategory> tagName(@Param("name") String key);



    @Query(value = "SELECT  art.id as id ,art.title as title, art.description as description,art.publish_date as publishDate " +
            " FROM article_type as a " +
            " inner join article as art on art.id = a.article_id " +
            " inner join type as t on t.id = a.types_id " +
            " inner join region as r on r.id=art.region_id " +
            " Where  t.key =:typeKey and  r.key=:regionKey and  art.visible = true and " +
            " art.status = 'PUBLISHED'" +
            " order by art.publish_date " +
            " limit 5",
            nativeQuery = true)
    List<ArticleShortInfoByCategory> getLast5ArticleByTypesAndByRegionKey(@Param("typeKey") String typeKey, @Param("regionKey") String regionKey);

    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishDate) " +
            " from ArticleEntity  as art" +
            " where art.region.key =:regionKey and " +
            " art.visible = true and art.status='PUBLISHED'" +
            " order by art.publishDate ")
    Page<ArticleEntity> getArticlelistByRegionKey(@Param("regionKey") String categoryKey, Pageable pageable);

    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishDate) " +
            " from ArticleEntity as art " +
            " inner join art.category as a " +
            " Where a.key =:key and art.visible = true and art.status = 'PUBLISHED' " +
            " order by art.publishDate ")
    Page<ArticleEntity> getLastCategory(@Param("key") String key, Pageable pageable);




    @Query(value = "SELECT  art.id as id ,art.title as title, art.description as description,art.publish_date as publishDate  " +
            " FROM article as art " +
            " inner join category as cat on cat.id=art.category_id" +
            " where cat.key=:key  and art.visible = true and" +
            " art.status = 'PUBLISHED'" +
            " order by art.publish_date " +
            " limit 5 ",
            nativeQuery = true)
    List<ArticleShortInfoByCategory> findby5ArticleCategoryKeyNative(@Param("key") String key);


    @Query(value = "select  art.id as id ,art.title as title, art.description as description,art.publish_date as publishDate" +
            "   from article as art " +
            " where art.status='PUBLISHED' and art.visible=true  " +
            " order by art.view_count desc limit 4 ",
            nativeQuery = true)
    List<ArticleShortInfoByCategory> findMost4ViewedArticleList();


    Optional<ArticleEntity> findByIdAndVisibleTrueAndStatus(String id, ArticleStatus status);


    @Query("FROM ArticleEntity art where art.visible = true and art.status = 'PUBLISHED' and art.id =?1")
    Optional<ArticleEntity> getPublishedById(String id);


    @Modifying
    @Transactional
    @Query("update ArticleEntity a set a.viewCount =:count where a.id=:articleId")
    void updtecount(@Param("articleId") String articleId, @Param("count") Integer count);


    @Modifying
    @Transactional
    @Query("update  ArticleEntity a set a.image.id=?1 where a.id=?2")
    void updateByPhoto(String id, String aricleId);

    @Modifying
    @Transactional
    @Query("update  ArticleEntity a set a.image.id=?1 where a.id=?2")
    void updateByPhotoNull(String id, String aricleId);
}