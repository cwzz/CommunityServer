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

    @RequestMapping(value = "/changeBaseToUrl",method = RequestMethod.POST)
    public @ResponseBody
    String uploadPicture(@RequestBody UploadPicVO uploadPicVO) {
        System.err.println("yes");
        System.err.println(uploadPicVO.getBase64()+","+uploadPicVO.getFilename()+","+uploadPicVO.getProjectID());
        return aliService.uploadPicture(uploadPicVO.getProjectID(),uploadPicVO.getFilename(),uploadPicVO.getBase64());
    }

    /**
     * @param file 上传的文件
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
    String createPostID(@RequestBody Author_name author) {
        System.err.println("？？？？");
        String s=postBLService.createID(author.getAuthor());
        System.err.println("文章ID："+s);
        return s;
    }

    /**
     * 发表文章
     * @param publishArticleVO
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/publishArticle",method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage publishArticle(@RequestBody PublishArticleVO publishArticleVO) throws IOException {
        System.err.println("yes");
        System.err.println("新发布的文章ID："+publishArticleVO.getPostId());
        return postBLService.publishArticle(publishArticleVO.getPostId(), publishArticleVO.getAuthor(), publishArticleVO.getPostTitle(), publishArticleVO.getCategory(),publishArticleVO.getPostTag(), publishArticleVO.getBriefIntro(), publishArticleVO.getContent());
    }

    @RequestMapping(value = "/editArticle",method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage editArticle(@RequestBody EditArticleVO editArticleVO) {
        return postBLService.edit(editArticleVO.getPost_id(), editArticleVO.getPost_name(), editArticleVO.getPost_tag(), editArticleVO.getContent());
    }

    @RequestMapping(value = "/deleteArticle",method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage deleteArticle(@RequestParam String post_id) {
        return postBLService.deleteArticle(post_id);
    }

    @RequestMapping(value = "/remark",method = RequestMethod.POST)
    public @ResponseBody
    ResultMessage remark(@RequestParam String post_id, @RequestParam String reviewer, @RequestParam String remark_content) {
        return postBLService.remark(post_id, reviewer, remark_content);
    }

    @RequestMapping(value = "/readArticle",method = RequestMethod.POST)
    public @ResponseBody
    PostVO readArticle(@RequestParam String post_id, @RequestParam String reader) throws IOException {
        return postBLService.readArticle(post_id, reader);
    }

    @RequestMapping(value = "/readArticleList",method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<BriefPost> readArticleList(@RequestParam String author) {
        return postBLService.readArticleList(author);
    }

    @RequestMapping(value = "/searchArticle",method = RequestMethod.POST)
    public @ResponseBody
    ArrayList<BriefPost> searchArticle(@RequestParam String keywords) {
        return postBLService.searchArticle(keywords);
    }

    @RequestMapping(value = "/getAllArticle",method = RequestMethod.POST)
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
