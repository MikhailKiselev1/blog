package main.service;

import main.api.response.dto.TagDto;
import main.api.response.TagsResponse;
import main.model.Tag;
import main.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TagService {


    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagsResponse getTag(String query) {

        List<Tag> tagList = tagRepository.findAll();
        TagsResponse tagsResponse = new TagsResponse();
        List<TagDto> finishTagList = new ArrayList<>();

        if (tagList.size() > 0) {
            Tag popularTag = tagList.stream().max(Comparator.comparing(t -> t.getPostsWithTags().size())).get();
            int tagsCount = tagList.size();
            double k = Math.round(1 / (popularTag.getPostsWithTags().size() / tagsCount));
            if (query == null) {
                tagList.forEach(t -> {
                    TagDto tagDto = new TagDto();
                    tagDto.setName(t.getName());
                    tagDto.setWeight(Math.round(t.getPostsWithTags().size() / tagsCount * k));
                    finishTagList.add(tagDto);
                });
                tagsResponse.setTags(finishTagList);
            } else {
                tagList.forEach(t -> {
                    if (t.getName().equals(query)) {
                        TagDto tagDto = new TagDto();
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
