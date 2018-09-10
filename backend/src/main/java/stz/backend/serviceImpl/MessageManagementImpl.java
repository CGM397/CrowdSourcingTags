package stz.backend.serviceImpl;

import stz.backend.DAO.BaseDao;
import stz.backend.entity.Message;
import stz.backend.service.MessageManagementService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessageManagementImpl implements MessageManagementService {

    @Override
    public ArrayList<Message> getNewMessage(String userId) {
        ArrayList<Message> store = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM message WHERE userId = ? AND isRead = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,userId);
            stmt.setBoolean(2,false);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Date date = new Date(rs.getDate("releaseTime").getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.HOUR,8);
                date = cal.getTime();
                Message temp = new Message(rs.getString("messageId"),
                        rs.getString("userId"),
                        date,
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getBoolean("isRead"));
                store.add(temp);
            }
            for(int i = 0; i < store.size(); i++){
                for(int j = 0; j < store.size() - 1; j++){
                    if(store.get(j).getReleaseTime().before(store.get(j+1).getReleaseTime())){
                        Message temp = store.get(j);
                        store.set(j, store.get(j + 1));
                        store.set(j + 1, temp);
                    }
                }
            }
            BaseDao.closeAll(conn,stmt,rs);
        }catch (Exception e){
            e.printStackTrace();
        }
        return store;
    }

    @Override
    public ArrayList<Message> getReadMessage(String userId) {
        ArrayList<Message> store = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM message WHERE userId = ? AND isRead = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,userId);
            stmt.setBoolean(2,true);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Message temp = new Message(rs.getString("messageId"),
                        rs.getString("userId"),
                        rs.getDate("releaseTime"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getBoolean("isRead"));
                store.add(temp);
            }
            for(int i = 0; i < store.size(); i++){
                for(int j = 0; j < store.size() - 1; j++){
                    if(store.get(j+1).getReleaseTime().after(store.get(j).getReleaseTime())){
                        Message temp = store.get(j);
                        store.set(j, store.get(j + 1));
                        store.set(j + 1, temp);
                    }
                }
            }
            BaseDao.closeAll(conn,stmt,rs);
        }catch (Exception e){
            e.printStackTrace();
        }
        return store;
    }

    @Override
    public void createMessage(Message message) {
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "INSERT INTO message (messageId,userId,releaseTime,title," +
                    "content,isRead) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,message.getMessageId());
            stmt.setString(2,message.getUserId());
            stmt.setDate(3,new java.sql.Date(message.getReleaseTime().getTime()));
            stmt.setString(4,message.getTitle());
            stmt.setString(5,message.getContent());
            stmt.setBoolean(6,message.getIsRead());
            stmt.executeUpdate();
            conn.close();
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void readMessage(String messageId,String userId) {
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "UPDATE message SET isRead = ? WHERE messageId = ? AND userId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1,true);
            stmt.setString(2,messageId);
            stmt.setString(3,userId);
            stmt.executeUpdate();
            conn.close();
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessage(String messageId, String userId) {
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "DELETE FROM message WHERE messageId = ? AND userId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,messageId);
            stmt.setString(2,userId);
            stmt.executeUpdate();
            conn.close();
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReadMessage(String userId) {
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "DELETE FROM message WHERE userId = ? AND isRead = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,userId);
            stmt.setBoolean(2,true);
            stmt.executeUpdate();
            conn.close();
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void readAllMessage(String userId) {
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "UPDATE message SET isRead = ? WHERE userId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1,true);
            stmt.setString(2,userId);
            stmt.executeUpdate();
            conn.close();
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
