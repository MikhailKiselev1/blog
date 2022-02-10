package main.model.service;

import main.api.response.TagResponse;
import main.api.response.TagsResponse;
import main.model.Tag;
import main.model.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private TagsResponse tagsResponse;
    private TagResponse tagResponse;
    private TagRepository tagRepository;
    List<Tag> tagList;
    List<TagResponse> finishTagList;
    private Tag popularTag;
    private int tagsCount;
    private double k;

    public TagsResponse getTag() {
        tagList = new ArrayList<>();
        tagsResponse = new TagsResponse();
        finishTagList = new ArrayList<>();
        Iterable<Tag> tagsOptional = tagRepository.findAll();
        tagsOptional.forEach(t -> tagList.add(t));
        popularTag = tagList.stream().max(Comparator.comparing(t -> t.getPostsWithTags().size())).get();
        tagsCount = tagList.size();
        k = Math.round(1 / (popularTag.getPostsWithTags().size() / tagsCount));
        tagList.forEach(t -> {
            tagResponse = new TagResponse();
            tagResponse.setName(t.getName());
            tagResponse.setWeight(Math.round(t.getPostsWithTags().size() / tagsCount * k));
            finishTagList.add(tagResponse);
        });
        tagsResponse.setTags(finishTagList);
        return tagsResponse;
    }
}
