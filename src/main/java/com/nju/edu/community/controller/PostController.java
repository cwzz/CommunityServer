package com.nju.edu.community.controller;

import com.nju.edu.community.util.ali.AliServiceImpl;
import com.nju.edu.community.blservice.PostBLService;
import com.nju.edu.community.enums.ResultMessage;
import com.nju.edu.community.vo.*;
import com.nju.edu.community.vo.postvo.*;
import com.nju.edu.community.vo.remarkvo.RemarkVO;
import com.nju.edu.community.vo.uservo.StringVO;
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

    @RequestMapping(value = "/searchPost")
    public @ResponseBody
    ArrayList<PostListItem> searchByKeywords(@RequestBody StringVO stringVO){
        return postBLService.searchByKeywords(stringVO.getEmail());
    }

    /**
     * 根据分类和标签查找文章
     */
    @PostMapping(value = "/getArticleList")
    public @ResponseBody
    ArrayList<PostListItem> getArticleList(@RequestBody SearchReq searchReq){
        return postBLService.getArticleList(searchReq.getCategory(), searchReq.getLabel());
    }

    /**
     * 上传文章中的图片获得url
     */
    @RequestMapping(value = "/changeBaseToUrl",method = RequestMethod.POST)
    public @ResponseBody
    String uploadPicture(@RequestBody UploadPicVO uploadPicVO) {
        return aliService.uploadPicture(uploadPicVO.getProjectID(),uploadPicVO.getFilename(),uploadPicVO.getBase64());
    }

    /**
     * 上传文件
     */
    @RequestMapping(value="/upLoadFile",method = RequestMethod.POST)
    public @ResponseBody
    String upLoadFile(MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        return postBLService.uploadFile(file.getName(), file);
    }

    /**
     * 发帖时先创建ID
     */
    @RequestMapping(value = "/createPostID",method = RequestMethod.POST)
    public @ResponseBody
    String createPostID(@RequestBody Author author) {
        return postBLService.createID(author.getAuthor());
    }

    /**
     * 发表文章
     */
    @RequestMapping(value = "/publishArticle",method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage publishArticle(@RequestBody PublishArticleVO publishArticleVO) throws IOException {
        System.err.println("yes");
        System.err.println("新发布的文章ID："+publishArticleVO.getPostId());
        return postBLService.publishArticle(publishArticleVO.getPostId(), publishArticleVO.getAuthor(), publishArticleVO.getPostTitle(), publishArticleVO.getCategory(),publishArticleVO.getPostTag(), publishArticleVO.getBriefIntro(), publishArticleVO.getContent());
    }

    /**
     * 根据ID读取具体文章信息
     */
    @RequestMapping(value = "/readArticle",method = RequestMethod.POST)
    public @ResponseBody
    PostVO readArticle(@RequestBody PostID postID) throws IOException {
        return postBLService.readArticle(postID.getPostID());
    }

    /**
     * 编辑文章，现在没有这个功能
     */
    @RequestMapping(value = "/editArticle",method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage editArticle(@RequestBody EditArticleVO editArticleVO) {
        return postBLService.edit(editArticleVO.getPost_id(), editArticleVO.getPost_name(), editArticleVO.getPost_tag(), editArticleVO.getContent());
    }

    /**
     * 删除文章，现在没有这个功能
     */
    @RequestMapping(value = "/deleteArticle",method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage deleteArticle(@RequestParam String post_id) {
        return postBLService.deleteArticle(post_id);
    }

    /**
     * 评论文章
     */
    @RequestMapping(value = "/remark",method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage remark(@RequestBody RemarkVO remarkVO) {
        return postBLService.remark(remarkVO.getPost_id(), remarkVO.getReviewer(), remarkVO.getRemark_content());
    }

    @RequestMapping(value = "/testDownload", method = RequestMethod.POST)
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

    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    public ArrayList<RecordVO> getRecommend(String author) {
        return postBLService.recommend(author);
    }


}
