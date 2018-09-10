package stz.backend.service;

import stz.backend.entity.Message;

import java.util.ArrayList;

public interface MessageManagementService {
    //获得一个用户的所有未读消息(按发送时间顺序从最近开始)
    public ArrayList<Message> getNewMessage(String userId);

    //获得一个用户的所有已读消息(按发送时间顺序从最近开始)
    public ArrayList<Message> getReadMessage(String userId);

    //用于任务完成、提交、发布等事件发生时，后端调用该方法创造消息
    public void createMessage(Message message);

    //讲一个消息标为已读
    public void readMessage(String messageId,String userId);

    //用户删除一则消息
    public void deleteMessage(String messageId,String userId);

    //用户删除所有已读消息
    public void deleteReadMessage(String userId);

    //用户将所有消息标为已读
    public void readAllMessage(String userId);
}
