package com.ltdd.QuanLyThuChiAPI.Repository.Notification;

import com.ltdd.QuanLyThuChiAPI.Model.Notification.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {
    @Query("SELECT n FROM NotificationModel n WHERE n.userInfoModel.accountModel.username = :username AND n.is_read = false ")
    List<NotificationModel> findAllNotRead(@Param("username") String username);

    @Query("SELECT n FROM NotificationModel n WHERE n.userInfoModel.accountModel.username = :username AND n.id = :id")
    NotificationModel findNotifiById(@Param("username") String username, @Param("id") Long id);

    @Query("SELECT n FROM NotificationModel n WHERE n.content = :content AND n.title = :title")
    NotificationModel findByContentAndTitle(@Param("content") String content, @Param("title") String title);
}
