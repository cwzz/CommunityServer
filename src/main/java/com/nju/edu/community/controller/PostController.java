package com.nju.edu.community.controller;

import com.nju.edu.community.util.ali.AliServiceImpl;
import com.nju.edu.community.blservice.PostBLService;
import com.nju.edu.community.enums.ResultMessage;
import com.nju.edu.community.vo.*;
import com.nju.edu.community.vo.postvo.PostListItem;
import com.nju.edu.community.vo.postvo.SearchReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private AliServiceImpl aliService;

    @Autowired
    private PostBLService postBLService;

    @PostMapping(value = "/getArticleList")
    public @ResponseBody
    ArrayList<PostListItem> getArticleList(@RequestBody SearchReq searchReq){
        System.out.println(searchReq.toString());
        return postBLService.getArticleList(searchReq.getCategory(), searchReq.getLabel());
//        ArrayList<PostListItem> list=new ArrayList<>();
//        list.add(new PostListItem("postid1", true, searchReq.getLabel(), "2019年《专利代理师资格考试办法》公布，详细报考条件说明！",
//                "王旭儿子", 666,100));
//        list.add(new PostListItem("postid1", false, searchReq.getLabel(), "2019年《专利代理师资格考试办法》公布，详细报考条件说明！",
//                "王旭儿子", 666,100));
//        list.add(new PostListItem("postid1", true, searchReq.getLabel(), "2019年《专利代理师资格考试办法》公布，详细报考条件说明！",
//                "王旭儿子", 666,100));
//        list.add(new PostListItem("postid1", false, searchReq.getLabel(), "2019年《专利代理师资格考试办法》公布，详细报考条件说明！",
//                "王旭儿子", 666,100));
//        list.add(new PostListItem("postid1", false, searchReq.getLabel(), "2019年《专利代理师资格考试办法》公布，详细报考条件说明！",
//                "王旭儿子", 666,100));
//        return list;
    }

    @RequestMapping(value = "/changeBaseToUrl")
    public @ResponseBody
    String uploadPicture(@RequestParam String base64, @RequestParam String filename, @RequestParam String projectID) {
        return aliService.uploadPicture(projectID, filename, base64);
    }

    /**
     * @param file 上传的文件
     */
    @PostMapping("/upLoadFile")
    public @ResponseBody
    String upLoadFile(MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        return postBLService.uploadFile(file.getName(), file);
    }

    /**
     * 发帖时先创建ID
     */
    @PostMapping(value = "/createPostID")
    public @ResponseBody
    String createPostID(@RequestParam String author) {
        return postBLService.createID(author);
    }

    /**
     * 发表文章
     * @param publishArticleVO
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/publishArticle")
    public @ResponseBody
    ResultMessage publishArticle(@RequestBody PublishArticleVO publishArticleVO) throws IOException {
        System.err.println("新发布的文章ID："+publishArticleVO.getPostId());
        return postBLService.publishArticle(publishArticleVO.getPostId(), publishArticleVO.getAuthor(), publishArticleVO.getPostTitle(), publishArticleVO.getCategory(),publishArticleVO.getPostTag(), publishArticleVO.getBriefIntro(), publishArticleVO.getContent());
    }

    @PostMapping(value = "/editArticle")
    public @ResponseBody
    ResultMessage editArticle(@RequestBody EditArticleVO editArticleVO) {
        return postBLService.edit(editArticleVO.getPost_id(), editArticleVO.getPost_name(), editArticleVO.getPost_tag(), editArticleVO.getContent());
    }

    @PostMapping(value = "/deleteArticle")
    public @ResponseBody
    ResultMessage deleteArticle(@RequestParam String post_id) {
        return postBLService.deleteArticle(post_id);
    }

    @PostMapping(value = "/remark")
    public @ResponseBody
    ResultMessage remark(@RequestParam String post_id, @RequestParam String reviewer, @RequestParam String remark_content) {
        return postBLService.remark(post_id, reviewer, remark_content);
    }

    @PostMapping(value = "/readArticle")
    public @ResponseBody
    PostVO readArticle(@RequestParam String post_id, @RequestParam String reader) throws IOException {
        return postBLService.readArticle(post_id, reader);
    }


    @PostMapping(value = "/readArticleList")
    public @ResponseBody
    ArrayList<BriefPost> readArticleList(@RequestParam String author) {
        return postBLService.readArticleList(author);
    }

    @PostMapping(value = "/searchArticle")
    public @ResponseBody
    ArrayList<BriefPost> searchArticle(@RequestParam String keywords) {
        return postBLService.searchArticle(keywords);
    }

    @PostMapping(value = "/getAllArticle")
    public @ResponseBody
    ArrayList<BriefPost> getAllArticle() {
        return postBLService.getAllArticleList();
    }


    @RequestMapping(value = "/testDownload", method = RequestMethod.GET)
    public void Download(HttpServletResponse res) {
        String fileName = "1.png";
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File("d://"
                    + fileName)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("success");
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public String Download() {
        return "/fileDownload";
    }

    @RequestMapping(value = "/recommend", method = RequestMethod.GET)
    public ArrayList<RecordVO> getRecommend(String author) {
        return postBLService.recommend(author);
    }


}
