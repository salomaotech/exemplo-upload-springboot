package com.salomaotech.upload.controladores;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Controlador {

    private final Path pathRaiz = Paths.get("./uploads");

    @GetMapping("/")
    public ModelAndView home() {

        ModelAndView view = new ModelAndView("Index");
        List<String> imagens = new ArrayList();

        try {

            DirectoryStream<Path> stream = Files.newDirectoryStream(pathRaiz);

            for (Path path : stream) {

                imagens.add("/uploads/" + path.getFileName().toString());

            }

        } catch (Exception ex) {

        }

        view.addObject("imagens", imagens);

        return view;

    }

    @PostMapping("/upload")
    public ModelAndView upload(MultipartFile file) {

        try {

            Files.createDirectories(pathRaiz);
            Files.copy(file.getInputStream(), pathRaiz.resolve(file.getOriginalFilename()));

        } catch (Exception ex) {

        }

        return new ModelAndView("redirect:/");

    }

    @GetMapping("/uploads/{arquivo:.+}")
    public ResponseEntity<Resource> carregarImagem(@PathVariable String arquivo) {

        try {

            Resource resource = new UrlResource(pathRaiz.resolve(arquivo).toUri());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; arquivo=\"" + resource.getFilename() + "\"").body(resource);

        } catch (Exception ex) {

            return ResponseEntity.badRequest().build();

        }

    }

}
