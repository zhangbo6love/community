package com.fukuadiary.community.community.service;


import com.fukuadiary.community.community.dto.CommentDTO;
import com.fukuadiary.community.community.enums.CommentTypeEnum;
import com.fukuadiary.community.community.enums.NotificationStatusEnum;
import com.fukuadiary.community.community.enums.NotificationTypeEnum;
import com.fukuadiary.community.community.exception.CustomizeErrorCode;
import com.fukuadiary.community.community.exception.CustomizeException;
import com.fukuadiary.community.community.mapper.*;
import com.fukuadiary.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {

        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUNT);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }


        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria()
                    .andIdEqualTo(comment.getParentId());
            List<Comment> dbcomments = commentMapper.selectByExample(commentExample);
            if (dbcomments.get(0) == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUNT);
            }
            commentMapper.insert(comment);

            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);

            //创建通知
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(dbcomments.get(0).getParentId());
            List<Question> questions = questionMapper.selectByExample(questionExample);
            createNotify(comment, dbcomments.get(0).getCommentor(), comment.getContent(), commentator.getName(), NotificationTypeEnum.REPLY_COMMENT, questions.get(0).getId());
        } else {
            //回复问题
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(comment.getParentId());
            List<Question> questions = questionMapper.selectByExample(questionExample);
            if (questions.get(0) == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUNT);
            }
            commentMapper.insert(comment);
            questions.get(0).setCommentCount(1);
            questionExtMapper.incCommentCount(questions.get(0));

            //创建通知
            createNotify(comment, questions.get(0).getCreator(), questions.get(0).getTitle(), commentator.getName(), NotificationTypeEnum.REPLY_QUESTION, questions.get(0).getId());
        }

    }

    private void createNotify(Comment comment, Long receiver, String outerTitle, String notifierName, NotificationTypeEnum notificationType, Long outerId) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setNotifier(comment.getCommentor());

        notification.setOuterId(outerId);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    @Transactional
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }


        //获取去重的评论人
        Set<Long> commentors = comments.stream().map(comment -> comment.getCommentor()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentors);


        //获取评论人并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));


        //转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentor()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
