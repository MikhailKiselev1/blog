package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.dto.TagDto;
import main.api.response.TagsResponse;
import main.model.Tag;
import main.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service responsible for handling tag-related operations.
 */
@Service
@RequiredArgsConstructor
public class TagService {


    private final TagRepository tagRepository;

    /**
     * Retrieves tags and their weights based on popularity.
     *
     * @param query the tag query (can be null)
     * @return TagsResponse containing a list of TagDto with tag information
     */
    public TagsResponse getTag(String query) {

        List<Tag> tagList = tagRepository.findAllActualTags();
        TagsResponse tagsResponse = new TagsResponse();
        List<TagDto> finishTagList = new ArrayList<>();

        if (tagList.size() > 0) {
            Tag popularTag = tagList.stream().max(Comparator.comparing(t -> t.getPostsWithTags().size())).get();
            double tagsCount = tagList.size();
            double k = 1.0 / (popularTag.getPostsWithTags().size() / tagsCount);
            if (query == null) {
                tagList.forEach(t -> {
                    TagDto tagDto = new TagDto();
                    tagDto.setName(t.getName());
                    tagDto.setWeight(new BigDecimal(Double.toString
                            (t.getPostsWithTags().size() / tagsCount * k))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    finishTagList.add(tagDto);
                });
            } else {
                tagList.forEach(t -> {
                    if (t.getName().equals(query)) {
                        TagDto tagDto = new TagDto();
                        tagDto.setName(t.getName());
                        tagDto.setWeight(new BigDecimal(Double.toString
                                (t.getPostsWithTags().size() / tagsCount * k))
                                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        finishTagList.add(tagDto);
                    }
                });
            }
            tagsResponse.setTags(finishTagList);
        }
        return tagsResponse;
    }

}
