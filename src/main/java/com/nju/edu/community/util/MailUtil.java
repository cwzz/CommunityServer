package com.nju.edu.community.util;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class MailUtil implements Runnable{

    private String emailTo;
    private String code;
    private boolean target;//用于注册-true；用于忘记密码-false

    public MailUtil(String emailTo, String code, boolean target){
        this.emailTo = emailTo;
        this.code = code;
        this.target=target;
    }

    public void run() {

        String mailHost = "smtp.163.com";
        String mailFrom = "ipnet2018@163.com";
        String password = "ipnet2018";

        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", mailHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
//        props.setProperty("mail.smtp.port", "587");           //qq邮箱需要设置端口号

        // 开启 SSL 连接, 以及更详细的发送步骤请看上一篇: 基于 JavaMail 的 Java 邮件发送：简单邮件发送

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
//        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log
        try {
            // 3. 创建一封邮件
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom, "Ipnet", "UTF-8"));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(emailTo, emailTo, "UTF-8"));
            String context;
            if (target) {//注册激活邮件
                message.setSubject("欢迎加入Ipnet", "UTF-8");
                context = createRegisterMessage(code);
            }else {//找回密码邮件
                message.setSubject("Ipnet","UTF-8");
                context = createFindBackPassMessage(code);
            }
            message.setContent(context, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();

            // 4. 根据 Session 获取邮件传输对象
            Transport transport = session.getTransport();

            // 5. 使用 邮箱账号 和 密码 连接邮件服务器
            //    这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
            transport.connect(mailFrom, password);

            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());

            // 7. 关闭连接
            transport.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String createRegisterMessage(String code){
        return  "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>欢迎注册Ipnet</h1>\n" +
                "<p>您的验证码为"+code+"，注意验证码仅一次有效。</p>\n" +
                "</body>\n" +
                "</html>";
    }

    private static String createFindBackPassMessage(String content){
        return "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>您的新密码为"+content+",请重新登录并及时修改密码。</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String generateCode(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static String getRandomPassword(){
        int random = (int)(Math.random()*100000000);
        return String.valueOf(random);
    }
}
