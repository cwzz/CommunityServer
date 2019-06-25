package com.nju.edu.community.bl;

import com.nju.edu.community.blservice.MessageBLService;
import com.nju.edu.community.dao.RemarkDao;
import com.nju.edu.community.util.ali.AliServiceImpl;
import com.nju.edu.community.blservice.CommunityUserBLService;
import com.nju.edu.community.blservice.PostBLService;
import com.nju.edu.community.blservice.UserBLService;
import com.nju.edu.community.dao.PostDao;
import com.nju.edu.community.entity.Post;
import com.nju.edu.community.entity.Remark;
import com.nju.edu.community.enums.PostState;
import com.nju.edu.community.enums.ResultMessage;
import com.nju.edu.community.vo.postvo.PostVO;
import com.nju.edu.community.vo.RecordVO;
import com.nju.edu.community.vo.postvo.PostListItem;
import com.nju.edu.community.vo.uservo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class PostBL implements PostBLService {
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserBLService userBLService;
    @Autowired
    private CommunityUserBLService communityUserBLService;
    @Autowired
    private AliServiceImpl aliService;
    @Autowired
    private RemarkDao remarkDao;
    @Autowired
    private MessageBLService messageBLService;

    @Override
    public ArrayList<PostListItem> getArticleList(String category, String tag) {
        ArrayList<Post> posts=new ArrayList<>();
        if (category.equals("全部")&&tag.equals("全部")){
            posts=postDao.searchAllArticle();
        }else if (tag.equals("全部")){
            System.out.println("分类是全部");
            posts=postDao.searchArticleByCategory(category);
        }else {
            System.out.println("都不是全部");
            postDao.searchArticleByTag(category, tag);
        }
        ArrayList<PostListItem> list=new ArrayList<>();
        for (Post post: posts){
            list.add(new PostListItem(post));
        }
        return list;
    }

    @Override
    public ArrayList<PostListItem> getMyRelease(String email){
        ArrayList<Post> posts=postDao.getPostByAuthor(email);
        ArrayList<PostListItem> results=new ArrayList<>();
        for (Post post:posts){
            results.add(new PostListItem(post));
        }
        return results;
    }

    @Override
    public ArrayList<PostListItem> getMyCollectPost(ArrayList<String> postIds){
        ArrayList<PostListItem> result=new ArrayList<>();
        for (String s:postIds){
            result.add(new PostListItem(postDao.getOne(s)));
        }
        return result;
    }


    @Override
    public String createID(String author) {
        Post post=new Post(author);
        postDao.save(post);
        return post.getPid();
    }

    private void saveAsFile(String content) throws IOException {
        String saveFile = "test.txt";
        File file=new File(saveFile);
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(saveFile);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String uploadFile(String post_id, MultipartFile file) {
        String filename = post_id+".txt";
        String uploadUrl="";
        File newFile = new File(filename);
        try {
            if (file!=null) {
                if (!"".equals(filename.trim())) {
                    FileOutputStream os = new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
                    file.transferTo(newFile);
                    // 上传到OSS
                    uploadUrl = aliService.upLoad(newFile);
                    if(uploadUrl.charAt(7)!='/'){
                        String str1=uploadUrl.substring(7);
                        uploadUrl="https://"+str1;
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            newFile.delete();
        }
        return uploadUrl;
    }


    @Override
    public ResultMessage publishArticle(String postID, String author, String postTitle, String category,
                                        String postTag, String briefIntro, String content) throws IOException {
        this.saveAsFile(content);
        File file=new File("test.txt");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain",input);
        String content_url=uploadFile(postID,multipartFile);
        Post post=postDao.getOne(postID);
        post.setAuthor(author);
        post.setTitle(postTitle);
        post.setCategory(category);
        post.setPostTag(postTag);
        post.setBriefIntro(briefIntro);
        post.setContentUrl(content_url);
        post.setPublishTime(new Date().getTime());
        post.setVisits(0);
        post.setRemarkNum(0);
        post.setRemarkList(new ArrayList<>());
        post.setState(PostState.Published);
        post.setSetTop(false);
        postDao.save(post);
        communityUserBLService.releasePost(author);
        return ResultMessage.Success;
    }



    @Override
    public ResultMessage edit(String post_id, String post_name, String post_tag, String content) {
        Post post=postDao.getOne(post_id);
        post.setTitle(post_name);
        post.setPostTag(post_tag);
        post.setContentUrl(content);
        postDao.save(post);
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage deleteArticle(String post_id) {
        Post post=postDao.getOne(post_id);
        postDao.deleteById(post_id);
//        communityUserBLService.deletePost(post.getAuthor(),post_id);
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage remark(String post_id, String reviewer, String remark_content) {
        UserInfoVO userInfoVO=userBLService.getUserInfo(reviewer);
        Remark remark=new Remark(post_id,reviewer,remark_content,userInfoVO.getImageUrl(),userInfoVO.getNickname());
        Post post=postDao.getOne(post_id);
        post.setRemarkNum(post.getRemarkNum()+1);
        List<Remark> remarks=post.getRemarkList();
        remarks.add(remark);
        post.setRemarkList(remarks);
        postDao.save(post);
        messageBLService.generateMessage(post.getAuthor(),reviewer+"评论了您的文章:《"+post.getTitle()+"》");
        return ResultMessage.Success;
    }

    @Override
    public PostVO readArticle(String post_id) throws IOException {
        Post post=postDao.getOne(post_id);
        String content=downLoadFromUrl(post.getContentUrl());
        post.setVisits(post.getVisits()+1);
        postDao.save(post);
        return new PostVO(post, content,remarkDao.getRemarksOrderByTime(post_id));
    }



    @Override
    public ArrayList<RecordVO> recommend(String author) {
        ArrayList<Post> posts=new ArrayList<>(postDao.findAll());
        ArrayList<RecordVO> recordVOS=new ArrayList<>();
        ArrayList<RecordVO> res=new ArrayList<>();
        for(Post p:posts){
            if(!p.getAuthor().equals(author)){
                recordVOS.add(new RecordVO(p.getPid(),p.getTitle(),userBLService.getImageUrl(p.getAuthor())));
            }
        }
        Collections.shuffle(recordVOS);//打乱数组
        if(recordVOS.size()<=7){
            return recordVOS;
        }else{
            for(int i=0;i<7;i++){
                res.add(recordVOS.get(i));
            }
            return res;
        }
    }


    @Override
    @Transactional
    public ResultMessage addInterestNum(String post_id) {
        postDao.addInterestNum(post_id);
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage minusInterestNum(String post_id) {
        postDao.minusInterestNum(post_id);
        return ResultMessage.Success;
    }

    private  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    @Override
    //根据远端url下载文件到指定路径,然后读出文件的内容，再将文件删除
    public String downLoadFromUrl(String urlStr) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        //文件保存位置
//        File saveDir = new File("D:");
//        if(!saveDir.exists()){
//            saveDir.mkdir();
//        }
//        File file = new File(saveDir+File.separator+fileName);
        File file=new File("temp.txt");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }
        System.out.println("info:"+url+" download success");

        BufferedReader in = null;
        BufferedWriter out = null;
        String res="";
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"));
            String line = "";
            while((line = in.readLine())!=null){
                res=res+line+"\r\n";
//                System.out.println(line);
            }
            System.out.println(res);
        } catch (FileNotFoundException e) {
            System.out.println("file is not fond");
        } catch (IOException e) {
            System.out.println("Read or write Exceptioned");
        }finally{
            if(null!=in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }}
            if(null!=out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            file.delete();
        }
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
//        if (file.exists() && file.isFile()) {
//            if (file.delete()) {
//                System.out.println("删除单个文件成功！");
//            } else {
//                System.out.println("删除单个文件失败！");
//            }
//        } else {
//            System.out.println("删除单个文件失败,不存在！");
//        }
        return res;
    }
}
