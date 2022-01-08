package com.stewart.building.mbg.controller.other;

import com.stewart.building.Listener.ExcelEntity;
import com.stewart.building.common.R;
import com.stewart.building.util.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 陈鸿杰
 * @create 2022/1/8
 * @Description
 */
@Api(tags = "excel模块")
@RestController
@RequestMapping("/excel")
public class ExcelController {
    private static final Logger log = LoggerFactory.getLogger(ExcelController.class);

    /**
     * 单个文件上传
     * @param file
     * @return
     */
    @ApiOperation(value="上传excel",httpMethod="POST",notes="单个文件上传")
    @RequestMapping(value = "/upload")
    public R upload(@RequestParam("file") MultipartFile file) {
        R msg = new R();
        try {
            if (file.isEmpty()) {
                msg.setMsg("文件为空");
                return msg;
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            log.info("文件的后缀名为：" + suffixName);
            //创建一个临时文件，用于暂时存放
            File tmpFile = File.createTempFile("tmp", null);
            //将MultipartFile 转换为 File 临时文件
            file.transferTo(tmpFile);
            //将临时文件转为输入流
            InputStream inputStream = new FileInputStream(tmpFile);
            ExcelUtils.readExcel(inputStream, ExcelEntity.class);
            msg.setMsg("上传成功");
            //上传完成 删除临时文件
            tmpFile.delete();
            return msg;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        msg.setMsg("上传失败");
        return msg;
    }

}
