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
    private final static String path2 = "D:/home/img2/";

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

    @GetMapping(value = "getLbImage", consumes = MediaType.ALL_VALUE,produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_PNG_VALUE})
    @ApiOperation(value = "获取轮播图")
    @ResponseBody
    public byte[] getLbImage(String name) throws IOException{
        File file = new File(path + name);
        if(!file.exists()){
            return "该文件不存在！".getBytes();
        }else {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        }
    }

    @GetMapping("delLbImage")
    @ApiOperation(value = "删除轮播图")
    @ResponseBody
    public Result delLbImage(String fileName){
        File file = new File(path+fileName);
        file.delete();
        return new Result("OK");
    }

    @GetMapping("findLbImage")
    @ApiOperation(value = "获取所有轮播图名称")
    @ResponseBody
    public Result findLbImage(){
        File file = new File(path);
        File files[] = file.listFiles();
        List<String> images = new LinkedList<>();
        for(File file1 : files){
            images.add(file1.getName());
        }
        return new Result(images);
    }

    @PostMapping("saveGgImage")
    @ApiOperation(value = "保存广告图")
    @ResponseBody
    public Result saveGgImage(@RequestParam(value = "multipartFile") MultipartFile multipartFile) throws IOException {
        File file = new File(path2);
        if(!file.exists()){
            file.mkdirs();
        }
        FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
        String name = multipartFile.getOriginalFilename();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path2+name));

        byte[] bs = new byte[1024];
        int len;
        while ((len= fileInputStream.read(bs))!=-1){
            bos.write(bs,0,len);
        }
        bos.flush();
        bos.close();
        return new Result(name);
    }

    @GetMapping(value = "getGgImage", consumes = MediaType.ALL_VALUE,produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_PNG_VALUE})
    @ApiOperation(value = "获取广告图")
    @ResponseBody
    public byte[] getGgImage(String name) throws IOException{
        File file = new File(path2 + name);
        if(!file.exists()){
            return "该文件不存在！".getBytes();
        }else {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        }
    }

    @GetMapping("delGgImage")
    @ApiOperation(value = "删除广告图")
    @ResponseBody
    public Result delGgImage(String fileName){
        File file = new File(path2+fileName);
        file.delete();
        return new Result("OK");
    }

    @GetMapping("findGgImage")
    @ApiOperation(value = "获取所有广告图名称")
    @ResponseBody
    public Result findGgImage(){
        File file = new File(path2);
        File files[] = file.listFiles();
        List<String> images = new LinkedList<>();
        for(File file1 : files){
            images.add(file1.getName());
        }
        return new Result(images);
    }

}
