package com.practiceBackend.practiceBackend.modules.post;

import com.amazonaws.services.cloudformation.model.StackInstance;
import com.practiceBackend.practiceBackend.entity.Post;
import com.practiceBackend.practiceBackend.modules.post.dto.CreatePostDTO;
import com.practiceBackend.practiceBackend.modules.post.dto.PostRequestFORlist;
import com.practiceBackend.practiceBackend.modules.post.service.Postservice;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value={"/posts"})
public class PostController {

    private final Postservice postservice;

    @PostMapping
    public ResponseEntity<Map<?,?>> creatPostController(@RequestPart("postRequest") CreatePostDTO postRequest
            , @RequestParam(value = "multipartFiles", required = false)List<MultipartFile> multipartFiles,
                                                        HttpServletRequest request)   {

        Map<String, CreatePostDTO> map = new HashMap<>();
        map.put("data", postRequest);
        postservice.createPost(postRequest, multipartFiles,request);

        return ResponseEntity.ok(map);
    }



    @GetMapping
    public ResponseEntity<Map<String, List<?>>> getPostController(@RequestParam("page") int page, @RequestParam("size") int size)
    {
       List<Post> m =  postservice.postPaging(page, size);

       List<PostRequestFORlist> dtos =PostRequestFORlist.convertListToDTO(m);

        return postservice.reponseServiceForPost(dtos);
    }



    @GetMapping("/count")
    public ResponseEntity<Map<String,List<?>>> getPostControllerCount(
            @RequestParam("type") String type){

       List<Post>li =  postservice.findAll();
       log.info(" @GetMapping(/count)");

        return postservice.reponseServiceForPost(li);


    }

    @GetMapping("/{id}")
   public  ResponseEntity<?> getMappingPosts(@PathVariable("id") Long id) {

       return postservice.getMappingPosts(id);
   }



    @PutMapping("/{id}")
    public ResponseEntity<Map<?,?>> getPostController(@PathVariable("id") Long id, @RequestPart("postRequest") CreatePostDTO postRequest
            ,@RequestParam(value = "multipartFiles", required = false)List<MultipartFile> multipartFiles){
        Map<String, CreatePostDTO> map = new HashMap<>();

        postservice.editPost(postRequest, multipartFiles,id);
        map.put("data", postRequest);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/search/{data}")
    public ResponseEntity<?> searchPosts(
            @PathVariable String data, // URL 경로 변수
            @RequestParam int page,    // 쿼리 파라미터 (page)
            @RequestParam int size     // 쿼리 파라미터 (size)
    ){
        List<Post> m =  postservice.postPaging(page, size);

        List<PostRequestFORlist> dtos =PostRequestFORlist.convertListToDTO(m);

        return postservice.reponseServiceForPost(dtos);
    }

    @PatchMapping("/views/{id}")
    public ResponseEntity<?> patchViews(@PathVariable Long id){
       return this.postservice.getMappingPostsViews(id);

   }


}
