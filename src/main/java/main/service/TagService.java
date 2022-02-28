package main.service;

import main.api.response.dto.TagDto;
import main.api.response.TagsResponse;
import main.model.Tag;
import main.repositories.TagRepository;
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
        tagsResponse = new TagsResponse();
        finishTagList = new ArrayList<>();
        tagDto = new TagDto();
        tagDto.setName("Java");
        tagDto.setWeight(1);
        finishTagList.add(tagDto);
        TagDto tagDto1 = new TagDto();
        tagDto1.setName("Spring");
        tagDto1.setWeight(0.56);
        finishTagList.add(tagDto1);
        TagDto tagDto2 = new TagDto();
        tagDto2.setName("Hibernate");
        tagDto2.setWeight(0.22);
        finishTagList.add(tagDto2);
        TagDto tagDto3 = new TagDto();
        tagDto3.setName("Hadoop");
        tagDto3.setWeight(0.17);
        finishTagList.add(tagDto3);
        tagsResponse.setTags(finishTagList);
        return tagsResponse;
    }

//    public TagsResponse getTag(String query) {
//        tagList = new ArrayList<>();
//        tagsResponse = new TagsResponse();
//        finishTagList = new ArrayList<>();
//        Iterable<Tag> tagsOptional = tagRepository.findAll();
//        tagsOptional.forEach(t -> tagList.add(t));
//        popularTag = tagList.stream().max(Comparator.comparing(t -> t.getPostsWithTags().size())).get();
//        tagsCount = tagList.size();
//        k = Math.round(1 / (popularTag.getPostsWithTags().size() / tagsCount));
//        if (query == null) {
//            tagList.forEach(t -> {
//                tagDto = new TagDto();
//                tagDto.setName(t.getName());
//                tagDto.setWeight(Math.round(t.getPostsWithTags().size() / tagsCount * k));
//                finishTagList.add(tagDto);
//            });
//            tagsResponse.setTags(finishTagList);
//        }
//        else {
//            tagList.forEach(t -> {
//                if (t.getName().equals(query)) {
//                    tagDto = new TagDto();
//                    tagDto.setName(t.getName());
//                    tagDto.setWeight(Math.round(t.getPostsWithTags().size() / tagsCount * k));
//                    finishTagList.add(tagDto);
//                }
//            });
//            tagsResponse.setTags(finishTagList);
//        }
//        return tagsResponse;
//    }
}
