package com.soulout.community.service;

import com.soulout.community.dto.NotificationDTO;
import com.soulout.community.dto.PaginationDTO;
import com.soulout.community.enums.NotificationStatusEnum;
import com.soulout.community.enums.NotificationTypeEnum;
import com.soulout.community.exception.CustomizeErrorCode;
import com.soulout.community.exception.CustomizeException;
import com.soulout.community.mapper.NotificationMapper;
import com.soulout.community.mapper.UserMapper;
import com.soulout.community.model.Notification;
import com.soulout.community.model.NotificationExample;
import com.soulout.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public PaginationDTO<NotificationDTO> list(Long userId, Integer page, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        Integer totalPage;

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);

        if(totalCount % size == 0 ){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        if(page < 1){
            page = 1;
        }

        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset =size * (page - 1);

        if(offset < 0){
            offset = 0;
        }

        NotificationExample example = new NotificationExample();
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        if(notifications.size() == 0){
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!notification.getReceiver().equals(user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        //更新通知已读状态
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
