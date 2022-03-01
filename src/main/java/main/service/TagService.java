package main.service;

import main.api.response.dto.TagDto;
import main.api.response.TagsResponse;
import main.model.Tag;
import main.repositories.TagRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TagService {

    private TagsResponse tagsResponse;
    private TagDto tagDto;
    private TagRepository tagRepository;
    List<Tag> tagList;
    List<TagDto> finishTagList;
    private Tag popularTag;
    private int tagsCount;
    private double k;

    public TagsResponse getTag(String query) {
        tagList = new ArrayList<>();
        tagsResponse = new TagsResponse();
        finishTagList = new ArrayList<>();
        Iterable<Tag> tagsOptional = tagRepository.findAll();
        tagsOptional.forEach(t -> tagList.add(t));
        if(tagList.size() > 0) {
            popularTag = tagList.stream().max(Comparator.comparing(t -> t.getPostsWithTags().size())).get();
            tagsCount = tagList.size();
            k = Math.round(1 / (popularTag.getPostsWithTags().size() / tagsCount));
            if (query == null) {
                tagList.forEach(t -> {
                    tagDto = new TagDto();
                    tagDto.setName(t.getName());
                    tagDto.setWeight(Math.round(t.getPostsWithTags().size() / tagsCount * k));
                    finishTagList.add(tagDto);
                });
                tagsResponse.setTags(finishTagList);
            } else {
                tagList.forEach(t -> {
                    if (t.getName().equals(query)) {
                        tagDto = new TagDto();
                        tagDto.setName(t.getName());
                        tagDto.setWeight(Math.round(t.getPostsWithTags().size() / tagsCount * k));
                        finishTagList.add(tagDto);
                    }
                });
                tagsResponse.setTags(finishTagList);
            }
        }
        return tagsResponse;
    }
}
