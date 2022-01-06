package com.stewart.building.util;


import org.apache.commons.mail.HtmlEmail;

/**
 * @author Stewart
 * @create 2021/10/2
 */
public class EmailUtils {
    //邮箱验证码
    public static  boolean sendEmail(String emailaddress,String code) {
        try {
            HtmlEmail email = new HtmlEmail();
            //需要修改，126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com
            email.setHostName("smtp.qq.com");
            email.setCharset("UTF-8");
            // 收件地址
            email.addTo(emailaddress);
            //此处填邮箱地址和用户名,用户名可以任意填写
            email.setFrom("1939136076@qq.com", "泉州信息工程学院");
            //此处填写邮箱地址和客户端授权码
            email.setAuthentication("1939136076@qq.com", "rtpsgcfydltsbidj");
            //此处填写邮件名，邮件名可任意填写
            email.setSubject("软件学院老吴团队");
            //此处填写邮件内容
            email.setMsg("尊敬的用户您好,您本次的验证码是:" + code);

            email.send();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
