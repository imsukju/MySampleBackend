package com.practiceBackend.practiceBackend.modules.post.service;

import com.practiceBackend.practiceBackend.entity.Attachment;
import com.practiceBackend.practiceBackend.entity.Post;
import com.practiceBackend.practiceBackend.entity.RefreshToken;
import com.practiceBackend.practiceBackend.entity.Tag;
import com.practiceBackend.practiceBackend.modules.login.repository.UserRepository;
import com.practiceBackend.practiceBackend.modules.post.dto.AttachmentResponseDTO;
import com.practiceBackend.practiceBackend.modules.post.dto.CreatePostDTO;
import com.practiceBackend.practiceBackend.modules.post.dto.PostDTO;
import com.practiceBackend.practiceBackend.modules.post.dto.PostRequestFORlist;
import com.practiceBackend.practiceBackend.modules.post.repository.Attechmentrepository;
import com.practiceBackend.practiceBackend.modules.post.repository.PostDao;
import com.practiceBackend.practiceBackend.modules.post.repository.PostRepository;
import com.practiceBackend.practiceBackend.modules.post.repository.TagRepository;
import com.practiceBackend.practiceBackend.modules.s3.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class Postservice {
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final PostDao postDao;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final Attechmentrepository attechmentrepository;

    public void createPost(CreatePostDTO postdto, List<MultipartFile> files, HttpServletRequest request){
        AttachmentResponseDTO attachmentResponseDTO;
        //    private String title;          // 게시글 제목
        //    private String content;        // 게시글 내용
        //    private boolean privatePost;   // 비공개 게시 여부
        //    private boolean blockComment;  // 댓글 차단 여부
        //    private List<String> tags;     // 태그 리스트
        //    private List<Long> deletedFileIds; // 삭제된 파일 ID 리스트{
        Post post =
                Post.builder()
                        .title(postdto.getTitle())
                        .text(postdto.getContent())
                        .isprivate(postdto.isPrivatePost())
                        .isblockComment(postdto.isBlockComment())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .user(userRepository.findByUserid(request.getUserPrincipal().getName()).get())
                        .build();

        try{
            if(postdto.getTags() != null)
            {
                // post 가 tag 의 주인이므로 post 가 저장되기전에 tag 를 저장하면 영속성 문제가 발생 할 수있다
                this.addTaggs(postdto.getTags(),post);
//                post.addTaggs(Arrays.stream(postdto.getTags()).toList());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if(files !=null && !files.isEmpty()){

            List<Attachment> at = files.stream()
                    .map(e -> {
                        Attachment attachment = Attachment.builder()
                                .Filename(e.getOriginalFilename())
                                .s3url(s3Service.uploadFileToS3FromPost(e))
                                .build();
                        attachment.setPost(post); // Post 객체를 설정
                        attechmentrepository.save(attachment);


                        return attachment;
                    })
                    .collect(Collectors.toList());

            post.setAttachments(at);
        }



        postRepository.save(post);



    }

    public void editPost(CreatePostDTO postdto, List<MultipartFile> files,Long id){
        AttachmentResponseDTO attachmentResponseDTO;
//    private String title;          // 게시글 제목
//    private String content;        // 게시글 내용
//    private boolean privatePost;   // 비공개 게시 여부
//    private boolean blockComment;  // 댓글 차단 여부
//    private List<String> tags;     // 태그 리스트
//    private List<Long> deletedFileIds;  // 삭제된 파일 ID 리스트
        if(postRepository.findByPostkey(id).isPresent()){
            Post post = postRepository.findByPostkey(id).get();
            post.setTitle(postdto.getTitle());
            post.setText(postdto.getContent());
            post.setIsprivate(postdto.isPrivatePost());
            post.setIsblockComment(postdto.isBlockComment());
            if(post.getTags() != null){
                post.getTags().clear();

            }

            this.addTaggs(postdto.getTags(),post);




            if(files !=null && !files.isEmpty()){

                List<Attachment> at = files.stream()
                        .map(e -> {
                            Attachment attachment = Attachment.builder()
                                    .Filename(e.getOriginalFilename())
                                    .s3url(s3Service.uploadFileToS3FromPost(e))
                                    .build();
                            attachment.setPost(post); // Post 객체를 설정
                            attechmentrepository.save(attachment);


                            return attachment;
                        })
                        .collect(Collectors.toList());

                post.setAttachments(at);
            }
            postRepository.save(post);
        }



    }



    public List<Post> postPaging(int page, int size){
        List<Post> posts = postDao.getPostByOffset(page, size);

        return posts;
    }

    public List<PostRequestFORlist> postBySeachingname(String name, int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        List<Post> posts = this.postRepository.findByTitleStartsWith(name,pageable);

        List<PostRequestFORlist> re = PostRequestFORlist.convertListToDTO(posts);
        return re;


    }

    public ResponseEntity<Map<String,List<?>>> reponseServiceForPost(List<?> li){
        Map<String,List<?>> map = new HashMap<>();
        map.put("data", li);

        return ResponseEntity.ok().body(map);

    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }


    //여기서 영속문제가 발생할수있음.
    //태그의 이름을 기준으로 확인하지만, 이름이 중복되지 않아도 다른 속성이 같거나 새로운 Tag 객체가 Transient 상태일 가능성이 있습니다.
    public void persistTag(Tag tag){
        if (!tagRepository.existsByTagname(tag.getTagname())){
            tagRepository.save(tag);
        };
    }

    public long countAll(){
        return postRepository.count();
    }



    public void tagadd(Tag tag, Post post) {
        try{
            this.persistTag(tag);
            post.getTags().add(tag);
        }catch (Exception e) {
            e.printStackTrace();
            // logger.error("Error adding tag to post", e); 후에 이걸로 교체
        }
    }

    public void addTaggs(List<String> s, Post post){
        if (s == null || s.isEmpty()) {
            return; // 입력 값이 없으면 아무 작업도 하지 않음
        }

        if(post.getTags() != null)
        {
            Set<String> tagsname = post.getTags().stream()
                    .map(Tag::getTagname)
                    .collect(Collectors.toSet());
            s.stream().filter(e -> !tagsname.contains(e)).map(e -> Tag.builder().tagname(e).build()).forEach(e-> this.tagadd(e, post));
        }else{
            s.stream().map(e -> Tag.builder().tagname(e).build()).forEach(e-> this.tagadd(e, post));
        }

        // Stream<T> filter(Predicate<? super T> predicate); ->
        // Stream<List<String> filter(Predicate<? super String> predicate);

        // boolean test(String t);
        // test 메소드는 !tagsname.contains(e) 이걸 활용해서 참 거짓을 리턴


//    <R> Stream<R> map(Function<? super T, ? extends R> mapper);
        //R apply(T t);
        // 여기서 t는 t스트림을 돌린 List<String> s 임





    }

    public void editTag(List<String> s, Post post) {

        List<Tag> posttags = post.getTags();

        posttags.stream()
                .filter(e ->
                        !s.contains(e.getTagname())
                )
                .forEach(e -> post.removeTag(e));



    }



    public ResponseEntity<?> getMappingPosts(Long id){

        if( postRepository.findByPostkey(id).isPresent()){
            Post poost = postRepository.findByPostkey(id).get();
            PostDTO dto = PostDTO.convertToDTO(poost);
            Map<String,PostDTO> map = new HashMap<>();
            map.put("data",dto);
            return ResponseEntity.ok().body(map);
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }

    @Transactional
    public ResponseEntity<?> getMappingPostsViews(Long id){

        if( postRepository.findByPostkey(id).isPresent()){
            Post poost = postRepository.findByPostkey(id).get();
            poost.setViews(poost.getViews() + 1);
            postRepository.save(poost);
            PostDTO dto = PostDTO.convertToDTO(poost);
            Map<String,PostDTO> map = new HashMap<>();
            map.put("data",dto);
            return ResponseEntity.ok().body(map);
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }

    public List<Post> findPostbyTag(String tagname, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        return this.postRepository.findByTagsTagname(tagname,pageable);

    }

    public Long countPostBytags(String tagname){
        return this.postRepository.countByTagsTagname(tagname);
    }

    public Long countPostBytitle(String title){
        return this.postRepository.countByTitle(title);
    }

    public void deletePostByid(Long id){
        this.postRepository.deleteById(id);
    }

}
