package com.practiceBackend.practiceBackend.entity;

import com.practiceBackend.practiceBackend.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_KEY")
    private Long userkey;

    private String userid;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "PROFILE_KEY")
    private Profile profile;

    private UserRole userRole;

    private String profileImage;

    public static UserBuilder builder() {
        return new UserBuilder();
    }


    public static class UserBuilder {
        private Long userkey;
        private String userid;
        private String password;
        private List<Post> posts = new ArrayList<>();
        private List<Comment> comments = new ArrayList<>();
        private Profile profile;
        private String profileImage;

        public UserBuilder userkey(Long userkey) {
            this.userkey = userkey;
            return this;
        }
        public UserBuilder userid(String userid) {
            this.userid = userid;
            return this;
        }
        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder posts(List<Post> posts) {
            this.posts = posts;
            return this;
        }
        public UserBuilder comments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }
        public UserBuilder profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public UserBuilder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }
        public User build() {
            User user = new User();
            user.setUserid(userid);
            user.setPassword(password);
            user.setPosts(posts);
            user.setComments(comments);
            user.setUserkey(userkey);
            user.setProfile(profile);
            user.setProfileImage(profileImage);
            return user;

        }



    }
}
