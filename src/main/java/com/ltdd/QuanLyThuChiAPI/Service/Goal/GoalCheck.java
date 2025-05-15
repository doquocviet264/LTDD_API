package com.ltdd.QuanLyThuChiAPI.Service.Goal;

import com.ltdd.QuanLyThuChiAPI.Model.Goal.GoalModel;
import com.ltdd.QuanLyThuChiAPI.Model.Notification.NotificationModel;
import com.ltdd.QuanLyThuChiAPI.Repository.Goal.GoalRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.Notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class GoalCheck {

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Scheduled(cron = "*/10 * * * * *")
    public void checkGoalDeadline()
    {
        LocalDate currentDate = LocalDate.now();
        List<GoalModel> listGoal = goalRepository.findAll();
        for (GoalModel goalModel : listGoal)
        {
            if(goalModel.getStatus() != 2)
            {
                LocalDate deadline = goalModel.getDeadline().toLocalDate();
                Long balance = goalModel.getBalance();
                Long amount = goalModel.getAmount();
                Long deposit = goalModel.getDeposit();

                if(currentDate.isAfter(deadline))
                {
                    if((balance+deposit) < amount)
                    {
                        NotificationModel noti = notificationRepository.findByContentAndTitle("Mục tiêu " + goalModel.getName() + " đã hết hạn", "Mục tiêu đã hết hạn");
                        if(noti == null) {
                            NotificationModel notificationModel = new NotificationModel();
                            notificationModel.setUserInfoModel(goalModel.getUserInfoModel());
                            notificationModel.setTitle("Mục tiêu đã hết hạn");
                            notificationModel.setContent("Mục tiêu " + goalModel.getName() + " đã hết hạn");
                            notificationModel.set_read(false);
                            Date current = new Date(System.currentTimeMillis());
                            notificationModel.setCreated_at(current);
                            notificationRepository.save(notificationModel);

                            goalModel.setStatus(3L);
                            goalRepository.save(goalModel);
                        }
                    }
                }else if(currentDate.equals(deadline))
                {
                    if((balance+deposit) >= amount)
                    {
                        NotificationModel noti = notificationRepository.findByContentAndTitle("Bạn đã hoàn thành mục tiêu " + goalModel.getName(), "Mục tiêu đã hoàn thành");
                        if(noti == null) {
                            NotificationModel notificationModel = new NotificationModel();
                            notificationModel.setUserInfoModel(goalModel.getUserInfoModel());
                            notificationModel.setTitle("Mục tiêu đã hoàn thành");
                            notificationModel.setContent("Bạn đã hoàn thành mục tiêu " + goalModel.getName());
                            notificationModel.set_read(false);
                            Date current = new Date(System.currentTimeMillis());
                            notificationModel.setCreated_at(current);
                            notificationRepository.save(notificationModel);

                            goalModel.setStatus(2L);
                            goalRepository.save(goalModel);
                        }
                    }
                }
                else if(currentDate.isBefore(deadline))
                {
                    Long daysBetween = ChronoUnit.DAYS.between(currentDate, deadline);
                    if(daysBetween == 3)
                    {
                        if((balance+deposit) < amount)
                        {
                            NotificationModel noti = notificationRepository.findByContentAndTitle("Mục tiêu " + goalModel.getName() + " của bạn sắp hết hạn. Hãy thêm tiền cho mục tiêu để hoàn thành nhé", "Mục tiêu sắp hết hạn");
                            if(noti == null){
                            NotificationModel notificationModel = new NotificationModel();
                            notificationModel.setUserInfoModel(goalModel.getUserInfoModel());
                            notificationModel.setTitle("Mục tiêu sắp hết hạn");
                            notificationModel.setContent("Mục tiêu " + goalModel.getName() + " của bạn sắp hết hạn. Hãy thêm tiền cho mục tiêu để hoàn thành nhé");
                            notificationModel.set_read(false);
                            Date current = new Date(System.currentTimeMillis());
                            notificationModel.setCreated_at(current);
                            notificationRepository.save(notificationModel);

                            goalModel.setStatus(1L);
                            goalRepository.save(goalModel);
                            }
                        }
                    }
                    if((balance+deposit) >= amount)
                    {
                        NotificationModel noti = notificationRepository.findByContentAndTitle("Bạn đã hoàn thành mục tiêu " + goalModel.getName(), "Mục tiêu đã hoàn thành");
                        NotificationModel notificationModel = new NotificationModel();
                        notificationModel.setUserInfoModel(goalModel.getUserInfoModel());
                        notificationModel.setTitle("Mục tiêu đã hoàn thành");
                        notificationModel.setContent("Bạn đã hoàn thành mục tiêu " + goalModel.getName());
                        notificationModel.set_read(false);
                        Date current = new Date(System.currentTimeMillis());
                        notificationModel.setCreated_at(current);
                        notificationRepository.save(notificationModel);

                        goalModel.setStatus(2L);
                        goalRepository.save(goalModel);
                    }
                }
            }
        }
    }
}
