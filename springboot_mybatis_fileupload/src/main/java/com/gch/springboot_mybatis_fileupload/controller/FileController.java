package com.gch.springboot_mybatis_fileupload.controller;

import com.gch.springboot_mybatis_fileupload.domain.User;
import com.gch.springboot_mybatis_fileupload.domain.UserFile;
import com.gch.springboot_mybatis_fileupload.service.UserFileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("file")
public class FileController {
    @Resource
    private UserFileService userFileService;

    /*
     * 上传文件，并保存数据库
     * */
    @PostMapping("upload")
    public String upload(MultipartFile aaa, HttpSession session) throws IOException {
        //获取用户id
        User user = (User) session.getAttribute("user");
        //获取文件原始名称
        String oldFileName = aaa.getOriginalFilename();
        /* 获取文件后缀 */
        String extension = "." + FilenameUtils.getExtension(aaa.getOriginalFilename());
        //生成新文件名称
        String newFileName =
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                        UUID.randomUUID().toString().replace("-", "").substring(0, 8) + extension;
        //文件的大小
        long size = aaa.getSize();
        //文件的类型
        String type = aaa.getContentType();
        //处理根据日期生成目录
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static/files";
        String dataformat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateDirPath = realPath + "/" + dataformat;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) dateDir.mkdirs();

        //处理文件上传
        aaa.transferTo(new File(dateDir, newFileName));

        //将文件信息存入数据库
        UserFile userFile = new UserFile();
        userFile.setOldFileName(oldFileName)
                .setNewFileName(newFileName)
                .setExt(extension)
                .setSize(String.valueOf(size))
                .setType(type)
                .setPath("/files/" + dataformat)
                .setUserId(user.getId());
        userFileService.save(userFile);
        return "redirect:/file/findAll";
    }

    /*
     * 文件下载
     * */
    @GetMapping("download")
    public void download(String openStyle, Integer id, HttpServletResponse response) throws IOException {
        //null就是下载   不为null就是在线打开
        openStyle = openStyle == null ? "attachment" : openStyle;

        UserFile userFile = userFileService.findByID(id);
        //点击下载才更新下载次数  打开不更新次数
        if ("attachment".equals(openStyle)) {
            userFile.setDowncounts(userFile.getDowncounts() + 1);
            userFileService.update(userFile);
        }
        //获取文件真实路径
        String realpath = ResourceUtils.getURL("classpath:").getPath() + "/static" + userFile.getPath();
        //获取文件输入流
        FileInputStream is = new FileInputStream(new File(realpath, userFile.getNewFileName()));
        //附件下载
        response.setHeader("content-disposition", openStyle + ";fileName" + URLEncoder.encode(userFile.getOldFileName(), "UTF-8"));

        //获取响应输出流
        ServletOutputStream os = response.getOutputStream();
        //文件拷贝
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);


    }


    @GetMapping("findAll")
    public String findAll(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<UserFile> userFiles = userFileService.findByUserId(user.getId());
        model.addAttribute("files", userFiles);
        return "showAll";
    }

    @GetMapping("findAllJson")   //实现页面定时刷新
    @ResponseBody
    public List<UserFile> findAllJson(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<UserFile> userFiles = userFileService.findByUserId(user.getId());
        return userFiles;
    }

    /*
     * 删除文件
     * */
    @GetMapping("delete")
    public String delete(Integer id) throws FileNotFoundException {
        UserFile userFile = userFileService.findByID(id);
        //删除文件
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static" + userFile.getPath();
        File file = new File(realPath, userFile.getNewFileName());
        if (file.exists()) file.delete(); //立即删除

        //删除数据库中记录
        userFileService.delete(id);
        return "redirect:/file/findAll";

    }
}
