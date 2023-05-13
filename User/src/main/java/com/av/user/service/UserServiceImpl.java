package com.av.user.service;

import com.av.user.entity.User;
import com.av.user.exception.UserDuplicatedException;
import com.av.user.exception.UserNotFoundException;
import com.av.user.exception.UserPhoneNumberDuplicatedException;
import com.av.user.exception.UserUserNameDuplicatedException;
import com.av.user.repository.UserRepository;
import com.av.user.request.UserInsertRequest;
import com.av.user.request.UserUpdateRequest;
import com.av.user.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long insert(UserInsertRequest request) throws
            UserUserNameDuplicatedException , UserPhoneNumberDuplicatedException {
        try {
            findByPhoneNumber(request.phoneNumber());
            findByUserName(request.username());
        } catch (UserDuplicatedException exception){
            throw new UserDuplicatedException(
                    "User with username or phone number found.\nUsername : " + request.username()
                    + "\nPhone Number : " + request.phoneNumber()
            );
        }
        User user = User.builder().
                firstname(request.firstName()).
                lastname(request.lastName()).
                bio(request.bio()).
                username(request.username()).
                phoneNumber(request.phoneNumber()).
                profilePicture(null).
                savedMessages(new ArrayList<>()).
                sendMessages(new ArrayList<>()).
                allMessages(new ArrayList<>()).
                unreadMessages(new ArrayList<>()).
                contacts(new ArrayList<>()).
                build();
        return userRepository.save(user).getId();
    }

    @Override
    public UserResponse findById(Long id) throws UserNotFoundException{
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User not found with id : " + id));
        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getBio(),
                user.getPhoneNumber(),
                user.getProfilePicture());
    }

    @Override
    public UserResponse findByPhoneNumber(String phoneNumber) throws UserNotFoundException{
        User user = userRepository.findByPhoneNumber(phoneNumber).
                orElseThrow(() -> new UserNotFoundException("User not found with phone number : " + phoneNumber));
        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getBio(),
                user.getPhoneNumber(),
                user.getProfilePicture()
        );
    }

    @Override
    public UserResponse findByUserName(String username) throws UserNotFoundException{
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException("User not found with username : " + username));
        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getBio(),
                user.getPhoneNumber(),
                user.getProfilePicture()
        );
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    @Override
    public List<UserResponse> findAllBySorting() {
        List<User> users = userRepository.findAll(Sort.by(/* messageRepository.save(message); */"id"));
        return users.stream().map(this::convertToResponse).toList();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteByPhoneNumber(String phoneNumber) {
        userRepository.deleteByPhoneNumber(phoneNumber);
    }

    @Override
    public Long updateById(UserUpdateRequest request) {
        return null;
    }

    @Override
    public Long updateByPhoneNumber(UserUpdateRequest request) {
        return null;
    }

    private UserResponse convertToResponse(User user){
        return new UserResponse(user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getBio(),
                user.getPhoneNumber(),
                user.getProfilePicture());
    }
}
