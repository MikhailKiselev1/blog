package main.api.response;

import main.api.response.enums.PostMode;

public class PostResponce {
    private int offset;
    private int limit;
    private PostMode mode;

    public PostResponce() {
        offset = 0;
        limit = 10;
        mode = PostMode.RECENT;
    }


}
