package com.practiceBackend.practiceBackend.modules.post;

import com.amazonaws.services.cloudformation.model.StackInstance;
import com.practiceBackend.practiceBackend.entity.Post;
import com.practiceBackend.practiceBackend.modules.post.dto.CreatePostDTO;
import com.practiceBackend.practiceBackend.modules.post.dto.PostRequestFORlist;
import com.practiceBackend.practiceBackend.modules.post.service.Postservice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public ResponseEntity<Map<String, List<?>>> getPostController(
            @RequestParam("page") int page, @RequestParam("size") int size)
    {

       List<Post> m =  postservice.postPaging(page, size);

       List<PostRequestFORlist> dtos =PostRequestFORlist.convertListToDTO(m);

        return postservice.reponseServiceForPost(dtos);
    }



    @GetMapping("/count")
    public ResponseEntity<?> getPostControllerCount(
            @RequestParam String type,                  // 타입 (normal 또는 tag)
            @RequestParam(required = false) String data // data는 type이 tag일 때만 필요
    ){

        Map<String,Long> map = new HashMap<>();
        long l = 0;
        if(type.equals("normal")){
            l =  postservice.countAll();
        }else if(type.equals("tag")){
             l = postservice.countPostBytags(data);
        }else if(type.equals("search")){
            l = postservice.countPostBytitle(data);
        }

        map.put("data",l);

        return ResponseEntity.ok(map);


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
            @RequestParam("page") int page, @RequestParam("size") int size
    ){
        List<PostRequestFORlist> dtos = this.postservice.postBySeachingname(data, page, size);
        return postservice.reponseServiceForPost(dtos);
    }

    @PatchMapping("/views/{id}")
    public ResponseEntity<?> patchViews(@PathVariable Long id){
       return this.postservice.getMappingPostsViews(id);

   }

   @GetMapping("/search/tags/{tag}")
    public ResponseEntity<?> searchByTag(@PathVariable String tag,
                               @RequestParam int page,
                               @RequestParam int size){
        List<Post> posts = this.postservice.findPostbyTag(tag,page,size);

       List<PostRequestFORlist> dtos =PostRequestFORlist.convertListToDTO(posts);

       return postservice.reponseServiceForPost(dtos);

   }

   @Transactional
   @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
       Map<String,String> map = new HashMap<>();
       try{
           this.postservice.deletePostByid(id);
           map.put("message","삭제완료");
           return ResponseEntity.ok(map);
       }catch (Exception e){
           map.put("message",e.getMessage());
           return ResponseEntity.badRequest().body(map);
       }




   }




}
