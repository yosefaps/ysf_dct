package ysf.app.dct.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ysf.app.dct.lib.DCT;

@Controller
public class HomeController {

    private static String UPLOADED_FOLDER = "/tmp/";
    private static String FULL_PATH;
    private DCT dct;

    @PostConstruct
    public void initialize() {
        this.FULL_PATH = Paths.get("").toAbsolutePath().toString() + UPLOADED_FOLDER;
//        System.out.println("FULL PATH: " + FULL_PATH);
        dct = new DCT();
    }

    @RequestMapping(value={"", "/", "home"}, method = RequestMethod.GET)
    public String main(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }

    @GetMapping("/exetest")
    public String exeTest() {
        dct.funcTest();
        return "home";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile uploadfile, RedirectAttributes redirectAttributes) {
        if (uploadfile.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:home";
        }

        try {
//            BufferedImage cleanImage = dct.DCTdenoising(uploadfile.getBytes());
//            dct.DCTdenoising(uploadfile.getBytes());

//            dct.testCreateImage();

            // Get a BufferedImage object from a byte array
            InputStream in = new ByteArrayInputStream(uploadfile.getBytes());
            BufferedImage originalImage = ImageIO.read(in);
            BufferedImage output = null;

            // Get image dimensions
            int height = originalImage.getHeight();
            int width = originalImage.getWidth();
//            dct.testColorTransform(originalImage, output, width, height);
//            dct.boofTest(originalImage);
            dct.ColorTransform(originalImage, output);
        }

        catch (IOException e) {
            System.out.println("Failed create gray scale image from input file");
            e.printStackTrace();
        }

        try {
            byte[] bytes = uploadfile.getBytes();
            String filePath = FULL_PATH + uploadfile.getOriginalFilename();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message","File image uploaded '" + filePath + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/result";
    }

}
