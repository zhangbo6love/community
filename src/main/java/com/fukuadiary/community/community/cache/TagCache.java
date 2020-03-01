package com.fukuadiary.community.community.cache;

import com.fukuadiary.community.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("Java", "c#", "javascript", "HTML5", "css", "微信小程序", "python"));
        tagDTOS.add(program);

        TagDTO frameWork = new TagDTO();
        frameWork.setCategoryName("平台框架");
        frameWork.setTags(Arrays.asList("spring", "spring boot", "mybatis", "vue"));
        tagDTOS.add(frameWork);


        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("tomcat", "linux", "apache", "nginx","windows-server"));
        tagDTOS.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql", "h2", "oracle", "sqlserver","redis","sql"));
        tagDTOS.add(db);

        TagDTO tools = new TagDTO();
        tools.setCategoryName("开发工具");
        tools.setTags(Arrays.asList("git", "idea", "eclipse", "sublime-text","visual-studio","maven","visual-studio-code"));
        tagDTOS.add(tools);

        return tagDTOS;
    }


    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
