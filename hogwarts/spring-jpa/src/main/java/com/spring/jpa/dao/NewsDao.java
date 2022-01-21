package com.spring.jpa.dao;

import com.spring.jpa.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface NewsDao extends JpaRepository<News,Integer>, JpaSpecificationExecutor {

    @Query("select u from News u where u.id = ?1")
    News findByNewsId(String newsId);

    //如果是更新或者删除操作，方法上面要加@Modifying      默认开启的事务只是可读的，更新操作加入@Modifying 就会关闭可读
    @Modifying
    @Transactional
    @Query("update News set title=?1 where id in ?2")
    void updateCardStatus(String title, List<Integer> ids);

    // @Param 代替参数占位符，  hql或者sql里就用  :firstname替换   方法里的参数顺序可以打乱
    @Query("select u from News u where u.title = :title or u.id = :id")
    News findByIdOrTitle(@Param("title") String title, @Param("id") int id);

    //返回字段 组成新的entity返回 类名必须是全写的
    @Query(value="select new News(c.id, c.title, c.intro) from News c")
    List<News> getAllNews();

    @Modifying
    @Query(value="select status from cms_news where id=?1",nativeQuery = true)
    List<Integer> findStatusById(int id);

    @Query(value="select title from cms_news",nativeQuery = true)
    List<String> getNewsList();
}
