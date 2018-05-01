package com.app.temp.network.model;

import com.app.temp.pojo.Post;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nguyen_van_cuong on 13/11/2017.
 */

public class PostsResponse {
    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void add(Post post) {
        if (posts == null) {
            posts = new LinkedList<>();
        }
        posts.add(post);
    }
}
