package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.result.Result;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;
import java.util.List;


@Controller
@RequestMapping(value = "image")
@Api(tags = {"图片"})
@Slf4j
public class ImageController {

    private final static String path = "D:/home/img/";

    @PostMapping("saveLbImage")
    @ApiOperation(value = "保存轮播图")
    @ResponseBody
    public Result saveLbImage(@RequestParam(value = "multipartFile") MultipartFile multipartFile) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
        String name = multipartFile.getOriginalFilename();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path+name));

        byte[] bs = new byte[1024];
        int len;
        while ((len= fileInputStream.read(bs))!=-1){
            bos.write(bs,0,len);
        }
        bos.flush();
        bos.close();
        return new Result(name);
    }

    @GetMapping(value = "getLbImage",produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_PNG_VALUE})
    @ApiOperation(value = "获取轮播图")
    public Result getLbImage() throws IOException{
        File file = new File(path);
        File files[] = file.listFiles();
        List<BufferedImage> images = new LinkedList<>();
        for(File file1 : files){
            images.add(ImageIO.read(new FileInputStream(file1)));
        }

        return new Result(images);
    }

    @GetMapping("delLbImage")
    @ApiOperation(value = "删除轮播图")
    @ResponseBody
    public Result delLbImage(String fileName){
        File file = new File(path+fileName);
        file.delete();
        return new Result("OK");
    }



}
