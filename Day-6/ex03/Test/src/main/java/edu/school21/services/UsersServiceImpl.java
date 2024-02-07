package edu.school21.services;

import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.repositories.UsersRepository;
import edu.school21.models.User;

public class UsersServiceImpl {
    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public boolean authenticate(String login, String password)
            throws AlreadyAuthenticatedException, EntityNotFoundException, IllegalArgumentException {
        if (login == null || password == null) {
            throw new IllegalArgumentException("Login and password cannot be null");
        }

        try {
            User user = usersRepository.findByLogin(login);
            if (user.getAuthStatus()) {
                throw new AlreadyAuthenticatedException("User " + login + " is already authenticated");
            }
            if (!user.getPassword().equals(password)) {
                return false;
            }
            user.setAuthStatus(true);
            usersRepository.update(user);
            return true;
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User with login " + login + " not found", e);
        }
    }
}