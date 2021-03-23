package co.com.wolox.technicaltest.service;

import co.com.wolox.technicaltest.model.user.User;

import java.util.List;

public interface InterfaceUserService {

    User findById(Long id);

    List<User> findAll();
}
