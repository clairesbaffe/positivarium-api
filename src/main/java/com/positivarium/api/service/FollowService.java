package com.positivarium.api.service;

import com.positivarium.api.dto.UserDTO;
import com.positivarium.api.entity.User;
import com.positivarium.api.mapping.UserMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserService userService;
    private final UserMapping userMapping;

    public void followPublisher(Long publisherId, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) throw new Exception("User not found");

        User user = userService.getUser(username);
        User publisher = userService.findUserById(publisherId);

        List<String> publisherRoles = userService.getUserRoles(publisher.getUsername());
        if(!publisherRoles.contains("ROLE_PUBLISHER"))
            throw new Exception("Only publishers can be followed");

        user.getFollowing().add(publisher);
        publisher.getFollowers().add(user);

        userService.updateUser(user);
        userService.updateUser(publisher);
    }

    public void unfollowPublisher(Long publisherId, Authentication authentication) throws Exception {
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) throw new Exception("User not found");

        User user = userService.getUser(username);
        Long userId = user.getId();

        if(!userService.following(userId, publisherId)) throw new Exception("Publisher is not followed");

        User publisher = userService.findUserById(publisherId);

        user.getFollowing().remove(publisher);
        publisher.getFollowers().remove(user);

        userService.updateUser(user);
        userService.updateUser(publisher);
    }

    public Page<UserDTO> getFollowing(int pageNumber, int pageSize, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if (username == null) return null;

        User user = userService.getUser(username);
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> following = userService.findFollowing(userId, pageable);
        return following.map(userMapping::entityToDto);
    }
}
