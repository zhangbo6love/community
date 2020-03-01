package com.fukuadiary.community.community.service;

import com.fukuadiary.community.community.dto.NotificationDTO;
import com.fukuadiary.community.community.dto.PaginationDTO;
import com.fukuadiary.community.community.dto.helper.PageHelper;
import com.fukuadiary.community.community.enums.NotificationStatusEnum;
import com.fukuadiary.community.community.enums.NotificationTypeEnum;
import com.fukuadiary.community.community.exception.CustomizeErrorCode;
import com.fukuadiary.community.community.exception.CustomizeException;
import com.fukuadiary.community.community.mapper.NotificationMapper;
import com.fukuadiary.community.community.mapper.UserMapper;
import com.fukuadiary.community.community.model.Notification;
import com.fukuadiary.community.community.model.NotificationExample;
import com.fukuadiary.community.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {


        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
        if(totalCount == 0){
            return null;
        }
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        PageHelper.pagination(paginationDTO, totalCount, page, size);


        if(page <= 1){
            page = 1;
        }

        if(page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }

        Integer offset = size * (page - 1);

        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        if(notifications.size() == 0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setType(NotificationTypeEnum.nameOfType(notification.getType()));

            notificationDTOS.add(notificationDTO);

        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(0);
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andIdEqualTo(id);
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);

        if(notifications == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notifications.get(0).getReceiver(), user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        if(notifications.get(0).getStatus() == NotificationStatusEnum.READ.getStatus()){
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notifications.get(0), notificationDTO);
            notificationDTO.setType(NotificationTypeEnum.nameOfType(notifications.get(0).getType()));
            return notificationDTO;
        } else {
            notifications.get(0).setStatus(NotificationStatusEnum.READ.getStatus());
            NotificationExample example = new NotificationExample();
            example.createCriteria()
                    .andIdEqualTo(notifications.get(0).getId());
            notificationMapper.updateByExample(notifications.get(0), example);

            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notifications.get(0), notificationDTO);
            notificationDTO.setType(NotificationTypeEnum.nameOfType(notifications.get(0).getType()));
            return notificationDTO;
        }

    }
}
