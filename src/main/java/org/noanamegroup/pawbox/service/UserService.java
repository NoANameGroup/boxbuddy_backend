package org.noanamegroup.pawbox.service;

import org.noanamegroup.pawbox.dao.BoxDAO;
import org.noanamegroup.pawbox.dao.UserDAO;
import org.noanamegroup.pawbox.entity.Box;
import org.noanamegroup.pawbox.entity.User;
import org.noanamegroup.pawbox.entity.dto.BoxDTO;
import org.noanamegroup.pawbox.entity.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements UserServiceImpl
{
    @Autowired
    UserDAO userDAO;
    BoxDAO boxDAO;

    @Override
    public User register(UserDTO userDTO)
    {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userDAO.insert(user);
        return user;
    }

    @Override
    public User getUser(Integer userId) {
        return userDAO.selectById(userId);
    }

    @Override
    public User updateUser(UserDTO userDTO) {
        User user = userDAO.selectById(userDTO.getUserId());
        if (user == null) {
            return null;
        }
        BeanUtils.copyProperties(userDTO, user);
        userDAO.updateById(user);
        return user;
    }

    @Override
    public User deleteUser(Integer userId)
    {
        User user = userDAO.selectById(userId);
        if (user == null) {
            return null;
        }
        userDAO.deleteById(userId);
        return user;
    }

    @Override
    public User login(UserDTO userDTO) {
        User user = userDAO.selectById(userDTO.getUserId());
        if (user == null) {
            return null;
        }
        if (user.getPassword().equals(userDTO.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public Box sendBox(BoxDTO boxDTO, Integer senderId) {
        Box box = new Box();
        box.setContent(boxDTO.getContent());
        box.setCreateTime(LocalDateTime.now());
        box.setImageUrl(boxDTO.getImageUrl());  // 直接设置图片URL

        box.setSender(userDAO.selectById(senderId));
        boxDAO.insert(box);
        return box;
    }

    @Override
    public Box receiveBox(Integer boxId, Integer receiverId) {
        Box box = boxDAO.selectById(boxId);
        if (box == null) {
            return null;
        }
        User receiver = userDAO.selectById(receiverId);
        if (receiver == null) {
            return null;
        }
        List<User> receivers = box.getReceivers();
        receivers.add(receiver);
        box.setReceivers(receivers);
        return box;
    }
}