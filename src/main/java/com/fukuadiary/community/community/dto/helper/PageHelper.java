package com.fukuadiary.community.community.dto.helper;

import com.fukuadiary.community.community.dto.PaginationDTO;

public class PageHelper {

    public static void pagination(PaginationDTO paginationDTO, Integer totalCount, Integer page, Integer size){


        paginationDTO.setPage(page);
        if (totalCount % size == 0){
            paginationDTO.setTotalPage(totalCount / size);
        } else {
            paginationDTO.setTotalPage(totalCount / size + 1);
        }
        if(page < 1){
            page = 1;
        }
        if(page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }

        //i =5, (page = 5) - 3 = 2
        int left = page;
        while (left >= 1){
            paginationDTO.getPages().add(0, left);
            left--;
            if(left == page - 4)
                break;
        }

        //right = 5, totalPage = 6
        int right = page;
        while(right < paginationDTO.getTotalPage()){
            right++;
            paginationDTO.getPages().add(right);
            if(right == page + 3)
                break;
        }


        // 是否展示上一页
        if (page == 1){
            paginationDTO.setShowPrevious(false);
        }else{
            paginationDTO.setShowPrevious(true);
        }

        // 是否展示下一页
        if(paginationDTO.getPage() == paginationDTO.getTotalPage()){
            paginationDTO.setShowNext(false);
        } else {
            paginationDTO.setShowNext(true);
        }

        //是否展示第一页
        if (paginationDTO.getPages().contains(1)){
            paginationDTO.setShowFirstPage(false);
        } else{
            paginationDTO.setShowFirstPage(true);
        }

        //是否展示最后一页
        if(paginationDTO.getPages().contains(paginationDTO.getTotalPage())){
             paginationDTO.setShowEndPage(false);
        } else {
             paginationDTO.setShowEndPage(true);
        }
    }

}
